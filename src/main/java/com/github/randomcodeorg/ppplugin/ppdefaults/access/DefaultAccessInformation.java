package com.github.randomcodeorg.ppplugin.ppdefaults.access;

import java.util.Arrays;

class DefaultAccessInformation implements AccessInformation {

	private final Thread thread;
	private final StackTraceElement[] stackTrace;
	private final String extra;
	
	private DefaultAccessInformation(Thread thread, StackTraceElement[] stackTrace, String extra) {
		this.thread = thread;
		this.stackTrace = stackTrace;
		this.extra = extra;
	}
	
	
	public static AccessInformation build(String extra){
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackTrace = thread.getStackTrace();
		stackTrace = Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
		return new DefaultAccessInformation(thread, stackTrace, extra);
	}


	@Override
	public Thread getThread() {
		return thread;
	}


	@Override
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}


	@Override
	public String getExtra() {
		return extra;
	}


	@Override
	public boolean hasParameters() {
		return false;
	}


	@Override
	public Object[] getParameters() {
		return null;
	}
	
	

}
