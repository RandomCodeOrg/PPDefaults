package com.github.randomcodeorg.ppplugin.ppdefaults;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.ppplugin.PContext;
import com.github.randomcodeorg.ppplugin.ppdefaults.logging.AbstractLoggingProcessor;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * A helper class to simplify the modification of generated class files.
 * Please see the documentation of the {@link ByteCodeHelper#edit(CtClass, Class)} method.
 * @author Marcel Singer
 *
 */
public class ByteCodeHelper {

	private final PContext context;
	private ClassPool classPool;
	private List<ClassPath> classPaths = new ArrayList<ClassPath>();
	private final Map<CtClass, Class<?>> editedClasses = new HashMap<CtClass, Class<?>>();

	/**
	 * Creates a new {@link ByteCodeHelper} using the given context.
	 * @param context The context to be used by this {@link ByteCodeHelper}.
	 */
	public ByteCodeHelper(PContext context) {
		this.context = context;
	}

	private void createClassPool() throws NotFoundException {
		if (classPool != null)
			return;
		classPool = new ClassPool(true);
		classPaths.add(classPool.appendClassPath(context.getClassRoot().getAbsolutePath()));
		for (String cp : context.getClassPaths()) {
			try {
				ClassPath tmp = classPool.appendClassPath(cp);
				classPaths.add(tmp);
			} catch (NotFoundException e) {
				context.getLog().warn(String.format("Could not add class path: %s", cp));
			}
		}
	}

	/**
	 * Finds the {@link Method} corresponding to the given javassist {@link CtMethod}.
	 * @param clazz The {@link CtClass} that contains the {@link CtMethod}.
	 * @param m The reflection {@link Method} thats corresponding {@link CtMethod} should be returned.
	 * @return The {@link CtMethod} with the same name and input parameters as the given reflection {@link Method}.
	 */
	public static CtMethod findMethod(CtClass clazz, Method m) {
		try {
			CtClass[] parameterTypes;
			Class<?>[] runtimeParameterTyes = m.getParameterTypes();
			for (CtMethod ctMethod : clazz.getDeclaredMethods(m.getName())) {
				parameterTypes = ctMethod.getParameterTypes();
				if (parameterTypes.length != runtimeParameterTyes.length)
					continue;
				boolean missmatch = false;
				for (int i = 0; i < parameterTypes.length; i++) {
					if (!runtimeParameterTyes[i].getName().equals(parameterTypes[i].getName())) {
						missmatch = true;
						break;
					}
				}
				if (missmatch)
					continue;
				return ctMethod;
			}
			return null;
		} catch (NotFoundException e) {
			return null;
		}
	}

	/**
	 * Returns the javassist {@link ClassPool} used by this instance.
	 * @return The javassist {@link ClassPool} used by this instance.
	 * @throws NotFoundException If the class pool could not be created.
	 */
	public ClassPool getClassPool() throws NotFoundException {
		if (classPool == null)
			createClassPool();
		return classPool;
	}

	/**
	 * <p>Marks the given class as edited. A future call to {@link ByteCodeHelper#commit()} will compile and save the changes of this class.</p>
	 * <p><b>Note:</b> Some processors in this project ({@link AbstractClassModificationProcessor}, {@link AbstractLoggingProcessor}, ...) will call the {@link ByteCodeHelper#commit()} method automatically.</p>
	 * @param cl The {@link CtClass} to edit.
	 * @param runtimeClass The corresponding reflection {@link Class}.
	 * @return {@code false} if the given class is already marked as edited.
	 * @throws IllegalStateException Is thrown if there is no {@link ClassPool} for this instance. See {@link ByteCodeHelper#getClassPool()}.
	 */
	public boolean edit(CtClass cl, Class<?> runtimeClass) throws IllegalStateException {
		if (classPool == null)
			throw new IllegalStateException("There is no ClassPool for this ByteCodeHelper");
		if (!editedClasses.containsKey(cl)) {
			editedClasses.put(cl, runtimeClass);
			return true;
		}
		return false;
	}

	/**
	 * <p>Returns a existing field or creates one.</p>
	 * <p><b>Note:</b> This method will not call {@link ByteCodeHelper#edit(CtClass, Class)}. One should do this himself.</p>
	 * @param ctClass The {@link CtClass} that contains the field.
	 * @param typeName A string containing the canonical type of the field to return. 
	 * @param fieldPrefix The prefix of the fields name.
	 * @param visibility The visibility of the field (public, private, protected).
	 * @param isStatic {@code true} if the field is static.
	 * @param isFinal {@code true} if the field is final.
	 * @param initValue The initial value of the field (4, new java.lang.Object(), new java.lang.StringBuilder()) or {@code null}.
	 * @return The found or created field.
	 * @throws CannotCompileException If the field could not be found and the creation failed.
	 */
	public CtField getOrCreateField(CtClass ctClass, String typeName, String fieldPrefix, String visibility,
			boolean isStatic, boolean isFinal, String initValue) throws CannotCompileException {
		for (CtField f : ctClass.getDeclaredFields()) {
			if (!f.getName().startsWith(fieldPrefix))
				continue;
			try {
				if (f.getType().getName().equals(typeName))
					return f;
			} catch (NotFoundException e) {

			}
		}
		String name = ctClass.makeUniqueName(fieldPrefix);
		String staticV = "";
		if (isStatic)
			staticV = "static";
		String finalV = "";
		if (isFinal)
			finalV = "final";
		if (initValue == null || initValue.isEmpty()) {
			CtField f = CtField.make(String.format("%s %s %s %s %s;", visibility, staticV, finalV, typeName, name),
					ctClass);
			ctClass.addField(f);
			return f;
		} else {
			CtField f = CtField.make(
					String.format("%s %s %s %s %s = %s;", visibility, staticV, finalV, typeName, name, initValue),
					ctClass);
			ctClass.addField(f);
			return f;
		}
	}

	/**
	 * Compiles the changes and applies them to the corresponding .class-files.
	 * <p><b>Note:</b> Some processors in this project ({@link AbstractClassModificationProcessor}, {@link AbstractLoggingProcessor}, ...) will call the {@link ByteCodeHelper#commit()} method automatically.</p>
	 * @param continueOnException {@code false} if the process should abort if an exception is thrown. {@code true} if the process should continue if an exception is thrown (in this case the exception will be logged). 
	 * @throws CannotCompileException Is thrown if the changes could not be compiled and {@literal continueOnException} is {@code false}.
	 * @throws IOException Is thrown if the changes could not be written to the corresponding .class-files and {@literal continueOnException} is {@code false}.
	 */
	public void commit(boolean continueOnException) throws CannotCompileException, IOException {
		if (classPool == null)
			return;
		ClassPool cp = classPool;
		for (CtClass c : editedClasses.keySet()) {
			cp.importPackage(c.getPackageName());
			try {
				c.toBytecode(new DataOutputStream(context.modify(editedClasses.get(c))));
			} catch (IOException e) {
				handleCompilationException(e, c, continueOnException);
			} catch (CannotCompileException e) {
				handleCompilationException(e, c, continueOnException);
			} catch (Error e) {
				handleCompilationException(e, c, continueOnException);
			} catch (RuntimeException e) {
				handleCompilationException(e, c, continueOnException);
			}
		}
		editedClasses.clear();
	}

	/**
	 * The same as {@code ByteCodeHelper#commit(true)}.
	 */
	public void commit() {
		try {
			commit(true);
		} catch (CannotCompileException e) {
		} catch (IOException e) {
		}
	}

	private <T extends Throwable> void handleCompilationException(T e, CtClass c, boolean continueOnException)
			throws T {
		if (!continueOnException)
			throw e;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		context.getLog()
				.warn(String.format("Could not compile changes in the class %s. The following exception occured: %s",
						c.getName(), sw.toString()));
	}

	/**
	 * Releases the resources of created by this instance.
	 */
	public void releaseResources() {
		if (classPool != null) {
			for (ClassPath cp : classPaths)
				classPool.removeClassPath(cp);
			classPaths.clear();
		}
	}

}
