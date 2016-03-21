package com.github.randomcodeorg.ppplugin.ppdefaults.test.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultValidationsTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeDeposit() {
		BankAccount ba = new BankAccount("abc");
		ba.deposit(-100);
		assertEquals(0, ba.getCredit());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativePayOut() {
		BankAccount ba = new BankAccount("abc");
		ba.payOut(-100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullPayOut() {
		BankAccount ba = new BankAccount("cde");
		ba.payOut(null);
	}

	@Test
	public void testEmptyPayOut() {
		BankAccount ba = new BankAccount("efg");
		ba.payOut(0);
	}

	@Test
	public void testNegativeDepositStackTrace() {
		try {
			BankAccount ba = new BankAccount("hij");
			ba.deposit(-50);
			assertEquals(0, ba.getCredit());
		} catch (IllegalArgumentException e) {
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		BankAccount account = new BankAccount(null);
		account.getAccountIdentifier();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyArray(){
		new BankAccount("abc", new String[]{});
	}
	
	@Test
	public void testNonEmptyArray(){
		new BankAccount("abc", new String[]{"Erica Example"});
	}

}
