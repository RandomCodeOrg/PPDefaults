package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

public interface BindingSourceFactory {
	
	
	/**
	 * Returns the binding source for the given identifier.
	 * @param identifier The identifier of the binding source to be returned.
	 * @return The binding source for the given identifier.
	 */
	BindingSource getBindingSource(String identifier);

	
}
