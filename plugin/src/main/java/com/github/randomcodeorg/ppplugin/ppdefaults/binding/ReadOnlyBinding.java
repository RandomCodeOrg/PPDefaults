package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

public class ReadOnlyBinding<T> {

	protected T value;
	
	
	public ReadOnlyBinding() {
		
	}
	
	public ReadOnlyBinding(String key, Class<? extends BindingSourceFactory> provider){
		
	}
	
	public ReadOnlyBinding(String key, BindingSourceFactory provider){
		
	}
	
	public T get(){
		return value;
	}

}
