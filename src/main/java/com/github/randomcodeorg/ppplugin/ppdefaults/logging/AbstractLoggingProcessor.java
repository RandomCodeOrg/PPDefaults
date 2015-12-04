package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.AbstractClassModificationProcessor;

import javassist.CtClass;

public abstract class AbstractLoggingProcessor extends AbstractClassModificationProcessor {

	private static final String DEFAULT_LOGGER_TYPE = "org.slf4j.Logger";
	private static final String DEFAULT_LOGGER_PREFIX = "al_";
	private static final String DEFAULT_LOGGER_INITIALIZATION = "org.slf4j.LoggerFactory.getLogger(%s.class);";

	public AbstractLoggingProcessor() {

	}

	protected String getLogMethodName(LogLevel level) {
		return level.getLevelMethodName();
	}

	protected String getLoggerType() {
		return DEFAULT_LOGGER_TYPE;
	}

	protected String getLoggerFieldPrefix() {
		return DEFAULT_LOGGER_PREFIX;
	}

	protected String getLoggerInitialization(CtClass cl) {
		return String.format(DEFAULT_LOGGER_INITIALIZATION, cl.getSimpleName());
	}

}
