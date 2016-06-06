package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

import java.util.HashMap;
import java.util.Map;

public class MemoryBindingProviderFactory implements ProviderFactory {

	private static final Map<String, BindingProvider> providers = new HashMap<String, BindingProvider>();
	
	@Override
	public BindingProvider getProvider(String providerName) {
		return getOrCreateProvider(providerName);
	}
	
	protected static synchronized BindingProvider getOrCreateProvider(String providerName){
		if(providers.containsKey(providerName)) return providers.get(providerName);
		MemoryBindingProvider prov = new MemoryBindingProvider();
		providers.put(providerName, prov);
		return prov;
	}

}
