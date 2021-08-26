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

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.service.FireStationService;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

	@Autowired
	private FireStationService fireStationService;

	private Logger log = LogManager.getLogger(FireStationController.class);

	/**
	 * Read - Get all firestations
	 * 
	 * @return - A List object of firestations full filled
	 */
	@GetMapping("/firestations")
	public List<FireStation> getFireStations() {
		log.info("Retriving all fire stations");
		return fireStationService.getFireStations();
	}

	@GetMapping("/{id}")
	public FireStation getFireStation(@PathVariable("id") final String id) {
		FireStation fireStation = null;
		try {
			log.info("Getting fireStation information with id: " + id);
			return fireStationService.getFireStation(id).get();

		} catch (NoSuchElementException e) {
			log.error("fireStation with id: " + id + " not found " + e);
		}
		return fireStation;

	}

	/**
	 * Creates a fire station
	 * 
	 * @param fireStation
	 * @return
	 */
	@PostMapping
	public FireStation createfireStation(@RequestBody FireStation fireStation) {
		log.info("Creating fireStation with id: " + fireStation.toString());
		return fireStationService.saveFireStation(fireStation);
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
	public FireStation updatePerson(@PathVariable("id") final String id, @RequestBody FireStation fireStation)
			throws NoSuchElementException {

		FireStation fireStationUpdated = this.getFireStation(id);

		if (fireStation.getStation() != null)
			fireStationUpdated.setStation(fireStation.getStation());

		log.info("Updating fireStation information " + fireStationUpdated.toString());

		fireStationService.saveFireStation(fireStationUpdated);

		return fireStationUpdated;

	}

	/**
	 * Delete - Delete a fire station
	 * 
	 * @param id - The id of the fire station to delete
	 */
	@DeleteMapping("/{id}")
	public void deletePerson(@PathVariable("id") final String id) {
		log.info("Deleting fire station with id: " + id);
		fireStationService.deleteFireStation(id);
	}

}
