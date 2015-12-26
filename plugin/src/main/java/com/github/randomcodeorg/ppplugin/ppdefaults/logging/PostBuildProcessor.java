package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.ppplugin.PContext;
import com.github.randomcodeorg.ppplugin.PProcessor;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;

public class PostBuildProcessor implements PProcessor {

	private static final LogLevel CATCH_WARN_LEVEL = LogLevel.WARNING;

	public PostBuildProcessor() {

	}

	public void init(PContext context) {

	}

	
	public void run(PContext context) {
		context.getLog().info("Inserting automated log calls...");
		ClassPool cp = new ClassPool(true);
		List<ClassPath> classPaths = new ArrayList<ClassPath>();
		try {
			classPaths.add(cp.appendClassPath(context.getClassRoot().getAbsolutePath()));
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
		for (String classPath : context.getClassPaths()) {
			try {
				classPaths.add(cp.appendClassPath(classPath));
			} catch (NotFoundException e) {
				context.getLog().warn(String.format("Could not add class path: %s", classPath));
			}
		}
		Map<Class<?>, List<Method>> methodsMap = new HashMap<Class<?>, List<Method>>();
		Map<Method, LogThis> annotations = new HashMap<Method, LogThis>();
		buildMaps(context, methodsMap, annotations);
		Map<CtClass, Class<?>> editedClasses = new HashMap<CtClass, Class<?>>();
		Map<CtClass, String> logFieldNames = new HashMap<CtClass, String>();
		insertAnnotationsLog(context, cp, methodsMap, annotations, logFieldNames, editedClasses);
		insertCatchLog(context, cp, editedClasses, logFieldNames);
		for (CtClass editedClass : editedClasses.keySet()) {
			cp.importPackage(editedClass.getPackageName());
			try {
				editedClass.toBytecode(new DataOutputStream(context.modify(editedClasses.get(editedClass))));
			} catch (CannotCompileException e) {
				context.getLog().debug(e);
				context.getLog()
						.warn(String.format(
								"Could not inject auto logging statements for class %s because the following compilation error occured: %s",
								editedClass.getName(), e.getMessage()));
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				context.getLog()
						.warn(String.format(
								"Could not inject auto logging statements for class %s. The following exception occured: %s",
								editedClass.getName(), sw.toString()));
			}
		}
		for (ClassPath classPath : classPaths)
			cp.removeClassPath(classPath);
	}

	private void buildMaps(PContext context, Map<Class<?>, List<Method>> methodsMap,
			Map<Method, LogThis> annotationsMap) {
		LogThis classAnnot;
		boolean classLevel;
		for (Class<?> cl : context.getClasses()) {
			if (cl.isInterface())
				continue;
			classLevel = cl.isAnnotationPresent(LogThis.class);
			if (classLevel)
				classAnnot = cl.getAnnotation(LogThis.class);
			else
				classAnnot = null;
			try {
				for (Method m : cl.getDeclaredMethods()) {
					if (m.isAnnotationPresent(Stealth.class))
						continue;
					if (Modifier.isAbstract(m.getModifiers()))
						continue;
					if (m.isAnnotationPresent(LogThis.class)) {
						if (!methodsMap.containsKey(cl))
							methodsMap.put(cl, new ArrayList<Method>());
						methodsMap.get(cl).add(m);
						annotationsMap.put(m, m.getAnnotation(LogThis.class));
					} else if (classLevel) {
						if (!methodsMap.containsKey(cl))
							methodsMap.put(cl, new ArrayList<Method>());
						methodsMap.get(cl).add(m);
						annotationsMap.put(m, classAnnot);
					}

				}
			} catch (NoClassDefFoundError e) {

			}
		}
	}

	private void insertAnnotationsLog(PContext context, ClassPool cp, Map<Class<?>, List<Method>> methodsMap,
			Map<Method, LogThis> annotationsMap, Map<CtClass, String> logFieldNames,
			Map<CtClass, Class<?>> editedClasses) {
		CtClass editClass;

		for (Class<?> cl : methodsMap.keySet()) {
			context.getLog().info(String.format("Creating logs for %s", cl.getName()));
			try {
				editClass = cp.getCtClass(cl.getCanonicalName());
				cp.importPackage(editClass.getPackageName());
			} catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
			if (editClass.isFrozen())
				editClass.defrost();
			String loggerFieldName = editClass.makeUniqueName("al_");
			try {
				editClass.addField(CtField.make(String.format(
						"private static org.slf4j.Logger %s = org.slf4j.LoggerFactory.getLogger(%s.class);",
						loggerFieldName, cl.getSimpleName()), editClass));
				logFieldNames.put(editClass, loggerFieldName);
			} catch (CannotCompileException e) {
				throw new RuntimeException(e);
			}
			for (Method m : methodsMap.get(cl)) {
				try {
					prepareMethod(context, loggerFieldName, m, cl, editClass, annotationsMap.get(m));
				} catch (CannotCompileException e) {
					throw new RuntimeException(e);
				}
			}
			editedClasses.put(editClass, cl);

		}
	}

	private String insertLogField(CtClass c) throws CannotCompileException {
		String loggerFieldName = c.makeUniqueName("al_");
		c.addField(CtField
				.make(String.format("private static org.slf4j.Logger %s = org.slf4j.LoggerFactory.getLogger(%s.class);",
						loggerFieldName, c.getSimpleName()), c));
		return loggerFieldName;
	}

	private void insertCatchLog(final PContext context, ClassPool cp, final Map<CtClass, Class<?>> editedClasses,
			final Map<CtClass, String> logFieldNames) {
		for (Class<?> c : context.getClasses()) {
			final Class<?> cL = c;
			try {
				if (c.getCanonicalName() == null)
					continue;
				final CtClass editClass = cp.get(c.getCanonicalName());

				editClass.instrument(new ExprEditor() {
					@Override
					public void edit(Handler h) throws CannotCompileException {
						if (h.isFinally())
							return;
						String logFieldName;
						if (!logFieldNames.containsKey(editClass)) {
							logFieldName = insertLogField(editClass);
							logFieldNames.put(editClass, logFieldName);
						} else {
							logFieldName = logFieldNames.get(editClass);
						}
						doInsertCatchLog(context, editClass, h, logFieldName);
						if (!editedClasses.containsKey(editClass))
							editedClasses.put(editClass, cL);
					}
				});
			} catch (NotFoundException e) {
				handleCompilationsException(context, cL, e);
			}catch (CannotCompileException e) {
				handleCompilationsException(context, cL, e);
			}
		}
	}
	
	private void handleCompilationsException(PContext context, Class<?> c, Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		context.getLog()
				.warn(String.format(
						"Could not inject exception logging for class %s. The following exception occured: %s",
						c.getCanonicalName(), sw.toString()));
	}

	private void doInsertCatchLog(PContext context, CtClass clazz, Handler h, String loggerVar)
			throws CannotCompileException {
		CtBehavior behavior = h.where();
		String location;
		if (behavior == null) {
			location = String.format("%s:%d", h.getEnclosingClass().getSimpleName(), h.getLineNumber());
		} else {
			location = String.format("%s:%d", behavior.getLongName(), h.getLineNumber());
		}
		context.getLog().info(String.format("Inserting catch log for %s", location));
		String code;
		code = String.format(
				"{ %s.%s(\"Caught an exception of the type \" +  $1.getClass().getCanonicalName() + \" at %s\\n\\tMessage: \" + $1.getMessage() ); }",
				loggerVar, CATCH_WARN_LEVEL.getLevelMethodName(), location);
		h.insertBefore(code);
	}

	private void prepareMethod(PContext context, String loggerFieldName, Method m, Class<?> cl, CtClass editClass,
			LogThis annot) throws CannotCompileException {
		for (CtMethod editMethod : editClass.getDeclaredMethods()) {
			if (!m.getName().equals(editMethod.getName()))
				continue;

			CtClass[] editTypes;
			try {
				editTypes = editMethod.getParameterTypes();
			} catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
			if (editTypes.length != m.getParameterCount())
				continue;
			boolean missmatch = false;
			for (int i = 0; i < m.getParameterCount(); i++) {
				if (!m.getParameterTypes()[i].getName().equals(editTypes[i].getName())) {
					missmatch = true;
					break;
				}
			}
			if (missmatch)
				continue;
			String[] args = getMethodVariableNames(context, editMethod, m);
			insertLog(context, loggerFieldName, m, editMethod, args, cl, editClass, annot);
		}

	}

	private void insertLog(PContext context, String loggerFieldName, Method m, CtMethod editMethod,
			String[] parameterNames, Class<?> cl, CtClass editClass, LogThis annot) throws CannotCompileException {
		StringBuilder sb = new StringBuilder();
		sb.append("\"Called ");
		sb.append(String.format("%s.%s()", m.getDeclaringClass().getSimpleName(), m.getName()));
		if (parameterNames.length > 0) {
			sb.append(" with:");
			for (int i = 0; i < parameterNames.length; i++) {
				String param = parameterNames[i];
				if (param != null) {
					sb.append(String.format("\\n\\t%s: ", param));
					sb.append(String.format("\" + $%d + \"", (i + 1)));
				} else {
					sb.append(String.format("\\n\\targ%d: ", i));
					sb.append("@Stealth");
				}
			}
		}
		if (annot.logFields()) {
			Field[] fields = m.getDeclaringClass().getDeclaredFields();
			if (fields.length > 0) {
				if (parameterNames.length == 0)
					sb.append(" with:");
				else
					sb.append("\\n");
				for (Field f : fields) {
					if (f.isAnnotationPresent(Stealth.class))
						continue;
					if (annot.ignoreStaticFinal()) {
						if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
							continue;
					}
					sb.append(String.format("\\n\\t%s [field]: ", f.getName()));
					sb.append(String.format("\" + %s + \"", f.getName()));
				}
			}

		}
		sb.append("\"");
		String toInsert = String.format("%s.%s(%s);", loggerFieldName, annot.value().getLevelMethodName(),
				sb.toString());
		context.getLog().debug("Inserting: " + toInsert);
		editMethod.insertBefore(toInsert);
	}

	private String[] getMethodVariableNames(PContext context, CtMethod cm, Method method) {
		Parameter[] params = method.getParameters();
		String[] result = new String[params.length];
		Parameter p;
		for (int i = 0; i < params.length; i++) {
			p = params[i];
			if (p.isAnnotationPresent(Stealth.class))
				continue;
			result[i] = String.format("arg%d", i);
		}
		return result;
	}

}
