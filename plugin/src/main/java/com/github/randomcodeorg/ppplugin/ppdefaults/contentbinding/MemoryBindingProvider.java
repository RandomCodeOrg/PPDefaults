package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

import java.util.HashMap;
import java.util.Map;

public class MemoryBindingProvider  implements BindingProvider{

	
	private Map<String, Object> values = new HashMap<String, Object>();
	
	@Override
	public void load() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, T defaultValue) {
		if(!values.containsKey(key)) return defaultValue;
		try{
			return (T) values.get(key);
		}catch(ClassCastException e){
			return defaultValue;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSafe(String key, T defaultValue, Class<T> valueType) {
		if(!values.containsKey(key)) return defaultValue;
		Object curr;
		try{
			curr = values.get(key);
			if(curr == null) return null;
			if(!valueType.isAssignableFrom(curr.getClass())) return defaultValue;
			return (T) values.get(key);
		}catch(ClassCastException e){
			return defaultValue;
		}
	}

	@Override
	public boolean contains(String key) {
		return values.containsKey(key);
	}

	@Override
	public boolean remove(String key) {
		if(!contains(key)) return false;
		values.remove(key);
		return true;
	}

	@Override
	public <T> T set(String key, T value) {
		values.put(key, (Object) value);
		return value;
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}

}
