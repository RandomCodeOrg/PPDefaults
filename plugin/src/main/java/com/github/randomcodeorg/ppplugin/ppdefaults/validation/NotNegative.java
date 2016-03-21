package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Validation(NotNegativeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNegative {
	
	

}
