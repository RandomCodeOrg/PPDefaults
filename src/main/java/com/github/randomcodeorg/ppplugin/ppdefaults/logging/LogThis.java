package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogThis{

	LogLevel value() default LogLevel.DEBUG;
	boolean logFields() default false;
	boolean ignoreStaticFinal() default true;
	
}
