package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

public class DefaultValidationInformation implements ValidationInformation<Object> {

	public DefaultValidationInformation() {
	}

	@Override
	public RuntimeException createException(Validator<? extends Object> validator, Object value, StackTraceElement[] stackTrace) {
		RuntimeException e = new IllegalArgumentException(String.format("The given argument (%s) is invalid.", "" + value));
		e.setStackTrace(stackTrace);
		return e;
	}

}
