package com.github.randomcodeorg.ppplugin.ppdefaults.test.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.ppplugin.ppdefaults.logging.LogLevel;

public class TestLogger {
	
	public static final TestLogger INSTANCE = new TestLogger();
	
	private final Map<LogLevel, List<String>> entries = new HashMap<LogLevel, List<String>>();

	public TestLogger() {
		for(LogLevel ll : LogLevel.values()) entries.put(ll, new ArrayList<String>());
	}
	
	public void warn(String msg){
		entries.get(LogLevel.WARNING).add(msg);
	}
	
	public void error(String msg){
		entries.get(LogLevel.ERROR).add(msg);
	}
	
	public void info(String msg){
		entries.get(LogLevel.INFORMATION).add(msg);
	}
	
	public void debug(String msg){
		entries.get(LogLevel.DEBUG).add(msg);
	}
	
	public void trace(String msg){
		entries.get(LogLevel.VERBOSE).add(msg);
	}
	
	public void clear(LogLevel level){
		entries.get(level).clear();
	}
	
	public void clear(){
		for(LogLevel ll : LogLevel.values()) clear(ll);
	}
	
	public boolean hasEntries(LogLevel level){
		return !entries.get(level).isEmpty();
	}
	
	public boolean hasEntries(){
		for(List<String> lst : entries.values()) if(!lst.isEmpty()) return true;
		return false;
	}
	
	public List<String> get(LogLevel level){
		return entries.get(level);
	}
	
	

}
