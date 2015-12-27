package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.ANNOTATION_TYPE})
@Repeatable(Validations.class)
public @interface Validation {
	Class<? extends Validator<?>> value();
}
