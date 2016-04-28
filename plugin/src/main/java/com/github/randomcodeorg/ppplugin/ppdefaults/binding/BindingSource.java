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
	 * more about this issue. Although every implementation should at least (by
	 * convention) support the primitive types (e.g. byte, int, long, double,
	 * boolean) and strings.
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

	/**
	 * Returns <code>true</code> if there is a value for the specified key.
	 * 
	 * @param key
	 *            The key to check.
	 * @return <code>true</code> if there is a value for the specified key.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given key is invalid (e.g. <code>null</code>
	 *             or an empty string). Every implementation can specify its own
	 *             requirements regarding the allowed key values. One may refer
	 *             to the corresponding documentation.
	 */
	public boolean contains(String key) throws IllegalArgumentException;

	/**
	 * Removes the key and its associated value.
	 * 
	 * @param key
	 *            The key to be removed.
	 * @return <code>true</code> if the key was present before this method was
	 *         called. <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given key is invalid (e.g. <code>null</code>
	 *             or an empty string). Every implementation can specify its own
	 *             requirements regarding the allowed key values. One may refer
	 *             to the corresponding documentation.
	 */
	public boolean remove(String key) throws NoSuchElementException, IllegalArgumentException;

	/**
	 * Loads this store. All implementations of this interface are required to
	 * tolerate multiple invocations of this method. Only the first call to this
	 * method should actually perform the necessary operations. No action should
	 * be done on subsequent invocations.
	 */
	public void load();

	/**
	 * Flushes all changes by writing them to a persistent medium.
	 */
	public void flush();

	/**
	 * Flushes all changes by writing them to a persistent medium and releases
	 * all associated resources.
	 */
	public void close();

	/**
	 * Registers a listener.
	 * 
	 * @param key
	 *            The key of the key/value pair to listen for.
	 * @param listener
	 *            The listener that should be notified when the specified value
	 *            changes.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given key is invalid (e.g. <code>null</code>
	 *             or an empty string). Every implementation can specify its own
	 *             requirements regarding the allowed key values. One may refer
	 *             to the corresponding documentation.
	 */
	public void registerListener(String key, BindingListener listener) throws IllegalArgumentException;

	/**
	 * Removes the given listener.
	 * 
	 * @param key
	 *            The key of the key/value pair to unregister from.
	 * @param listener
	 *            The listener to unregister.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given key is invalid (e.g. <code>null</code>
	 *             or an empty string). Every implementation can specify its own
	 *             requirements regarding the allowed key values. One may refer
	 *             to the corresponding documentation.
	 */
	public void releaseListener(String key, BindingListener listener) throws IllegalArgumentException;

	/**
	 * Returns this instance.
	 * @param identifier <p style="text-decoration: line-through">The identifier to be returned.</p>
	 * <p><b>This parameter is ignored.</b></p>
	 * @return This method will <b>always</b> (regardless of the given parameter) return this instance.
	 */
	@Override
	BindingSource getBindingSource(String identifier);

}
