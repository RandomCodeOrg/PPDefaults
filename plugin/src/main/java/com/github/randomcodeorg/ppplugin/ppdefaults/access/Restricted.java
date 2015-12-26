package com.github.randomcodeorg.ppplugin.ppdefaults.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Restricted {

	Class<? extends AccessController> controller();
	String extra() default "";
	boolean includeParameters() default false;

}
