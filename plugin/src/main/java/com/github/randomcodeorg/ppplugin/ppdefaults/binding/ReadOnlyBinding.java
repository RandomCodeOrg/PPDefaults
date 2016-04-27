package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

import com.google.common.base.Objects;

public class ReadOnlyBinding<T> implements BindingListener {

	protected T value;

	protected final String key;
	protected final BindingSource provider;
	private boolean isValid = true;
	private T defaultValue;
	private boolean requested = false;

	public ReadOnlyBinding() {
		throw new ClassFormatError();
	}

	public ReadOnlyBinding(String key, String sourceIdentifer, Class<? extends BindingSourceFactory> provider) {
		this.key = key;
		try {
			BindingSourceFactory factory = provider.newInstance();
			this.provider = factory.getBindingSource(sourceIdentifer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		init();
	}

	public ReadOnlyBinding(String key, String sourceIdentifer, Class<? extends BindingSourceFactory> provider,
			T value) {
		this(key, sourceIdentifer, provider);
		this.defaultValue = value;
	}

	public ReadOnlyBinding(String key, Class<? extends BindingSourceFactory> provider) {
		this(key, "", provider);
	}

	public ReadOnlyBinding(String key, Class<? extends BindingSourceFactory> provider, T value) {
		this(key, provider);
		this.defaultValue = value;
	}

	public ReadOnlyBinding(String key, String sourceIdentifier, BindingSourceFactory provider) {
		this.provider = provider.getBindingSource(sourceIdentifier);
		this.key = key;
		init();
	}

	public ReadOnlyBinding(String key, String sourceIdentifier, BindingSourceFactory provider, T value) {
		this(key, sourceIdentifier, provider);
		this.defaultValue = value;
	}

	public ReadOnlyBinding(String key, BindingSourceFactory provider) {
		this(key, "", provider);
	}

	public ReadOnlyBinding(String key, BindingSourceFactory provider, T value) {
		this(key, provider);
		this.defaultValue = value;
	}

	private void init() {
		provider.registerListener(key, this);
	}

	public T get() {
		if(!requested){
			value = provider.get(key, defaultValue);
			requested = true;
		}
		return value;
	}
	
	

	protected boolean isValid() {
		return isValid;
	}

	@Override
	public void bindingRemoved(BindingSource source, String key) {
		isValid = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> void valueUpdated(BindingSource source, String key, S oldValue, S newValue) {
		if (provider != source || !Objects.equal(this.key, key))
			return;
		try {
			T val = (T) newValue;
			this.value = val;
		} catch (ClassCastException e) {

		}
	}

}
