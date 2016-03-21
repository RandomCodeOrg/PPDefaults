package com.github.randomcodeorg.ppplugin.ppdefaults.test.validation;

import java.util.ArrayList;
import java.util.List;

import com.github.randomcodeorg.ppplugin.ppdefaults.validation.NotEmpty;
import com.github.randomcodeorg.ppplugin.ppdefaults.validation.NotNegative;
import com.github.randomcodeorg.ppplugin.ppdefaults.validation.NotNull;

public class BankAccount {

	private int credit = 0;
	private final String accountIdentifier;
	private final List<String> owner = new ArrayList<String>();
	
	public BankAccount(@NotNull @NotEmpty String accountIdentifier, @NotNull @NotEmpty Iterable<String> owner) {
		this.accountIdentifier = accountIdentifier;
		for(String o : owner) this.owner.add(o);
	}
	
	public BankAccount(@NotNull @NotEmpty String accountIdentifier, @NotNull @NotEmpty String[] owner){
		this.accountIdentifier = accountIdentifier;
		for(String o : owner) this.owner.add(o);
	}
	
	public BankAccount(@NotNull @NotEmpty String accountIdentifier){
		this(accountIdentifier, new String[]{"Erica Example"});
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

	public String getAccountIdentifier(){
		return accountIdentifier;
	}
	
}
