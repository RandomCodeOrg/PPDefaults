package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.randomcodeorg.ppplugin.ppdefaults.testing.BaseTest;

public class ConstructorBasedBindingTest extends BaseTest {

	
	@Test
	public void test() {
		SomeClass a = new SomeClass();
		SomeClass b = new SomeClass();
		
		System.out.println(String.format("%s %d", a.getUsername(), a.getAge()));
		
		assertEquals(a.getUsername(), b.getUsername());
		assertEquals(a.getAge(), b.getAge());
		
		a.setAge(randomInteger());
		a.setUsername(randomString());
		
		System.out.println(String.format("%s %d", a.getUsername(), a.getAge()));
		
		assertEquals(a.getUsername(), b.getUsername());
		assertEquals(a.getAge(), b.getAge());
	}

}
