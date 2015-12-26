package com.github.randomcodeorg.ppplugin.ppdefaults.access;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccessRestrictionTest {


	@Test
	@Restricted(controller=AlwaysAllowedAccessController.class, extra = "Hello")
	public void test(){
		assertNull(null);
	}	
	
}
