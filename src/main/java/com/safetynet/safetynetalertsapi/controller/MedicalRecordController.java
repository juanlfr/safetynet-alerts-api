package com.safetynet.safetynetalertsapi.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.service.MedicalRecordService;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
	@Autowired
	private MedicalRecordService medicalRecordService;

	private Logger log = LogManager.getLogger(MedicalRecordController.class);

	/**
	 * Read - Get all medical records
	 * 
	 * @return - A List object of medical records full filled
	 */
	@GetMapping("/medicalRecords")
	public List<MedicalRecord> getMedicalRecords() {
		log.info("Retriving all medical records");
		return medicalRecordService.getMedicalRecords();
	}

	/**
	 * Get a medical record
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping("/{id}")
	public MedicalRecord getMedicalRecord(@PathVariable("id") final String id) {
		MedicalRecord medicalRecord = null;
		try {
			log.info("Getting medical Record information with id: " + id);
			return medicalRecordService.getMedicalRecord(id).get();

		} catch (NoSuchElementException e) {
			log.error("medical Record with id: " + id + " not found " + e);
		}
		return medicalRecord;

	}

	@PostMapping
	public MedicalRecord createfireStation(@RequestBody MedicalRecord medicalRecord) {
		log.info("Creating fireStation with id: " + medicalRecord.toString());
		return medicalRecordService.saveMedicalRecord(medicalRecord);
	}

	/**
	 * Updates a fire station
	 * 
	 * @param id
	 * @param fireStation
	 * @return the fire station updated
	 * @throws NoSuchElementException
	 */
	@PutMapping("/{id}")
	public MedicalRecord updateMedicalRecord(@PathVariable("id") final String id,
			@RequestBody MedicalRecord medicalRecord)
			throws NoSuchElementException {

		MedicalRecord medicalRecordUpdated = this.getMedicalRecord(id);

		if (medicalRecord.getBirthdate() != null)
			medicalRecordUpdated.setBirthdate(medicalRecord.getBirthdate());
		if (medicalRecord.getMedications() != null)
			medicalRecordUpdated.setMedications(medicalRecord.getMedications());
		if (medicalRecord.getAllergies() != null)
			medicalRecordUpdated.setAllergies(medicalRecord.getAllergies());

		log.info("Updating medical Record information " + medicalRecordUpdated.toString());

		medicalRecordService.saveMedicalRecord(medicalRecordUpdated);

		return medicalRecordUpdated;

	}

	/**
	 * Delete - Delete a medical Record
	 * 
	 * @param id - The id of the medical Record to delete
	 */
	@DeleteMapping("/{id}")
	public void deletePerson(@PathVariable("id") final String id) {
		log.info("Deleting medical Record with id: " + id);
		medicalRecordService.deleteMedicalRecord(id);
	}

}
