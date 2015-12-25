package com.github.randomcodeorg.ppplugin.ppdefaults.access;

public class AlwaysAllowedAccessController implements AccessController{

	public AlwaysAllowedAccessController() {
		
	}

	@Override
	public AccessController redirect(AccessInformation access) {
		return this;
	}

	@Override
	public boolean check(AccessInformation access) {
		return true;
	}

}
