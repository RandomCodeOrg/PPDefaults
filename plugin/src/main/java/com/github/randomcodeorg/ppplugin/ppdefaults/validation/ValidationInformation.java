package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

public interface ValidationInformation<T> {
	
	RuntimeException createException(Validator<? extends T> validator, T value, StackTraceElement[] stackTrace);
}
