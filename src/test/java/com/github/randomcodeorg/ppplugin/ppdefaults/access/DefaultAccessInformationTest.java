package com.github.randomcodeorg.ppplugin.ppdefaults.access;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultAccessInformationTest {

	@Test
	public void testAccessInformationBuilding() {
		AccessInformation info = DefaultAccessInformation.build();
		assertNotNull(info);
		StackTraceElement[] stackTrace = info.getStackTrace();
		assertNotEquals(0, stackTrace.length);
		StackTraceElement first = stackTrace[0];
		assertEquals("testAccessInformationBuilding", first.getMethodName());
	}

}
