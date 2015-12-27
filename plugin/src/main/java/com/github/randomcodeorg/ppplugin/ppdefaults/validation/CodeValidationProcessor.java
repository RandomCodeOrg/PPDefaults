package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.github.randomcodeorg.ppplugin.ppdefaults.AbstractClassModificationProcessor;
import com.github.randomcodeorg.ppplugin.ppdefaults.ByteCodeHelper;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.Modifier;
import javassist.NotFoundException;

public class CodeValidationProcessor extends AbstractClassModificationProcessor {

	private final HashMap<Class<?>, Set<Class<? extends Validator<?>>>> validationAnnotationTypes = new HashMap<Class<?>, Set<Class<? extends Validator<?>>>>();

	public CodeValidationProcessor() {
		
	}

	@Override
	protected void doRun() throws NotFoundException {
		context.getLog().info("Searching for validation annotations...");
		searchValidationAnnotations();
		context.getLog().info("Adding default annotation validations");
		addDefaultValidationAnnotations();
		super.doRun();
	}

	@Override
	protected void processClass(ByteCodeHelper helper, CtClass ctClass, Class<?> clazz) throws CannotCompileException {
		if (clazz.isInterface())
			return;
		boolean changed = false;
		for (CtBehavior b : ctClass.getDeclaredBehaviors()) {
			if (Modifier.isAbstract(b.getModifiers()))
				continue;
			Object[][] allAnnotations = b.getAvailableParameterAnnotations();
			Object[] annotations;
			for (int i = 0; i < allAnnotations.length; i++) {
				annotations = allAnnotations[i];
				Set<Class<? extends Validator<?>>> validators = getValidators(annotations);
				if (validators.isEmpty()) {
					continue;
				}

				if (!changed) {
					context.getLog().info(String.format("Inserting validations into %s", clazz.getCanonicalName()));
					changed = true;
				}
				doInsertValidation(helper, ctClass, clazz, b, validators, i);
			}
		}
		if (changed)
			helper.edit(ctClass, clazz);
	}

	protected void doInsertValidation(ByteCodeHelper helper, CtClass ctClass, Class<?> clazz, CtBehavior behavior,
			Set<Class<? extends Validator<?>>> validators, int parameterIndex) throws CannotCompileException {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Class<? extends Validator<?>> validator : validators) {
			if (first)
				first = false;
			else
				sb.append("\n");
			int i = parameterIndex;
			if (!Modifier.isStatic(behavior.getModifiers()))
				i++;
			sb.append(String.format("%s.invoke($%d, new %s(), new %s(), Thread.currentThread().getStackTrace());", ValidationInvoker.class.getCanonicalName(), i, validator.getCanonicalName(), DefaultValidationInformation.class.getCanonicalName()));
			
		}
		context.getLog().debug("Inserting: " + sb);
		doInsert(behavior, sb.toString());
	}

	private void doInsert(CtBehavior b, String src) throws CannotCompileException {
		if (b instanceof CtConstructor && !((CtConstructor) b).isClassInitializer()) {
			((CtConstructor) b).insertBeforeBody(src);
		} else {
			b.insertBefore(src);
		}
	}

	protected Set<Class<? extends Validator<?>>> getValidators(Object[] annotations) {
		Set<Class<? extends Validator<?>>> result = new HashSet<Class<? extends Validator<?>>>();
		Class<?> annotType;
		for (Object o : annotations) {
			if (o == null)
				continue;
			annotType = ((Annotation) o).annotationType();
			for (Class<?> key : validationAnnotationTypes.keySet()) {
				if (annotType.getCanonicalName().equals(key.getCanonicalName()))
					result.addAll(validationAnnotationTypes.get(key));
			}
		}
		return result;
	}

	protected void addDefaultValidationAnnotations() {
		addAnnotationTypes(NotNegative.class, NotNull.class);
	}

	protected void searchValidationAnnotations() {
		for (Class<?> c : context.getClasses()) {
			if (isValidationAnnotation(c)) {
				context.getLog().debug(String.format("Found annotation: %s", c.getName()));
				addAnnotationType(c);
			}
		}
	}

	protected void addAnnotationTypes(Class<?>... types) {
		for (Class<?> c : types)
			addAnnotationType(c);
	}

	protected void addAnnotationType(Class<?> type) {
		if (validationAnnotationTypes.containsKey(type))
			return;
		checkValidationAnnotation(type);
		if (type.isAnnotationPresent(Validation.class)) {
			doAddAnnotationType(type, type.getAnnotation(Validation.class));
		} else {

		}
	}

	protected boolean isValidationAnnotation(Class<?> type) {
		try {
			checkValidationAnnotation(type);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private void checkValidationAnnotation(Class<?> type) {
		if (type == null)
			throw new IllegalArgumentException("The annotation type must not be null.");
		if (!type.isAnnotation())
			throw new IllegalArgumentException("The given class does not represent an annotation.");
		if (type.isAnnotationPresent(Validation.class) || type.isAnnotationPresent(Validations.class))
			return;
		throw new IllegalArgumentException("The given annotation type is not annotated with @Validation.");
	}

	private void doAddAnnotationType(Class<?> annotationType, Validation validationTypeInfo) {
		if (!validationAnnotationTypes.containsKey(annotationType))
			validationAnnotationTypes.put(annotationType, new HashSet<Class<? extends Validator<?>>>());
		Set<Class<? extends Validator<?>>> validatorClasses = validationAnnotationTypes.get(annotationType);
		Class<? extends Validator<?>> validatorClass = validationTypeInfo.value();
		if (!validatorClasses.contains(validatorClass))
			validatorClasses.add(validatorClass);
	}

}
