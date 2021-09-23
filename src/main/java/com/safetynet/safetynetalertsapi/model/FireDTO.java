package com.safetynet.safetynetalertsapi.model;

import java.util.List;

public class FireDTO {

	private List<FireFloodDTO> personInfoInFireOrFloodDTO;
	private String firestationNumber;

	public FireDTO() {
		// TODO Auto-generated constructor stub
	}

	public List<FireFloodDTO> getPersonInfoInFireOrFloodDTO() {
		return personInfoInFireOrFloodDTO;
	}

	public void setPersonInfoInFireOrFloodDTO(List<FireFloodDTO> personInfoInFireOrFloodDTO) {
		this.personInfoInFireOrFloodDTO = personInfoInFireOrFloodDTO;
	}

	public String getFirestationNumber() {
		return firestationNumber;
	}

	public void setFirestationNumber(String string) {
		this.firestationNumber = string;
	}

}
