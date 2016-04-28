package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBindingSourceFactory implements BindingSourceFactory {

	private static final Map<Class<? extends BindingSourceFactory>, Map<String, BindingSource>> sourceCache = new HashMap<Class<? extends BindingSourceFactory>, Map<String, BindingSource>>();
	
	
	public AbstractBindingSourceFactory() {
	
	}
	
	
	@Override
	public final BindingSource getBindingSource(String identifier) {
		synchronized (sourceCache) {
			if(!sourceCache.containsKey(getClass())) sourceCache.put(getClass(), new HashMap<String, BindingSource>());
			Map<String, BindingSource> cache = sourceCache.get(getClass());
			if(!cache.containsKey(identifier)){
				cache.put(identifier, createBindingSource(identifier));
			}
			return cache.get(identifier);
		}
	}
	
	protected abstract BindingSource createBindingSource(String identifier);

}
