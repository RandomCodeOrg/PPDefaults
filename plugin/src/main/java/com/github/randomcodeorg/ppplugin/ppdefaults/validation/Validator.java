package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

public interface Validator<T> {

	
	void validate(T value, ValidationInformation<? super T> information, StackTraceElement[] stackTrace) throws RuntimeException;
	
	Class<T> getValueType();
	
}
