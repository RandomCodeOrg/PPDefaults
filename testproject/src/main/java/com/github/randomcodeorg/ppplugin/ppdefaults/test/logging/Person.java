package com.github.randomcodeorg.ppplugin.ppdefaults.test.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.logging.LogLevel;
import com.github.randomcodeorg.ppplugin.ppdefaults.logging.LogThis;
import com.github.randomcodeorg.ppplugin.ppdefaults.logging.Stealth;

public class Person {

	private String firstname;
	private String lastname;
	private boolean isMale;
	private int age;
	private String superSecretPassword;
	private String username;

	public Person() {

	}

	public String getSuperSecretPassword() {
		return superSecretPassword;
	}

	@LogThis(value = LogLevel.WARNING)
	public void setSuperSecretPassword(@Stealth String superSecretPassword) {
		this.superSecretPassword = superSecretPassword;
	}

	@LogThis
	public String getUsername() {
		return username;
	}

	@LogThis
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	@LogThis
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	@LogThis
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public boolean isMale() {
		return isMale;
	}

	@LogThis(value = LogLevel.WARNING)
	public void setIsMale(boolean isMale) {
		this.isMale = isMale;
	}

	public int getAge() {
		return age;
	}

	@LogThis
	public void setAge(int age) {
		this.age = age;
	}

	@LogThis
	public boolean login(String username, @Stealth String password) {
		return username.equals(this.username) && password.equals(superSecretPassword);
	}

}
