package com.safetynet.safetynetalertsapi.controller;

import java.net.URI;
import java.util.NoSuchElementException;

import javax.validation.constraints.NotBlank;

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

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
	@Autowired
	private MedicalRecordService medicalRecordService;

	private Logger log = LogManager.getLogger(MedicalRecordController.class);

	/**
	 * Get a medical record
	 * 
	 * @param fullname
	 * @return MedicalRecord
	 */

	@GetMapping("/{fullName}")
	public MedicalRecord getMedicalRecord(@PathVariable("fullName") final String fullName) {

		log.info("Getting medical Record information with fullName: " + fullName);
		MedicalRecord medicalrecord = medicalRecordService.getMedicalRecord(fullName);
		if (medicalrecord == null)
			throw new NoSuchElementException();
		return medicalrecord;

	}

	/**
	 * Creates and saves a medical record
	 * 
	 * @param medicalRecord
	 * @return ResponseEntity
	 */

	@PostMapping
	public ResponseEntity<Void> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {

		log.info("Creating MedicalRecord with name: " + medicalRecord.toString());
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
	 * @param fireStation
	 * @return the fire station updated
	 * @throws NoSuchElementException
	 */
	@PutMapping("/{fullName}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable("fullName") final @NotBlank String fullName,
			@RequestBody MedicalRecord medicalRecord)
			throws NoSuchElementException {

		MedicalRecord medicalRecordUpdated = this.getMedicalRecord(fullName);

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
	@DeleteMapping("/{fullName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable("fullName") final String fullName) {
		log.info("Deleting medical Record with fullName: " + fullName);
		medicalRecordService.deleteMedicalRecord(fullName);
		return ResponseEntity.ok().build();
	}

}
