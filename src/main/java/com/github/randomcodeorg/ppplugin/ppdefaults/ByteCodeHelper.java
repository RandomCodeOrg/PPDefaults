package com.github.randomcodeorg.ppplugin.ppdefaults;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.ppplugin.PContext;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class ByteCodeHelper {

	private final PContext context;
	private ClassPool classPool;
	private List<ClassPath> classPaths = new ArrayList<ClassPath>();
	private final Map<CtClass, Class<?>> editedClasses = new HashMap<CtClass, Class<?>>();

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
	
	public ClassPool getClassPool() throws NotFoundException{
		if(classPool == null) createClassPool();
		return classPool;
	}

	public boolean edit(CtClass cl, Class<?> runtimeClass){
		if(classPool == null) throw new IllegalStateException("There is no ClassPool for this ByteCodeHelper");
		if(!editedClasses.containsKey(cl)){
			editedClasses.put(cl, runtimeClass);
			return true;
		}
		return false;
	}
	
	public CtField getOrCreateField(CtClass ctClass, String typeName, String fieldPrefix, String visibility, boolean isStatic, boolean isFinal, String initValue) throws CannotCompileException{
		for(CtField f : ctClass.getDeclaredFields()){
			if(!f.getName().startsWith(fieldPrefix)) continue;
			try {
				if(f.getType().getName().equals(typeName)) return f;
			} catch (NotFoundException e) {
				
			}
		}
		String name = ctClass.makeUniqueName(fieldPrefix);
		String staticV = "";
		if(isStatic) staticV = "static";
		String finalV = "";
		if(isFinal) finalV = "final";
		if(initValue == null || initValue.isEmpty()){
			CtField f = CtField.make(String.format("%s %s %s %s %s;", visibility, staticV, finalV, typeName, name), ctClass);
			ctClass.addField(f);
			return f;
		}else{
			CtField f = CtField.make(String.format("%s %s %s %s %s = %s;", visibility, staticV, finalV, typeName, name, initValue), ctClass);
			ctClass.addField(f);
			return f;
		}
	}
	
	public void commit(boolean continueOnException) throws CannotCompileException, IOException {
		if(classPool == null) return;
		ClassPool cp = classPool;
		for(CtClass c : editedClasses.keySet()){
			cp.importPackage(c.getPackageName());
			try{
				c.toBytecode(new DataOutputStream(context.modify(editedClasses.get(c))));
			}catch(IOException e){
				handleCompilationException(e, c, continueOnException);
			}catch(CannotCompileException e){
				handleCompilationException(e, c, continueOnException);
			}catch(Error e){
				handleCompilationException(e, c, continueOnException);
			}catch (RuntimeException e) {
				handleCompilationException(e, c, continueOnException);
			}
		}
		editedClasses.clear();
	}
	
	public void commit(){
		try {
			commit(true);
		} catch (CannotCompileException e) {
		} catch (IOException e) {
		}
	}
	
	private <T extends Throwable> void handleCompilationException(T e, CtClass c, boolean continueOnException) throws T{
		if(!continueOnException) throw e;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		context.getLog()
				.warn(String.format(
						"Could not compile changes in the class %s. The following exception occured: %s",
						c.getName(), sw.toString()));
	}
	
	public void releaseRessources() {
		if (classPool != null) {
			for (ClassPath cp : classPaths)
				classPool.removeClassPath(cp);
			classPaths.clear();
		}
	}

}
