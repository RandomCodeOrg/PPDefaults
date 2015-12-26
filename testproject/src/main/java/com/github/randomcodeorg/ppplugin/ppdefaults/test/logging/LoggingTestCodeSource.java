package com.github.randomcodeorg.ppplugin.ppdefaults.test.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.logging.LogLevel;
import com.github.randomcodeorg.ppplugin.ppdefaults.logging.LoggingCodeSource;

import javassist.CtClass;

public class LoggingTestCodeSource implements LoggingCodeSource{
	
	public LoggingTestCodeSource() {
		
	}

	@Override
	public String getLogMethodName(LogLevel level) {
		return level.getLevelMethodName();
	}

	@Override
	public String getLoggerType() {
		return "com.github.randomcodeorg.ppplugin.ppdefaults.test.logging.TestLogger";
	}

	@Override
	public String getLoggerFieldPrefix() {
		return DEFAULT_LOGGER_PREFIX;
	}

	@Override
	public String getLoggerInitialization(CtClass cl, Class<?> runtimeClass) {
		return "com.github.randomcodeorg.ppplugin.ppdefaults.test.logging.TestLogger.INSTANCE";
	}

}
