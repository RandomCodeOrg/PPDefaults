package com.github.randomcodeorg.ppplugin.ppdefaults.test.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultValidationsTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeDeposit() {
		BankAccount ba = new BankAccount();
		ba.deposit(-100);
		assertEquals(0, ba.getCredit());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativePayOut() {
		BankAccount ba = new BankAccount();
		ba.payOut(-100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPayOut() {
		BankAccount ba = new BankAccount();
		ba.payOut(null);
	}

	@Test
	public void testEmptyPayOut() {
		BankAccount ba = new BankAccount();
		ba.payOut(0);
	}

	@Test
	public void testNegativeDepositStackTrace() {
		try {
			BankAccount ba = new BankAccount();
			ba.deposit(-50);
			assertEquals(0, ba.getCredit());
		} catch (IllegalArgumentException e) {
		}
	}

}
