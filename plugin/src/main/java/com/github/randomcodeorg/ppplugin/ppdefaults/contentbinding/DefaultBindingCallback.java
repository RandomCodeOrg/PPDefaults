package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

public class DefaultBindingCallback<T> implements BindingCallback<T> {

	private final BindingProvider provider;
	private final KeyComposer composer;
	private final T defaultValue;
	
	public DefaultBindingCallback(BindingProvider provider, T defaultValue, KeyComposer composer) {
		this.defaultValue = defaultValue;
		this.provider = provider;
		this.composer = composer;
	}

	@Override
	public T get() {
		return provider.get(composer.getKey(), defaultValue);
	}

	@Override
	public void set(T value) {
		provider.set(composer.getKey(), value);
	}

}
