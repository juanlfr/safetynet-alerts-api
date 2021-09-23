package com.safetynet.safetynetalertsapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

	private List<Person> persons;
	@JsonProperty("firestations")
	private List<FireStation> fireStations;
	private List<MedicalRecord> medicalrecords;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<FireStation> getFireStations() {
		return fireStations;
	}

	public void setFireStations(List<FireStation> fireStations) {
		this.fireStations = fireStations;
	}

	public List<MedicalRecord> getMedicalrecords() {
		return medicalrecords;
	}

	public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}

}
