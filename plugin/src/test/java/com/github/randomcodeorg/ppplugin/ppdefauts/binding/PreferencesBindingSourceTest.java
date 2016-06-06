package com.github.randomcodeorg.ppplugin.ppdefauts.binding;

import static org.junit.Assert.*;


import org.junit.Test;

import com.github.randomcodeorg.ppplugin.ppdefaults.binding.BindingSource;
import com.github.randomcodeorg.ppplugin.ppdefaults.binding.PreferencesBindingSource;
import com.github.randomcodeorg.ppplugin.ppdefaults.testing.BaseTest;

public class PreferencesBindingSourceTest extends BaseTest {

	@Test
	public void testFactoryReturnsInstance() {
		BindingSource source = getCleanSource();
		assertSame(source, source.getBindingSource(null));
		assertSame(source, source.getBindingSource(randomString()));
		assertSame(source, source.getBindingSource(""));
	}
	
	@Test
	public void testStore(){
		BindingSource source = getCleanSource();
		String key = randomString(ALPHA_NUMERIC);
		String value = randomString();
		assertFalse(source.contains(key));
		source.set(key, value);
		assertTrue(source.contains(key));
		assertEquals(value, source.get(key, null));
	}
	
	@Test
	public void testStoreNull(){
		BindingSource source = getCleanSource();
		String key = randomString(ALPHA_NUMERIC);
		assertFalse(source.contains(key));
		source.set(key, null);
		assertTrue(source.contains(key));
		assertNull(source.get(key, randomString()));
	}
	
	@Test
	public void testGetInvalidType(){
		BindingSource source = getCleanSource();
		String key = randomString(ALPHA_NUMERIC);
		assertFalse(source.contains(key));
		String value = randomString();
		int defaultValue = randomInteger();
		source.set(key, value);
		assertTrue(source.contains(key));
		int returned = source.get(key, defaultValue);
		assertEquals(defaultValue, returned);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testStoreIvalidType(){
		BindingSource source = getCleanSource();
		String key = randomString();
		assertFalse(source.contains(key));
		Object value = new Object();
		source.set(key, value);
	}
	
	
	
	protected final BindingSource getCleanSource(){
		BindingSource source = doGetCleanSource();
		assertNotNull(source);
		return source;
	}
	
	protected BindingSource doGetCleanSource(){
		PreferencesBindingSource source = new PreferencesBindingSource(getClass(), false);
		source.clear();
		return source;
	}

}
