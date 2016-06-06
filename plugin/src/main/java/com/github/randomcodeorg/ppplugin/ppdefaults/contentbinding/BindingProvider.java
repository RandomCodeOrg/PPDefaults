package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

/**
 * 
 * @author Marcel Singer
 *
 */
public interface BindingProvider {

	void load();
	
	<T> T get(String key, T defaultValue);
	
	<T> T getSafe(String key, T defaultValue, Class<T> valueType);
	
	
	boolean contains(String key);
	
	boolean remove(String key);
	
	<T> T set(String key, T value);
	
	void flush();
	
	void close();
	
}
