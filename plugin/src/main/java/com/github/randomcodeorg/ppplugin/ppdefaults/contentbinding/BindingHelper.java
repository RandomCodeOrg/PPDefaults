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
	
	public static <T> CodedReadOnlyBinding<T> createReadOnly(BindingProvider provider, String name, T defaultValue, CodedReadOnlyBinding<?>... keys){
		return new CodedReadOnlyBinding<T>(createCallback(provider, name, defaultValue, keys));
	}
	
	private static <T> BindingCallback<T> createCallback(BindingProvider provider, String name, T defaultValue, CodedReadOnlyBinding<?>... keys){
		DefaultKeyComposer composer = new DefaultKeyComposer(provider, name, keys);
		return new DefaultBindingCallback<T>(provider, defaultValue, composer);
	}
	

}
