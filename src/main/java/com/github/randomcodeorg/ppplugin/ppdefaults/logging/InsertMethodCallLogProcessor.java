package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import com.github.randomcodeorg.ppplugin.ppdefaults.ByteCodeHelper;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

public class InsertMethodCallLogProcessor extends AbstractLoggingProcessor {

	public InsertMethodCallLogProcessor() {
	}

	@Override
	protected void processClass(ByteCodeHelper helper, CtClass ctClass, Class<?> clazz) throws CannotCompileException {
		context.getLog().debug(String.format("Processing class %s...", clazz.getCanonicalName()));
		boolean classLevelLog = clazz.isAnnotationPresent(LogThis.class);
		LogThis annotation = null;
		if (classLevelLog)
			annotation = clazz.getAnnotation(LogThis.class);
		for (Method m : clazz.getDeclaredMethods()) {
			if(m.isAnnotationPresent(Stealth.class)) continue;
			if (m.isAnnotationPresent(LogThis.class)) {
				processMethod(helper, ctClass, clazz, ByteCodeHelper.findMethod(ctClass, m), m,
						m.getAnnotation(LogThis.class));
			} else if (classLevelLog) {
				processMethod(helper, ctClass, clazz, ByteCodeHelper.findMethod(ctClass, m), m, annotation);
			}
		}
	}

	protected void processMethod(ByteCodeHelper helper, CtClass ctClass, Class<?> clazz, CtMethod m, Method runtimeMethod,
			LogThis annotation) throws CannotCompileException {
		if (m == null)
			return;
		if (helper.edit(ctClass, clazz)) context.getLog().info(String.format("Inserting log calls into %s", m.getLongName()));
		CtField loggerField = injectLogger(helper, ctClass, clazz);
		context.getLog().debug(String.format("Using logger that is stored in field '%s' to log method calls of %s",
				loggerField.getName(), m.getLongName()));

		String loggerValue = getLoggerMessageString(m, runtimeMethod, annotation,
				getMethodVariableNames(m, runtimeMethod), loggerField);

		String toInsert = String.format("%s.%s(%s);", loggerField.getName(), getLogMethodName(annotation.value()), loggerValue);
		context.getLog().debug("Inserting: " + toInsert);
		m.insertBefore(toInsert);
	}

	protected String getLoggerMessageString(CtMethod m, Method runtimeMethod, LogThis annotation,
			String[] parameterNames, CtField loggerField) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"Called ");
		sb.append(m.getLongName());
		if (parameterNames.length > 0) {
			sb.append(" with:");
			for (int i = 0; i < parameterNames.length; i++) {
				String param = parameterNames[i];
				if (param != null) {
					sb.append(String.format("\\n\\t%s: ", param));
					sb.append(String.format("\" + $%d + \"", (i + 1)));
				} else {
					sb.append(String.format("\\n\\targ%d: ", i));
					sb.append("@Stealth");
				}
			}
		}
		if (annotation.logFields()) {
			Field[] fields = runtimeMethod.getDeclaringClass().getDeclaredFields();
			if (fields.length > 0) {
				if (parameterNames.length == 0)
					sb.append(" with:");
				else
					sb.append("\\n");
				for (Field f : fields) {
					if (f.isAnnotationPresent(Stealth.class))
						continue;
					if (annotation.ignoreStaticFinal()) {
						if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
							continue;
					}
					sb.append(String.format("\\n\\t%s [field]: ", f.getName()));
					sb.append(String.format("\" + %s + \"", f.getName()));
				}
			}
		}
		sb.append("\"");
		return sb.toString();
	}

	protected String[] getMethodVariableNames(CtMethod cm, Method method) {
		Parameter[] params = method.getParameters();
		String[] result = new String[params.length];
		Parameter p;
		for (int i = 0; i < params.length; i++) {
			p = params[i];
			if (p.isAnnotationPresent(Stealth.class))
				continue;
			result[i] = String.format("arg%d", i);
		}
		return result;
	}

}
