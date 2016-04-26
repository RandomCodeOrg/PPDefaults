package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

public class Binding<T> extends ReadOnlyBinding<T>{
	
	public Binding() {
		super();
	}

	public Binding(String key, BindingSource provider) {
		super(key, provider);
	}

	public Binding(String key, Class<? extends BindingSourceFactory> provider) {
		super(key, provider);
	}

	public void set(T value){
		this.value = value;
	}

}
