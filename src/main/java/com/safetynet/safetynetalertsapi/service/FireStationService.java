package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalertsapi.model.FireDTO;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.FloodDTO;
import com.safetynet.safetynetalertsapi.model.StationNumberDTO;

@Service
public interface FireStationService {

	Optional<FireStation> getFireStation(final String id);

	List<FireStation> getFireStations();

	FireStation saveFireStation(FireStation fireStation);

	void deleteFireStation(final String id);

	StationNumberDTO getPeopleCoveredByStationNumber(String stationNumber);

	String getPhoneNumbersByFireStationNumber(String stationNumber) throws JsonProcessingException;

	FireDTO getPeopleAndFirestationNumbersByAddress(String address);

	List<FloodDTO> getPeopleByStationsNumbers(List<String> stations);
}
