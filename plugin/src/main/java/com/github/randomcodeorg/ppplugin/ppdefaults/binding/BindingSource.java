package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

import java.util.NoSuchElementException;

/**
 * 
 * @author Marcel Singer
 *
 */
public interface BindingSource extends BindingSourceFactory {

	
	public <T> T get(String key, T defaultValue) throws NoSuchElementException, IllegalArgumentException;
	
	public <T> void set(String key, T value) throws NoSuchElementException, IllegalArgumentException;
	
	public boolean contains(String key) throws NoSuchElementException, IllegalArgumentException;
	
	public boolean remove(String key) throws NoSuchElementException, IllegalArgumentException;
	
	public void load();
	
	public void flush();
	
	public void close();
	
	public void registerListener(String key, BindingListener listener) throws NoSuchElementException, IllegalArgumentException;

	public void releaseListener(String key, BindingListener listener) throws IllegalArgumentException;
	
	@Override
	BindingSource getBindingSource(String identifier);
	
}
