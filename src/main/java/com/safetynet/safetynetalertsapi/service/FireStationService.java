package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.StationNumberDTO;

@Service
public interface FireStationService {

	Optional<FireStation> getFireStation(final String id);

	List<FireStation> getFireStations();

	FireStation saveFireStation(FireStation fireStation);

	void deleteFireStation(final String id);

	StationNumberDTO getPeopleCoveredByStationNumber(String stationNumber);
}
