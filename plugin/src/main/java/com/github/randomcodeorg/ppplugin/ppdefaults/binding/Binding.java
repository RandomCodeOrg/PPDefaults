package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

public class Binding<T> extends ReadOnlyBinding<T> {
	
	public Binding() {
		super();
	}

	public Binding(String key, BindingSourceFactory provider, T value) {
		super(key, provider, value);
	}

	public Binding(String key, BindingSourceFactory provider) {
		super(key, provider);
	}

	public Binding(String key, Class<? extends BindingSourceFactory> provider, T value) {
		super(key, provider, value);
	}

	public Binding(String key, Class<? extends BindingSourceFactory> provider) {
		super(key, provider);
	}

	public Binding(String key, String sourceIdentifier, BindingSourceFactory provider, T value) {
		super(key, sourceIdentifier, provider, value);
	}

	public Binding(String key, String sourceIdentifier, BindingSourceFactory provider) {
		super(key, sourceIdentifier, provider);
	}

	public Binding(String key, String sourceIdentifer, Class<? extends BindingSourceFactory> provider, T value) {
		super(key, sourceIdentifer, provider, value);
	}

	public Binding(String key, String sourceIdentifer, Class<? extends BindingSourceFactory> provider) {
		super(key, sourceIdentifer, provider);
	}

	public void set(T value) {
		this.value = value;
		setRequested();
		if (isValid()) {
			provider.set(key, value);
		}
	}
	

}
