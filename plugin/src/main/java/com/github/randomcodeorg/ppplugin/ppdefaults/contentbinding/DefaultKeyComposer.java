package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

public class DefaultKeyComposer implements KeyComposer, BindingListener<Object> {

	private final CodedReadOnlyBinding<?>[] values; 
	private final BindingProvider provider;
	private String key;
	private final String name;
	
	public DefaultKeyComposer(BindingProvider provider, String name, CodedReadOnlyBinding<?>[] values) {
		this.values = values;
		this.provider = provider;
		this.name = name;
		if(values.length > 0) key = createKey();
		for(int i=0; i<values.length; i++){
			values[i].addListener(this);
		}
	}

	@Override
	public String getKey() {
		if(values.length == 0) return name;
		return String.format("%s.%s", key, name);
	}
	
	private String getFullKey(Object[] ids){
		if(values.length == 0) return name;
		return String.format("%s.%s", getKey(ids), name);
	}

	private String createKey(){
		Object[] values = new Object[this.values.length];
		for(int i=0; i<values.length; i++){
			values[i] = this.values[i].get();
		}
		return getKey(values);
	}
	
	private String getKey(Object[] ids){
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(int i=0; i<values.length; i++){
			if(i > 0) sb.append(", ");
			sb.append(ids[i]);
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public void valueChanged(CodedReadOnlyBinding<?> binding, Object oldValue, Object newValue) {
		Object[] oldIds = new Object[values.length];
		Object[] newIds = new Object[values.length];
		for(int i=0; i<values.length; i++){
			if(binding == values[i]){
				oldIds[i] = oldValue;
				newIds[i] = newValue;
			}else{
				oldIds[i] = values[i].get();
				newIds[i] = values[i].get();
			}
		}
		String oldKey = getFullKey(oldIds);
		String newKey = getFullKey(newIds);
		if(provider.contains(oldKey)){
			Object value = provider.get(oldKey, null);
			provider.remove(oldKey);
			provider.set(newKey, value);
		}
		key = newKey;
	}
	
}
