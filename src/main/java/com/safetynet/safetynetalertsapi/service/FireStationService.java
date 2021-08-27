package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;

@Service
public interface FireStationService {

	public Optional<FireStation> getFireStation(final String id);

	public List<FireStation> getFireStations();

	public FireStation saveFireStation(FireStation fireStation);

	public void deleteFireStation(final String id);

	public List<FireStation> getPeopleCoveredByStationNumber(String stationNumber);
}
