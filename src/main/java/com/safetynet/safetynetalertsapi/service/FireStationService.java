package com.safetynet.safetynetalertsapi.service;

import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.DTO.FireDTO;
import com.safetynet.safetynetalertsapi.model.DTO.FloodDTO;
import com.safetynet.safetynetalertsapi.model.DTO.StationNumberDTO;

@Service
public interface FireStationService {

	FireStation getFireStation(final String address);

	List<FireStation> getFireStations();

	FireStation saveFireStation(FireStation fireStation);

	void deleteFireStation(final String address);

	StationNumberDTO getPeopleCoveredByStationNumber(String stationNumber);

	MappingJacksonValue getPhoneNumbersByFireStationNumber(String stationNumber) throws JsonProcessingException;

	FireDTO getPeopleAndFirestationNumbersByAddress(String address);

	List<FloodDTO> getPeopleByStationsNumbers(List<String> stations);

	void saveAll(List<FireStation> fireStations);
}
