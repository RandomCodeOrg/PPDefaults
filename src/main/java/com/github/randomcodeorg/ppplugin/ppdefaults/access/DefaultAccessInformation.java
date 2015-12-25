package com.github.randomcodeorg.ppplugin.ppdefaults.access;

import java.util.Arrays;

class DefaultAccessInformation implements AccessInformation {

	private final Thread thread;
	private final StackTraceElement[] stackTrace;
	
	private DefaultAccessInformation(Thread thread, StackTraceElement[] stackTrace) {
		this.thread = thread;
		this.stackTrace = stackTrace;
	}
	
	
	public static AccessInformation build(){
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackTrace = thread.getStackTrace();
		stackTrace = Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
		return new DefaultAccessInformation(thread, stackTrace);
	}


	@Override
	public Thread getThread() {
		return thread;
	}


	@Override
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
	
	

}
