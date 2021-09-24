package com.safetynet.safetynetalertsapi.model.DTO;

public class ChildDTO {

	private String firstName;
	private String lastName;
	private int age;

	public ChildDTO() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "ChildDTO [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}

}
