package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CheckNotPositive {

	Class<? extends RuntimeException> value() default IllegalArgumentException.class;

}
