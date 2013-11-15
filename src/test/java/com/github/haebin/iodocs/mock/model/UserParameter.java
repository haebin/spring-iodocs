package com.github.haebin.iodocs.mock.model;

import javax.validation.constraints.NotNull;

public class UserParameter extends Param {
	@NotNull
	private String name = "";
	
	private int age = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}