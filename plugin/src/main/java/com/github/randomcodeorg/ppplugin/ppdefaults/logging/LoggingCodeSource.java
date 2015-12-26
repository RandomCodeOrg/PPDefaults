package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import javassist.CtClass;

public interface LoggingCodeSource {

	public static final String DEFAULT_LOGGER_PREFIX = "al_";

	/**
	 * Returns the name of the loggers log method.
	 * 
	 * @param level
	 *            The log level.
	 * @return the name of the loggers log method (e.g. &quot;info&quot;,
	 *         &quot;warn&quot;).
	 */
	String getLogMethodName(LogLevel level);

	/**
	 * <p>
	 * Returns the canonical name of the loggers class.
	 * </p>
	 * <p>
	 * <b>Note:</b> The class of the logger must be available during the
	 * compilation (e.g. included as a maven dependency).
	 * </p>
	 * 
	 * @return The canonical name of the loggers class (e.g.
	 *         &quot;org.slf4j.Logger&quot;).
	 */
	String getLoggerType();

	/**
	 * Returns the prefix of the field that will be created to hold the logger.
	 * 
	 * @return The prefix of the field that will be created to hold the logger.
	 */
	String getLoggerFieldPrefix();

	/**
	 * Returns the initial value of the logger.
	 * 
	 * @param cl
	 *            The class in which the logger will be used.
	 * @param runtimeClass The {@link Class} representation of the class in which the logger will be used.
	 * @return The initial value of the logger (e.g. &quot;new Logger()&quot;,
	 *         &quot;org.slf4j.LoggerFactory.getLogger(Object.class)&quot;).
	 */
	String getLoggerInitialization(CtClass cl, Class<?> runtimeClass);

}
