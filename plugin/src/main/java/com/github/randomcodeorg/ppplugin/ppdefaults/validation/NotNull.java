package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Validation(NotNullValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

}
