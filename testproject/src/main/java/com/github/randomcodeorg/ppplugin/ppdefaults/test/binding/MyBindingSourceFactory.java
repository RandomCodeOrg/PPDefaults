package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import java.util.HashMap;
import java.util.prefs.Preferences;

import com.github.randomcodeorg.ppplugin.ppdefaults.binding.BindingSource;
import com.github.randomcodeorg.ppplugin.ppdefaults.binding.BindingSourceFactory;
import com.github.randomcodeorg.ppplugin.ppdefaults.binding.PreferencesBindingSource;

public class MyBindingSourceFactory implements BindingSourceFactory {

	public MyBindingSourceFactory() {

	}

	@Override
	public BindingSource getBindingSource(String identifier) {
		return getSource(identifier);
	}

	private static final HashMap<String, BindingSource> sources = new HashMap<String, BindingSource>();
	private static final Preferences userPrefs = Preferences.userNodeForPackage(MyBindingSourceFactory.class);

	private static synchronized BindingSource getSource(String identifier) {
		if (sources.containsKey(identifier))
			return sources.get(identifier);
		Preferences prefs = userPrefs.node(identifier);
		BindingSource result = new PreferencesBindingSource(prefs);
		sources.put(identifier, result);
		return result;
	}

}
