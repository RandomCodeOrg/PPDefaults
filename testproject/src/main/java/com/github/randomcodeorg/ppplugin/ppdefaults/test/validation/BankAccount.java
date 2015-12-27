package com.github.randomcodeorg.ppplugin.ppdefaults.test.validation;

import com.github.randomcodeorg.ppplugin.ppdefaults.validation.NotNegative;
import com.github.randomcodeorg.ppplugin.ppdefaults.validation.NotNull;

public class BankAccount {

	private int credit = 0;

	public BankAccount() {
	}

	public void deposit(@NotNegative int amount) {
		credit += amount;
	}

	public void payOut(@NotNegative @NotNull Integer amount) {
		credit -= amount;
	}

	public int getCredit() {
		return credit;
	}

}
