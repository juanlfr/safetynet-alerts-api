package com.safetynet.safetynetalertsapi.model;

import java.util.List;

public class ChildAlertDTO {

	private String firstName;
	private String lastName;
	private int age;
	private List<Person> houseHoldMembers;

	public ChildAlertDTO() {
	}

	public ChildAlertDTO(String firstName, String lastName, int age, List<Person> houseHoldMembers) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.houseHoldMembers = houseHoldMembers;
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

	public List<Person> getHouseHoldMembers() {
		return houseHoldMembers;
	}

	public void setHouseHoldMembers(List<Person> houseHoldMembers) {
		this.houseHoldMembers = houseHoldMembers;
	}

	@Override
	public String toString() {
		return "ChildAlertDTO [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
				+ ", houseHoldMembers=" + houseHoldMembers + "]";
	}

}
