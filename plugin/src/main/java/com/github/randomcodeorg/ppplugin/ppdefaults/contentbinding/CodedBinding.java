package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

public class CodedBinding<T> extends CodedReadOnlyBinding<T> {

	private final BindingCallback<T> callback;

	public CodedBinding(BindingCallback<T> callback) {
		super(callback);
		this.callback = callback;
	}

	public void set(T value) {
		T oldValue = get();
		boolean changed = oldValue != value;
		callback.set(value);
		if (changed) {
			notify(oldValue, value);
		}
	}

}
