package com.safetynet.safetynetalertsapi.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.service.MedicalRecordService;

import io.micrometer.core.instrument.util.StringUtils;

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
	public ResponseEntity<List<MedicalRecord>> getMedicalRecords() {

		log.info("Retriving all medical records");

		List<MedicalRecord> medicalrecords = medicalRecordService.getMedicalRecords();

		if (!medicalrecords.isEmpty()) {
			return new ResponseEntity<>(medicalrecords, HttpStatus.OK);
		} else {
			log.error("The list is empty");
			return new ResponseEntity<List<MedicalRecord>>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Get a medical record
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping("/{id}")
	public MedicalRecord getMedicalRecord(@PathVariable("id") final String id) {

		try {
			log.info("Getting medical Record information with id: " + id);
			return medicalRecordService.getMedicalRecord(id).get();

		} catch (NoSuchElementException e) {
			log.error("medical Record with id: " + id + " not found " + e);
		}
		return null;

	}

	@PostMapping
	public ResponseEntity<Void> createfireStation(@RequestBody MedicalRecord medicalRecord) {
		log.info("Creating fireStation with id: " + medicalRecord.toString());
		try {
			MedicalRecord medicalRecordAdded = medicalRecordService.saveMedicalRecord(medicalRecord);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(medicalRecordAdded.getId())
					.toUri();
			return ResponseEntity.created(location).build();

		} catch (Exception e) {
			log.error("Error on medical record creation");
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Updates a medical record
	 * 
	 * @param id
	 * @param fireStation
	 * @return the fire station updated
	 * @throws NoSuchElementException
	 */
	@PutMapping("/{id}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable("id") final String id,
			@RequestBody MedicalRecord medicalRecord)
			throws NoSuchElementException {

		if (StringUtils.isEmpty(id)) {
			log.error("Id number is absent for the update");
			return new ResponseEntity<MedicalRecord>(HttpStatus.BAD_REQUEST);
		}

		MedicalRecord medicalRecordUpdated = this.getMedicalRecord(id);

		if (medicalRecord.getBirthdate() != null)
			medicalRecordUpdated.setBirthdate(medicalRecord.getBirthdate());
		if (medicalRecord.getMedications() != null)
			medicalRecordUpdated.setMedications(medicalRecord.getMedications());
		if (medicalRecord.getAllergies() != null)
			medicalRecordUpdated.setAllergies(medicalRecord.getAllergies());

		log.info("Updating medical Record information " + medicalRecordUpdated.toString());

		return new ResponseEntity<MedicalRecord>(medicalRecordService.saveMedicalRecord(medicalRecordUpdated),
				HttpStatus.OK);

	}

	/**
	 * Delete - Delete a medical Record
	 * 
	 * @param id - The id of the medical Record to delete
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePerson(@PathVariable("id") final String id) {
		log.info("Deleting medical Record with id: " + id);
		medicalRecordService.deleteMedicalRecord(id);
		return ResponseEntity.ok().build();
	}

}
