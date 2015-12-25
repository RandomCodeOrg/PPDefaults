package com.github.randomcodeorg.ppplugin.ppdefaults.access;

import java.util.List;

public @interface Restricted {

	public Class<? extends List<String>> value();
	
}
