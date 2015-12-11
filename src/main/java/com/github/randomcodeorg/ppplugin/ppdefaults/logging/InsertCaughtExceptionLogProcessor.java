package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.ByteCodeHelper;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;

public abstract class InsertCaughtExceptionLogProcessor extends AbstractLoggingProcessor {

	private static final LogLevel DEFAULT_EXCEPTION_LOG_LEVEL = LogLevel.WARNING;

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
				CtField f = injectLogger(helper, ctClass);
				context.getLog()
						.debug(String.format(
								"Using logger that is stored in field '%s' to log caught exception at %s:%d", f.getName(),
								ctBehavior.getLongName(), h.getLineNumber()));
				doInsertCatchLog(runtimeClass, h, ctBehavior, f);
			}

		});
	}

	private void doInsertCatchLog(Class<?> clazz, Handler h, CtBehavior behavior, CtField loggerField)
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
				"{ %s.%s(\"Caught an exception of the type \" +  $1.getClass().getCanonicalName() + \" at %s\\n\\tMessage: \" + $1.getMessage() ); }",
				loggerField.getName(), getLogMethodName(getExceptionLogLevel(clazz)), location);
		h.insertBefore(code);
	}

	protected LogLevel getExceptionLogLevel(Class<?> enclodingClass) {
		return DEFAULT_EXCEPTION_LOG_LEVEL;
	}

}
