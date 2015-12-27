package com.github.randomcodeorg.ppplugin.ppdefaults.test.logging;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import com.github.randomcodeorg.ppplugin.ppdefaults.logging.LogLevel;



public class MethodCallLogTest {

	private TestLogger log;
	
	@Before
	public void setup(){
		assertNotNull(TestLogger.INSTANCE);
		log = TestLogger.INSTANCE;
	}
	
	@Test
	public void test(){
		log.clear();
		Person p = new Person();
		p.getFirstname();
		assertFalse(log.hasEntries());
		p.setFirstname("Max");
		assertEquals(1, log.get(LogLevel.DEBUG).size());
	}
	
	

}
