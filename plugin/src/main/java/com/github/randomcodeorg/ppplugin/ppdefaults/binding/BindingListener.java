package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

public interface BindingListener {

	void bindingRemoved(BindingSource source, String key);
	
	<T> void valueUpdated(BindingSource source, String key, T oldValue, T newValue);
	
}
