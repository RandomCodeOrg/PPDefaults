package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

public @interface Bind {

	String value();
	Class<? extends BindingSourceFactory> provider() default BindingSource.class;
	String identifier() default "";
	
	
}
