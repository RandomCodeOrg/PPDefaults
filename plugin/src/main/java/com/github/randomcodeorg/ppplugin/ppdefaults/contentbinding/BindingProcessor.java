package com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;

import com.github.randomcodeorg.ppplugin.ppdefaults.AbstractClassModificationProcessor;
import com.github.randomcodeorg.ppplugin.ppdefaults.ByteCodeHelper;

public class BindingProcessor extends AbstractClassModificationProcessor {

	@Override
	protected void processClass(ByteCodeHelper helper, CtClass ctClass,
			Class<?> clazz) throws CannotCompileException {
		String providerName = "";
		Class<? extends ProviderFactory> providerFactory = MemoryBindingProviderFactory.class;
		Map<Field, Bind> fieldMap = null;
		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(Bind.class)) {
				if (fieldMap == null)
					fieldMap = new HashMap<Field, Bind>();
				fieldMap.put(f, f.getAnnotation(Bind.class));
			}
		}
		if (clazz.isAnnotationPresent(Binding.class)) {
			Binding binding = clazz.getAnnotation(Binding.class);
			providerName = binding.provider();
			providerFactory = binding.providerFactory();
		}

		if (fieldMap != null) {
			createProvider(clazz, providerName, providerFactory, helper);
			helper.commit();
		}
	}

	private BindingProvider createProvider(Class<?> target,
			String providerName, Class<? extends ProviderFactory> factoryClass,
			ByteCodeHelper helper) {
		context.getLog().info(
				String.format("Processing bindings on %s", target));
		String message;
		if (factoryClass == null) {
			context.getLog()
					.error(String
							.format("A factory class is required for the Binding annotation on '%s'",
									target));
			throw new BindingConfigurationException(
					String.format(
							"A factory class is required for the Binding annotation on '%s'",
							target));
		}
		if (providerName == null)
			providerName = "";
		if (factoryClass.isInterface()) {
			message = String
					.format("The factory given for the binding on '%s' must not be an interface. Value is: '%s'",
							target, factoryClass);
			context.getLog().error(message);
			throw new BindingConfigurationException(message);
		}
		int modifiers = factoryClass.getModifiers();
		if (Modifier.isAbstract(modifiers)) {
			message = String
					.format("The factory given for the binding on '%s' must not be an abstract class. Value is: '%s'",
							target, factoryClass);
			context.getLog().error(message);
			throw new BindingConfigurationException(message);
		}
		try {
			factoryClass.getConstructor();
		} catch (NoSuchMethodException e) {
			message = String
					.format("The factory given for the binding on '%s' must declare a public default constructor. Value is: '%s'",
							target, factoryClass);
			context.getLog().error(message);
			throw new BindingConfigurationException(message, e);
		}
		//helper.getOrCreateField("", typeName, fieldPrefix, visibility, isStatic, isFinal, initValue)
		return null;
	}

}
