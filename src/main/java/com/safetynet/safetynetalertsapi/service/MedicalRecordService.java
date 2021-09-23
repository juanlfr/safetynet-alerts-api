package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;

@Service
public interface MedicalRecordService {

	public Optional<MedicalRecord> getMedicalRecord(final String id);

	public List<MedicalRecord> getMedicalRecords();

	public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);

	public void deleteMedicalRecord(final String id);

	public MedicalRecord getMedicalRecordByFullName(String lastName, String firstName);

}
