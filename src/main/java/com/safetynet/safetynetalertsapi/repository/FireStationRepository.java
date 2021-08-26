package com.safetynet.safetynetalertsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.safetynet.safetynetalertsapi.model.FireStation;

public interface FireStationRepository extends MongoRepository<FireStation, String> {

}
