package com.github.randomcodeorg.ppplugin.ppdefaults.binding;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBindingSource implements BindingSource {

	private Map<String, Set<WeakReference<BindingListener>>> listeners = new HashMap<String, Set<WeakReference<BindingListener>>>();

	public AbstractBindingSource() {

	}

	@Override
	public final void registerListener(String key, BindingListener listener) throws IllegalArgumentException {
		if (key == null)
			throw new IllegalArgumentException("The key must not be null.");
		if (listener == null)
			throw new IllegalArgumentException("The listener must not be null.");
		if (!listeners.containsKey(key)) {
			listeners.put(key, new HashSet<WeakReference<BindingListener>>());
		}
		Set<WeakReference<BindingListener>> set = listeners.get(key);
		if (set.contains(listener))
			return;
		set.add(new WeakReference<BindingListener>(listener));
	}

	@Override
	public final void releaseListener(String key, BindingListener listener) throws IllegalArgumentException {
		if (key == null)
			throw new IllegalArgumentException("The key must not be null.");
		if (listener == null)
			throw new IllegalArgumentException("The listener must not be null.");
		if (!listeners.containsKey(key))
			return;
		Set<WeakReference<BindingListener>> set = listeners.get(key);
		BindingListener current;
		for (WeakReference<BindingListener> ref : set) {
			current = ref.get();
			if (current == null || current == listener)
				set.remove(ref);
		}
		if (set.size() == 0)
			listeners.remove(key);
	}

	@Override
	public final <T> void set(String key, T value) {
		if (listeners.containsKey(key)) {
			T oldValue = null;
			if (contains(key)){
				oldValue = get(key, null);
			}
			onSet(key, value);
			Set<WeakReference<BindingListener>> set = listeners.get(key);
			BindingListener current;
			for (WeakReference<BindingListener> ref : set) {
				current = ref.get();
				if (current == null) {
					set.remove(current);
					continue;
				}
				current.valueUpdated(this, key, oldValue, value);
			}
			if (set.size() == 0)
				listeners.remove(key);
		} else
			onSet(key, value);
	}

	protected abstract <T> void onSet(String key, T value);

	@Override
	public final boolean remove(String key) {
		if (!contains(key))
			return false;
		if (listeners.containsKey(key)) {
			Set<WeakReference<BindingListener>> set = listeners.get(key);
			BindingListener current;
			for (WeakReference<BindingListener> ref : set) {
				current = ref.get();
				if (current != null)
					current.bindingRemoved(this, key);
			}
			listeners.remove(key);
		}
		onRemove(key);
		return true;
	}

	public abstract void onRemove(String key);
	

}
