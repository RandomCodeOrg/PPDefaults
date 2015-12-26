package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import javassist.CtClass;

/**
 * A implementation of {@link LoggingCodeSource} that will generate calls to the
 * android.util.Log-logger.
 * 
 * @author Marcel Singer
 *
 */
public class AndroidLoggingCodeSource implements LoggingCodeSource {

	private Class<? extends AndroidLogAdapter> adapterClass;

	/**
	 * Creates a new instance of {@link AndroidLoggingCodeSource} using the
	 * given {@link AndroidLogAdapter}.
	 * 
	 * @param adapterClass
	 *            The class (inheriting from {@link AndroidLogAdapter}) that
	 *            will be called at runtime to create the log entries.
	 *            <p><b>Note</b> that
	 *            one must override
	 *            {@link #getLoggerInitialization(CtClass, Class)} if the
	 *            constructors signature of the given class differs from the one
	 *            of the {@link AndroidLogAdapter}.</p>
	 */
	public AndroidLoggingCodeSource(Class<? extends AndroidLogAdapter> adapterClass) {
		this.adapterClass = adapterClass;
	}

	/**
	 * Creates a new instance of {@link AndroidLoggingCodeSource} that uses the
	 * {@link AndroidLogAdapter} that will be called at runtime to create the
	 * log entries.
	 */
	public AndroidLoggingCodeSource() {
		this(AndroidLogAdapter.class);
	}

	@Override
	public String getLogMethodName(LogLevel level) {
		switch (level) {
		case DEBUG:
			return "d";
		case VERBOSE:
			return "v";
		case ERROR:
			return "e";
		case INFORMATION:
			return "i";
		case WARNING:
			return "w";
		}
		return "i";
	}

	@Override
	public String getLoggerType() {
		return adapterClass.getCanonicalName();
	}

	@Override
	public String getLoggerFieldPrefix() {
		return DEFAULT_LOGGER_PREFIX;
	}

	@Override
	public String getLoggerInitialization(CtClass cl, Class<?> runtimeClass) {
		return String.format("new %s(\"%s\")", adapterClass.getCanonicalName(), AndroidLogAdapter.getTag(runtimeClass));
	}

}
