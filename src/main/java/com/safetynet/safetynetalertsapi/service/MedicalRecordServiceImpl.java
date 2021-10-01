package com.safetynet.safetynetalertsapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.repository.MedicalRecordRepository;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Override
	public MedicalRecord getMedicalRecord(String fullName) {
		String[] name = fullName.trim().split("\\s+");
		System.out.println(name[0] + name[1]);
		return medicalRecordRepository.findByLastNameAndFirstName(name[0], name[1]);
	}

	@Override
	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecordRepository.findAll();
	}

	@Override
	public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
		return medicalRecordRepository.save(medicalRecord);
	}

	@Override
	public void deleteMedicalRecord(String id) {
		medicalRecordRepository.deleteById(id);
	}

	@Override
	public MedicalRecord getMedicalRecordByFullName(String lastName, String firstName) {
		MedicalRecord medicalrecords = medicalRecordRepository.findByLastNameAndFirstName(lastName,
				firstName);
		return medicalrecords;
	}

	@Override
	public void saveAll(List<MedicalRecord> medicalRecords) {
		medicalRecordRepository.saveAll(medicalRecords);
	}

}
