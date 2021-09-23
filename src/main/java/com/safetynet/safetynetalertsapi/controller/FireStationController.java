package com.safetynet.safetynetalertsapi.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import javax.websocket.server.PathParam;

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

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.StationNumberDTO;
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
	public ResponseEntity<List<FireStation>> getFireStations() {
		log.info("Retriving all fire stations");
		List<FireStation> firestations = fireStationService.getFireStations();
		if (!firestations.isEmpty()) {
			return new ResponseEntity<>(firestations, HttpStatus.OK);
		} else {
			log.error("The list is empty");
			return new ResponseEntity<List<FireStation>>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{id}")
	public FireStation getFireStation(@PathVariable("id") final String id) {

		try {
			log.info("Getting fireStation information with id: " + id);
			return fireStationService.getFireStation(id).get();

		} catch (NoSuchElementException e) {
			log.error("fireStation with id: " + id + " not found " + e);
		}
		return null;

	}

	/**
	 * Creates a fire station
	 * 
	 * @param fireStation
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> createfireStation(@RequestBody FireStation fireStation) {
		log.info("Creating fireStation with id: " + fireStation.toString());
		try {
			FireStation fireStationAdded = fireStationService.saveFireStation(fireStation);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(fireStationAdded.getId())
					.toUri();
			return ResponseEntity.created(location).build();

		} catch (Exception e) {
			log.error("Error on firestation record creation");
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	public ResponseEntity<FireStation> updateFireStation(@PathVariable("id") final String id,
			@RequestBody FireStation fireStation)
			throws NoSuchElementException {

		FireStation fireStationUpdated = this.getFireStation(id);

		if (fireStation.getStation() != null)
			fireStationUpdated.setStation(fireStation.getStation());

		log.info("Updating fireStation information " + fireStationUpdated.toString());

		return new ResponseEntity<FireStation>(fireStationService.saveFireStation(fireStationUpdated),
				HttpStatus.OK);

	}

	/**
	 * Delete - Delete a fire station
	 * 
	 * @param id - The id of the fire station to delete
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFireStation(@PathVariable("id") final String id) {
		log.info("Deleting fire station with id: " + id);
		fireStationService.deleteFireStation(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<StationNumberDTO> getPeopleCoveredByStationNumber(
			@PathParam("stationNumber") String stationNumber) {
		if (stationNumber != null && !stationNumber.isEmpty()) {
			log.info("Finding people by firestation number: " + stationNumber);
			return new ResponseEntity<StationNumberDTO>(
					fireStationService.getPeopleCoveredByStationNumber(stationNumber), HttpStatus.OK);
		} else {
			log.warn("Firestation number is empty");
			return new ResponseEntity<StationNumberDTO>(HttpStatus.BAD_REQUEST);
		}
	}

}
