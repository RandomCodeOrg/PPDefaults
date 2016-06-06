package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding.Bind;
import com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding.Binding;
import com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding.BindingId;
import com.github.randomcodeorg.ppplugin.ppdefaults.contentbinding.MemoryBindingProviderFactory;

@Binding(providerFactory=MemoryBindingProviderFactory.class)
public class Account {

	@Bind
	@BindingId
	private String accountNumber;
	
	public String getAccountNumber(){
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

}
