package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.randomcodeorg.ppplugin.ppdefaults.testing.BaseTest;

public class ConstructorBasedBindingTest extends BaseTest {

	
	@Test
	public void test() {
		SomeClass a = new SomeClass();
		SomeClass b = new SomeClass();
		
		assertEquals(a.getUsername(), b.getUsername());
		assertEquals(a.getAge(), b.getAge());
		
		a.setAge(randomInteger());
		a.setUsername(randomString());
		
		assertEquals(a.getUsername(), b.getUsername());
		assertEquals(a.getAge(), b.getAge());
	}

}
