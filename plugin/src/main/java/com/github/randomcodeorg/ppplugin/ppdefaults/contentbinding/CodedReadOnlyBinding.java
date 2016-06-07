package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

import java.util.HashSet;
import java.util.Set;

public class CodedReadOnlyBinding<T> {

	private final ReadOnlyBindingCallback<T> callback;
	private final Set<BindingListener<? super T>> listeners = new HashSet<BindingListener<? super T>>();
	
	public CodedReadOnlyBinding(ReadOnlyBindingCallback<T> callback){
		this.callback = callback;
	}
	
	public T get(){
		return callback.get();
	}
	
	public void addListener(BindingListener<? super T> listener){
		if(listener == null) throw new IllegalArgumentException("The listener must not be null.");
		listeners.add(listener);
	}
	
	public void removeListener(BindingListener<? super T> listener){
		listeners.remove(listener);
	}
	
	protected void notify(T oldValue, T newValue){
		for(BindingListener<? super T> l : listeners)
			l.valueChanged(this, oldValue, newValue);
	}
	
}
