package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * If a method is annotated with this annotation a call to it, will be logged.
 * An annotation on a class applies to all its methods. A directly annotated method
 * will override an existing class-level annotation.</p>
 * <p>
 * See also: {@link Stealth}
 * </p>
 * @author Marcel Singer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogThis{

	/**
	 * Returns the log level to be used (default is {@link LogLevel#DEBUG}).
	 * @return The log level to be used.
	 */
	LogLevel value() default LogLevel.DEBUG;
	/**
	 * Returns {@code true} if the values of the instances fields should be included in the log entry (default is {@code true}). 
	 * @return {@code true} if the values of the instances fields should be included in the log entry. 
	 */
	boolean logFields() default true;
	/**
	 * Returns {@code true} if static and final fields should be excluded from the log entry (default is {@code true}.
	 * @return {@code true} if static and final fields should be excluded from the log entry.
	 */
	boolean ignoreStaticFinal() default true;
	
}
