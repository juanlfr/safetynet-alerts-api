package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public Optional<MedicalRecord> getMedicalRecord(final String id) {
		return medicalRecordRepository.findById(id);
	}

	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecordRepository.findAll();
	}

	public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
		return medicalRecordRepository.save(medicalRecord);
	}

	public void deleteMedicalRecord(final String id) {
		medicalRecordRepository.deleteById(id);
	}

}
