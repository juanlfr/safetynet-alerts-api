package com.safetynet.safetynetalertsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {

	MedicalRecord findByLastNameAndFirstName(String lastName, String firstName);

}
