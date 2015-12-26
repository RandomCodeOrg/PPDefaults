package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.ByteCodeHelper;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;

/**
 * A processor that inserts log calls for every caught exception. To enable this processor create
 * an inheriting class within your project.
 * @author Marcel Singer
 *
 */
public abstract class InsertCaughtExceptionLogProcessor extends AbstractLoggingProcessor {

	private static final LogLevel DEFAULT_EXCEPTION_LOG_LEVEL = LogLevel.WARNING;

	@Override
	protected void processClass(final ByteCodeHelper helper, CtClass ctClass, final Class<?> runtimeClass)
			throws CannotCompileException {
		ctClass.instrument(new ExprEditor() {

			@Override
			public void edit(Handler h) throws CannotCompileException {
				if (h.isFinally())
					return;
				CtClass ctClass = h.getEnclosingClass();
				if (helper.edit(ctClass, runtimeClass))
					context.getLog().info(
							String.format("Inserting a log call for exceptions caught in '%s'", ctClass.getName()));
				CtBehavior ctBehavior = h.where();
				CtField f = injectLogger(helper, ctClass, runtimeClass);
				context.getLog()
						.debug(String.format(
								"Using logger that is stored in field '%s' to log caught exception at %s:%d", f.getName(),
								ctBehavior.getLongName(), h.getLineNumber()));
				doInsertCatchLog(runtimeClass, h, ctBehavior, f);
			}

		});
	}

	/**
	 * Inserts the log call into the given catch-block.
	 * @param clazz The class containing the given catch-block.
	 * @param h The handler representing the catch-block.
	 * @param behavior The method or constructor containing the catch-block.
	 * @param loggerField The field containing the logger instance.
	 * @throws CannotCompileException If the changes can not be compiled.
	 */
	protected void doInsertCatchLog(Class<?> clazz, Handler h, CtBehavior behavior, CtField loggerField)
			throws CannotCompileException {
		String location;
		if (behavior == null) {
			location = String.format("%s:%d", h.getEnclosingClass().getSimpleName(), h.getLineNumber());
		} else {
			location = String.format("%s:%d", behavior.getLongName(), h.getLineNumber());
		}
		context.getLog().info(String.format("Inserting catch log for %s", location));
		String code;
		code = String.format(
				"{ %s.%s(\"Caught an \" +  $1.getClass().getCanonicalName() + \" at %s\\n\\tMessage: \" + $1.getMessage() ); }",
				loggerField.getName(), getLogMethodName(getExceptionLogLevel(clazz)), location);
		h.insertBefore(code);
	}

	/**
	 * Returns the {@link LogLevel} for caught exceptions (default is {@link LogLevel#WARNING}).
	 * @param enclosingClass The class in which the exception was caught. 
	 * @return The {@link LogLevel} for caught exceptions.
	 */
	protected LogLevel getExceptionLogLevel(Class<?> enclosingClass) {
		return DEFAULT_EXCEPTION_LOG_LEVEL;
	}

}
