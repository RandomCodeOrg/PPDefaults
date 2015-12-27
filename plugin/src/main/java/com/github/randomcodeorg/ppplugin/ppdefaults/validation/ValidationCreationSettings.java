package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.reflect.Method;

import com.github.randomcodeorg.ppplugin.data.BuildLog;

import javassist.CtClass;

public abstract class ValidationCreationSettings {
	
	
	
	public boolean checkCompatibility(BuildLog log, CtClass parameterType){
		Class<? extends Validator<?>> valType = getValidatorClass();
		for(Method m : valType.getDeclaredMethods()){
			if(m.getParameterCount() > 1){
				log.info(String.format("%s: %s", m.getName(), m.getParameterTypes()[0].getName()));
			}
		}
		return false;
	}
	
	
	public abstract Class<? extends Validator<?>> getValidatorClass();
	
}
