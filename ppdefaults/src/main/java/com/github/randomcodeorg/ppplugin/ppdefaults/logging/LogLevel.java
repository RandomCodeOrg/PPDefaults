package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

public enum LogLevel {
	
	
	WARNING("warn"),
	ERROR("error"),
	INFORMATION("info"),
	DEBUG("debug"),
	VERBOSE("trace");
	
	
	private final String level;
	
	private LogLevel(String level){
		this.level = level;
	}
	
	public String getLevelMethodName(){
		return level;
	}

}
