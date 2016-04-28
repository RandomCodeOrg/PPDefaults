package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

import java.util.NoSuchElementException;

/**
 * A binding source that is used to retrieve and store values from a persistent
 * store.
 * 
 * @author Marcel Singer
 *
 */
public interface BindingSource extends BindingSourceFactory {

	/**
	 * Returns the value that was stored for the given key. All implementations
	 * are required to return the given default value if an error occurs (e.g.
	 * I/O error, casting error) or if there is no value for the given key.
	 * 
	 * @param key
	 *            The key specifying the value to be retrieved.
	 * @param defaultValue
	 *            The default value that should be returned if there is an
	 *            retrieving the requested value.
	 * @return The value that was stored for the given key.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given key is invalid (e.g. <code>null</code>
	 *             or an empty string). Every implementation can specify its own
	 *             requirements regarding the allowed key values. One may refer
	 *             to the corresponding documentation.
	 */
	public <T> T get(String key, T defaultValue) throws IllegalArgumentException;

	/**
	 * <p>
	 * Stores the given value.
	 * </p>
	 * <p>
	 * <b>Note:</b> The supported value types depend on the specific
	 * implementation. Please refer to the appropriate documentation to learn
	 * more about the types supported by the used implementation. Although every
	 * implementation should at least (by convention) support the primitive
	 * types (e.g. byte, int, long, double, boolean) and strings.
	 * </p>
	 * 
	 * @param key
	 *            The key that can be used to retrieve the stored value in the
	 *            future.
	 * @param value
	 *            The value to be stored.
	 * @throws IllegalArgumentException
	 *             <p>
	 *             Is thrown if the value's type is invalid.
	 *             </p>
	 *             <p>
	 *             <i>or</i>
	 *             </p>
	 *             <p>
	 *             Is thrown if the given key is invalid (e.g. <code>null</code>
	 *             or an empty string). Every implementation can specify its own
	 *             requirements regarding the allowed key values. One may refer
	 *             to the corresponding documentation.
	 *             </p>
	 */
	public <T> void set(String key, T value) throws IllegalArgumentException;

	public boolean contains(String key) throws NoSuchElementException, IllegalArgumentException;

	public boolean remove(String key) throws NoSuchElementException, IllegalArgumentException;

	public void load();

	public void flush();

	public void close();

	public void registerListener(String key, BindingListener listener)
			throws NoSuchElementException, IllegalArgumentException;

	public void releaseListener(String key, BindingListener listener) throws IllegalArgumentException;

	@Override
	BindingSource getBindingSource(String identifier);

}
