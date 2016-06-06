package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;


public interface BindingCallback<T> extends ReadOnlyBindingCallback<T> {
	
	void set(T value);

}
