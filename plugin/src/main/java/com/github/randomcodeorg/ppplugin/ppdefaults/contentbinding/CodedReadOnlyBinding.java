package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;


public class CodedReadOnlyBinding<T> {

	private final ReadOnlyBindingCallback<T> callback;
	
	public CodedReadOnlyBinding(ReadOnlyBindingCallback<T> callback){
		this.callback = callback;
	}
	
	public T get(){
		return callback.get();
	}
	
}
