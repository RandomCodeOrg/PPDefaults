package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

public class NotNullValidator implements Validator<Object> {

	
	
	@Override
	public void validate(Object value, ValidationInformation<? super Object> information, StackTraceElement[] stackTrace) throws RuntimeException {
		if(value == null){
			throw information.createException(this, value, stackTrace);
		}
	}

	@Override
	public Class<Object> getValueType() {
		return Object.class;
	}

	
}
