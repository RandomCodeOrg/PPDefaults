package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import java.util.prefs.Preferences;

import com.github.randomcodeorg.ppplugin.ppdefaults.binding.AbstractBindingSourceFactory;
import com.github.randomcodeorg.ppplugin.ppdefaults.binding.BindingSource;
import com.github.randomcodeorg.ppplugin.ppdefaults.binding.BindingSourceFactory;
import com.github.randomcodeorg.ppplugin.ppdefaults.binding.PreferencesBindingSource;

public class MyBindingSourceFactory extends AbstractBindingSourceFactory implements BindingSourceFactory {

	public MyBindingSourceFactory() {

	}

	private static final Preferences userPrefs = Preferences.userNodeForPackage(MyBindingSourceFactory.class);

	@Override
	protected BindingSource createBindingSource(String identifier) {
		Preferences prefs = userPrefs.node(identifier);
		BindingSource result = new PreferencesBindingSource(prefs);
		return result;
	}

}
