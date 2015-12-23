package com.github.randomcodeorg.ppplugin.ppdefaults.logging;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * An adapter to call the methods of android.util.Log. This implementation might
 * be a little slow because it uses reflection to call the corresponding
 * methods. You can create and use a inheriting class that calls the required
 * methods directly. See the documentation of
 * <ul>
 * <li>{@link #v(String)}</li>
 * <li>{@link #d(String)}</li>
 * <li>{@link #i(String)}</li>
 * <li>{@link #w(String)}</li>
 * <li>{@link #e(String)}</li>
 * <li>{@link #getTag()}</li>
 * </ul>
 * to do so.
 * 
 * @author Marcel Singer
 */
public class AndroidLogAdapter {

	private final String tagName;
	private final Map<String, Method> methodMap = new HashMap<String, Method>();
	private boolean isInitialized = false;

	private static final String LOGGER_CLASS_NAME = "android.util.Log";

	public AndroidLogAdapter(String tagName) {
		this.tagName = tagName;
	}

	private void init() {
		if (isInitialized)
			return;
		Class<?> logger;
		try {
			logger = Class.forName(LOGGER_CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		findLogMethod(logger, "d");
		findLogMethod(logger, "e");
		findLogMethod(logger, "i");
		findLogMethod(logger, "v");
		findLogMethod(logger, "w");
	}

	private void findLogMethod(Class<?> logger, String name) {
		try {
			Method m = logger.getMethod(name, String.class, String.class);
			methodMap.put(name, m);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getTag(Class<?> runtimeClass) {
		try {
			Field f = runtimeClass.getDeclaredField("TAG");
			if (Modifier.isStatic(f.getModifiers()) && String.class.isAssignableFrom(f.getType())) {
				return (String) f.get(null);
			}
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		String tmp = runtimeClass.getSimpleName();
		if (tmp.length() > 23) {
			tmp = tmp.substring(0, 23);
		}
		return tmp;
	}

	private void callAndroidLogger(String methodName, String message) {
		if (!isInitialized)
			init();
		if (!methodMap.containsKey(methodName))
			throw new IllegalArgumentException();
		Method m = methodMap.get(methodName);
		try {
			m.invoke(null, tagName, message);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the tag to be used. One can use the result of this method to call
	 * the android.util.Log directly within an inheriting class.
	 * 
	 * @return The tag to be used.
	 */
	protected String getTag() {
		return tagName;
	}

	/**
	 * Creates a debug-level log entry. One can override this method to call the
	 * android.util.Log directly. You can get the required tag by calling
	 * {@link #getTag()}.
	 * 
	 * @param message
	 *            The message to be logged.
	 */
	public void d(String message) {
		callAndroidLogger("d", message);
	}

	/**
	 * Creates an error-level log entry. One can override this method to call
	 * the android.util.Log directly. You can get the required tag by calling
	 * {@link #getTag()}.
	 * 
	 * @param message
	 *            The message to be logged.
	 */
	public void e(String message) {
		callAndroidLogger("e", message);
	}

	/**
	 * Creates an information-level log entry. One can override this method to
	 * call the android.util.Log directly. You can get the required tag by
	 * calling {@link #getTag()}.
	 * 
	 * @param message
	 *            The message to be logged.
	 */
	public void i(String message) {
		callAndroidLogger("i", message);
	}

	/**
	 * Creates a verbose-level log entry. One can override this method to call
	 * the android.util.Log directly. You can get the required tag by calling
	 * {@link #getTag()}.
	 * 
	 * @param message
	 *            The message to be logged.
	 */
	public void v(String message) {
		callAndroidLogger("v", message);
	}

	/**
	 * Creates a warning-level log entry. One can override this method to call
	 * the android.util.Log directly. You can get the required tag by calling
	 * {@link #getTag()}.
	 * 
	 * @param message
	 *            The message to be logged.
	 */
	public void w(String message) {
		callAndroidLogger("w", message);
	}

}
