package com.safetynet.safetynetalertsapi.model;

import org.springframework.data.annotation.Id;

public class FireStation {

	@Id
	private String id;

	private String adresse;
	private String station;

	public FireStation() {
	}

	public FireStation(String adresse, String station) {
		this.adresse = adresse;
		this.station = station;
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
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Override
	public String toString() {
		return "FireStation [id=" + id + ", adresse=" + adresse + ", station=" + station + "]";
	}

}
