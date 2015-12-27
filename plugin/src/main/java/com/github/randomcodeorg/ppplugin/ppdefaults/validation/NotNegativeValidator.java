package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

public class NotNegativeValidator implements Validator<Number> {

	public NotNegativeValidator() {
		
	}

	@Override
	public void validate(Number value, ValidationInformation<? super Number> information, StackTraceElement[] stackTrace) throws RuntimeException {
		if(value == null) return;
		if(value.floatValue() < 0){
			throw information.createException(this, value, stackTrace);
		}
		
	}

	@Override
	public Class<Number> getValueType() {
		return Number.class;
	}

}
