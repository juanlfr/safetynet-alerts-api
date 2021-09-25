package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;

@Service
public interface MedicalRecordService {

	Optional<MedicalRecord> getMedicalRecord(final String id);

	List<MedicalRecord> getMedicalRecords();

	MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);

	void deleteMedicalRecord(final String id);

	MedicalRecord getMedicalRecordByFullName(String lastName, String firstName);

	void saveAll(List<MedicalRecord> medicalRecords);

}
