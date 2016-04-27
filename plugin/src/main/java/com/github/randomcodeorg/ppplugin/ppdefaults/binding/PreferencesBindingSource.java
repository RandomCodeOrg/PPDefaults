package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

import java.util.NoSuchElementException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesBindingSource extends AbstractBindingSource {

	private final Preferences prefs;

	public PreferencesBindingSource(Preferences prefs) {
		this.prefs = prefs;
	}

	private static final int NULL_TYPE = -1;
	private static final int STRING_TYPE = 0;
	private static final int LONG_TYPE = 1;
	private static final int BOOL_TYPE = 2;
	private static final int BYTE_ARRAY_TYPE = 3;
	private static final int DOUBLE_TYPE = 4;
	private static final int FLOAT_TYPE = 5;
	private static final int INT_TYPE = 6;
	private static final int NOT_AVAILABLE = -2;

	public PreferencesBindingSource(Class<?> cl, boolean useSystemPrefs) {
		if (useSystemPrefs) {
			prefs = Preferences.systemNodeForPackage(cl);
		} else {
			prefs = Preferences.userNodeForPackage(cl);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, T defaultValue) throws NoSuchElementException, IllegalArgumentException {
		String typeKey = String.format("#%s", key);
		int typeValue = prefs.getInt(typeKey, NOT_AVAILABLE);
		try {
			switch (typeValue) {
			case NOT_AVAILABLE:
				return defaultValue;
			case NULL_TYPE:
				return null;
			case STRING_TYPE:
				return (T) prefs.get(key, (String) defaultValue);
			case LONG_TYPE:
				return (T) (Long) prefs.getLong(key, (Long) defaultValue);
			case BOOL_TYPE:
				return (T) (Boolean) prefs.getBoolean(key, (Boolean) defaultValue);
			case BYTE_ARRAY_TYPE:
				return (T) prefs.getByteArray(key, (byte[]) defaultValue);
			case DOUBLE_TYPE:
				return (T) (Double) prefs.getDouble(key, (Double) defaultValue);
			case FLOAT_TYPE:
				return (T) (Float) prefs.getFloat(key, (Float) defaultValue);
			case INT_TYPE:
				return (T) (Integer) prefs.getInt(key, (Integer) defaultValue);
			}
		} catch (ClassCastException e) {
			return defaultValue;
		}

		return defaultValue;
	}

	public void clear() {
		try {
			String[] keys = prefs.keys();
			for (String k : keys) {
				if (k != null && k.startsWith("#")) {
					remove(k.substring(1));
				}
			}
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected <T> void onSet(String key, T value) {
		int typeValue = NULL_TYPE;
		if (contains(key))
			onRemove(key);
		if (value == null) {

		} else if (int.class.isAssignableFrom(value.getClass()) || value instanceof Integer) {
			typeValue = INT_TYPE;
			prefs.putInt(key, (Integer) value);
		} else if (float.class.isAssignableFrom(value.getClass()) || value instanceof Float) {
			typeValue = FLOAT_TYPE;
			prefs.putFloat(key, (Float) value);
		} else if (double.class.isAssignableFrom(value.getClass()) || value instanceof Double) {
			typeValue = DOUBLE_TYPE;
			prefs.putDouble(key, (Double) value);
		} else if (value instanceof byte[]) {
			typeValue = BYTE_ARRAY_TYPE;
			prefs.putByteArray(key, (byte[]) value);
		} else if (boolean.class.isAssignableFrom(value.getClass()) || value instanceof Boolean) {
			typeValue = BOOL_TYPE;
			prefs.putBoolean(key, (Boolean) value);
		} else if (long.class.isAssignableFrom(value.getClass()) || value instanceof Long) {
			typeValue = LONG_TYPE;
			prefs.putLong(key, (Long) value);
		} else if (value instanceof String) {
			typeValue = STRING_TYPE;
			prefs.put(key, (String) value);
		} else {
			throw new IllegalArgumentException(
					String.format("The type ('%s') of the given value is not supported by this binding source.",
							value.getClass().getSimpleName()));
		}
		String typeKey = String.format("#%s", key);
		prefs.putInt(typeKey, typeValue);
	}

	@Override
	public void onRemove(String key) {
		prefs.remove(key);
		prefs.remove(String.format("#%s", key));
	}

	@Override
	public boolean contains(String key) throws NoSuchElementException, IllegalArgumentException {
		String typeKey = String.format("#%s", key);
		int typeValue = prefs.getInt(typeKey, NOT_AVAILABLE);
		return typeValue != NOT_AVAILABLE;
	}

	@Override
	public void load() {
		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void flush() {
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		flush();
	}

	@Override
	public BindingSource getBindingSource(String identifier) {
		return this;
	}

}
