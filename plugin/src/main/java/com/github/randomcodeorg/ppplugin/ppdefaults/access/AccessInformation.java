package com.github.randomcodeorg.ppplugin.ppdefaults.access;

public interface AccessInformation {
	
	
	Thread getThread();
	StackTraceElement[] getStackTrace();
	String getExtra();
	boolean hasParameters();
	Object[] getParameters();

}
