package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.AbstractClassModificationProcessor;
import com.github.randomcodeorg.ppplugin.ppdefaults.ByteCodeHelper;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

/**
 * A processor that can be inherited to insert logging calls. One can/should
 * override the methods of this processor to modify/customize the way the logger
 * is called.
 * 
 * @author Marcel Singer
 *
 */
public abstract class AbstractLoggingProcessor extends AbstractClassModificationProcessor {

	private LoggingCodeSource codeSource = new DefaultLoggingCodeSource();

	/**
	 * Creates a new instance of {@link AbstractLoggingProcessor}.
	 */
	public AbstractLoggingProcessor() {

	}

	/**
	 * Sets the {@link LoggingCodeSource} that will be used to create the necessary lines of code.
	 * <p><b>Note:</b> It is not longer recommended to override the methods of this class directly. Instead one might use this method to customized the logging process.</p>
	 * @param source The {@link LoggingCodeSource} that will be used to create the necessary lines of code.
	 */
	public void setLoggingCodeSource(LoggingCodeSource source) {
		this.codeSource = source;
	}

	/**
	 * Returns the name of the loggers log method.
	 * @param level
	 *            The log level.
	 * @return the name of the loggers log method (e.g. &quot;info&quot;,
	 *         &quot;warn&quot;).
	 */
	protected String getLogMethodName(LogLevel level) {
		return codeSource.getLogMethodName(level);
	}

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
	protected String getLoggerType() {
		return codeSource.getLoggerType();
	}

	/**
	 * Returns the prefix of the field that will be created to hold the logger.
	 * 
	 * @return The prefix of the field that will be created to hold the logger.
	 */
	protected String getLoggerFieldPrefix() {
		return codeSource.getLoggerFieldPrefix();
	}

	/**
	 * Returns the initial value of the logger.
	 * 
	 * @param cl
	 *            The class in which the logger will be used.
	 * @param runtimeClass
	 *            The java.lang.Class<?> representation of the class in which
	 *            the logger will be used.
	 * @return The initial value of the logger (e.g. &quot;new Logger()&quot;,
	 *         &quot;org.slf4j.LoggerFactory.getLogger(Object.class)&quot;).
	 */
	protected String getLoggerInitialization(CtClass cl, Class<?> runtimeClass) {
		return codeSource.getLoggerInitialization(cl, runtimeClass);
	}

	/**
	 * Creates the field that holds the logger for the given class.
	 * 
	 * @param helper
	 *            The helper to be used.
	 * @param ctClass
	 *            The class in which the logger will be used.
	 * @param runtimeClass
	 *            The java.lang.Class<?> representation of the class in which
	 * @return The field that holds the logger for the given class.
	 * @throws CannotCompileException
	 *             If the changes could not be compiled.
	 */
	protected CtField injectLogger(ByteCodeHelper helper, CtClass ctClass, Class<?> runtimeClass) throws CannotCompileException {
		return helper.getOrCreateField(ctClass, getLoggerType(), getLoggerFieldPrefix(), "private", true, true,
				getLoggerInitialization(ctClass, runtimeClass));
	}

}
