package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Binding {

	String provider() default "";
	Class<? extends ProviderFactory> providerFactory() default MemoryBindingProviderFactory.class;
	
}
