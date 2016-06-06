package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.randomcodeorg.ppplugin.ppdefaults.binding.BindingSource;
import com.github.randomcodeorg.ppplugin.ppdefaults.testing.BaseTest;

public class ConstructorBasedBindingTest extends BaseTest {

	@Before
	public void setup(){
		MyBindingSourceFactory factory = new MyBindingSourceFactory();
		BindingSource source = factory.getBindingSource("");
		if(source.contains("username")){
			source.remove("username");
		}
		if(source.contains("age")){
			source.remove("age");
		}
	}
	
	
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
