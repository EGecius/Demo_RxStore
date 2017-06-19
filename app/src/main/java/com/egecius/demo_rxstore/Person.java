package com.egecius.demo_rxstore;

public class Person {

	public final String name;
	public final int age;

	public Person(final String name, final int age) {
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
