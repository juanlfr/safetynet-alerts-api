package com.safetynet.safetynetalertsapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;

@Service
public interface MedicalRecordService {

	MedicalRecord getMedicalRecord(final String fullName);

	MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);

	void deleteMedicalRecord(final String id);

	MedicalRecord getMedicalRecordByFullName(String lastName, String firstName);

	void saveAll(List<MedicalRecord> medicalRecords);

	List<MedicalRecord> findAll();

}
