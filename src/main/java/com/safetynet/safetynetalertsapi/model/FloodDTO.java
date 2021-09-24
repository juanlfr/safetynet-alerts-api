package com.safetynet.safetynetalertsapi.model;

import java.util.List;

public class FloodDTO {

	private String address;
	private List<FireFloodDTO> personInfoInFireOrFloodDTO;

	public FloodDTO() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<FireFloodDTO> getPersonInfoInFireOrFloodDTO() {
		return personInfoInFireOrFloodDTO;
	}

	public void setPersonInfoInFireOrFloodDTO(List<FireFloodDTO> personInfoInFireOrFloodDTO) {
		this.personInfoInFireOrFloodDTO = personInfoInFireOrFloodDTO;
	}

	@Override
	public String toString() {
		return "FloodDTO [address=" + address + ", personInfoInFireOrFloodDTO=" + personInfoInFireOrFloodDTO + "]";
	}

}
