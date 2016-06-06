package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

public class BindingHelper {

	public static BindingProvider createProvider(String providerName,
			Class<? extends ProviderFactory> factoryClass) {
		try {
			ProviderFactory pf = factoryClass.newInstance();
			BindingProvider provider = pf.getProvider(providerName);
			return provider;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
