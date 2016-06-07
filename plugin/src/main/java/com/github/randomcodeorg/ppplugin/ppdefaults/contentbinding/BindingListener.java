package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

public interface BindingListener<T> {
	
	
	void valueChanged(CodedReadOnlyBinding<?> binding, T oldValue, T newValue);
	

}
