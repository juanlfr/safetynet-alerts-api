package com.safetynet.safetynetalertsapi.model.DTO;

import java.util.List;

import com.safetynet.safetynetalertsapi.model.Person;

public class ChildAlertDTO {

	private List<ChildDTO> childs;
	private List<Person> houseHoldMembers;

	public ChildAlertDTO() {
	}

	public List<ChildDTO> getChilds() {
		return childs;
	}

	public void setChilds(List<ChildDTO> childs) {
		this.childs = childs;
	}

	public List<Person> getHouseHoldMembers() {
		return houseHoldMembers;
	}

	public void setHouseHoldMembers(List<Person> houseHoldMembers) {
		this.houseHoldMembers = houseHoldMembers;
	}

	@Override
	public String toString() {
		return "ChildAlertDTO [childs=" + childs + ", houseHoldMembers=" + houseHoldMembers + "]";
	}

}
