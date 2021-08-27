package com.safetynet.safetynetalertsapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "firestation")
public class FireStation {

	@Id
	private String id;

	@Field("address")
	private String adresse;
	@Field("station")
	private String stationNumber;

	public FireStation() {
	}

	public FireStation(String adresse, String station) {
		this.adresse = adresse;
		this.stationNumber = station;
	}

	public String getId() {
		return id;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getStation() {
		return stationNumber;
	}

	public void setStation(String station) {
		this.stationNumber = station;
	}

	@Override
	public String toString() {
		return "FireStation [id=" + id + ", adresse=" + adresse + ", station=" + stationNumber + "]";
	}

}
