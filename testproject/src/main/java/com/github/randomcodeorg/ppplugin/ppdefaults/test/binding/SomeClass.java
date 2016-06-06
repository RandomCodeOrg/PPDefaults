package com.github.randomcodeorg.ppplugin.ppdefaults.test.binding;

import com.github.randomcodeorg.ppplugin.ppdefaults.binding.Binding;

public class SomeClass {

	private final Binding<String> username = new Binding<String>("username", MyBindingSourceFactory.class, "");
	private final Binding<Integer> age = new Binding<Integer>("age", MyBindingSourceFactory.class, 12);

	public SomeClass() {

	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String username) {
		this.username.set(username);
	}

	public int getAge() {
		return age.get();
	}

	public void setAge(int age) {
		this.age.set(age);
	}

}
