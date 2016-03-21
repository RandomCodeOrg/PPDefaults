package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Validation(NotEmptyValidator.class)
public @interface NotEmpty {

}
