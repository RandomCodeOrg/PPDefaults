package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import javassist.CtClass;

public class DefaultLoggingCodeSource implements LoggingCodeSource {

	private static final String DEFAULT_LOGGER_TYPE = "org.slf4j.Logger";
	private static final String DEFAULT_LOGGER_PREFIX = "al_";
	private static final String DEFAULT_LOGGER_INITIALIZATION = "org.slf4j.LoggerFactory.getLogger(%s.class);";

	public DefaultLoggingCodeSource() {

	}

	@Override
	public String getLogMethodName(LogLevel level) {
		return level.getLevelMethodName();
	}

	@Override
	public String getLoggerType() {
		return DEFAULT_LOGGER_TYPE;
	}

	@Override
	public String getLoggerFieldPrefix() {
		return DEFAULT_LOGGER_PREFIX;
	}

	@Override
	public String getLoggerInitialization(CtClass cl, Class<?> runtimeClass) {
		return String.format(DEFAULT_LOGGER_INITIALIZATION, cl.getSimpleName());
	}

}
