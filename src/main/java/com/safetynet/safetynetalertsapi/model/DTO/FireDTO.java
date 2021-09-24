package com.safetynet.safetynetalertsapi.model.DTO;

import java.util.List;

public class FireDTO {

	private String firestationNumber;
	private List<FireFloodDTO> personInfoInFireOrFloodDTO;

	public FireDTO() {
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
