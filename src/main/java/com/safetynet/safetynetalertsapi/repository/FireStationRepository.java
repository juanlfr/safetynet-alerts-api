package com.safetynet.safetynetalertsapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.safetynet.safetynetalertsapi.model.FireStation;

public interface FireStationRepository extends MongoRepository<FireStation, String> {

	List<FireStation> findAllFireStationsByStationNumber(String stationNumber);

	FireStation findFireStationByAdresse(String address);

	List<FireStation> findAllFireStationsByStationNumberIn(List<String> stationNumbers);

	void deleteByAdresse(String address);

}
