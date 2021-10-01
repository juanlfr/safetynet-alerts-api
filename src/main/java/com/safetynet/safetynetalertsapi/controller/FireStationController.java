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
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.DTO.FireDTO;
import com.safetynet.safetynetalertsapi.model.DTO.FloodDTO;
import com.safetynet.safetynetalertsapi.model.DTO.StationNumberDTO;
import com.safetynet.safetynetalertsapi.service.FireStationService;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

	@Autowired
	private FireStationService fireStationService;

	private Logger log = LogManager.getLogger(FireStationController.class);

	@GetMapping("/{address}")
	public FireStation getFireStation(@PathVariable("address") final String address) {

		try {
			log.info("Getting fireStation information with address: " + address);
			return fireStationService.getFireStation(address);

		} catch (NoSuchElementException e) {
			log.error("fireStation with id: " + address + " not found " + e);
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
					.path("/{address}")
					.buildAndExpand(fireStationAdded.getAdresse().replace(" ", ""))
					.toUri();
			return ResponseEntity.created(location).build();

		} catch (Exception e) {
			log.error("Error on firestation record creation" + e);
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
	@PutMapping("/{address}")
	public ResponseEntity<FireStation> updateFireStation(@PathVariable("address") final String address,
			@RequestBody FireStation fireStation)
			throws NoSuchElementException {

		if (address != null && !address.isEmpty()) {

			FireStation fireStationUpdated = this.getFireStation(address);

			if (fireStation.getStation() != null)
				fireStationUpdated.setStation(fireStation.getStation());

			log.info("Updating fireStation information " + fireStationUpdated.toString());

			return new ResponseEntity<FireStation>(fireStationService.saveFireStation(fireStationUpdated),
					HttpStatus.OK);
		} else {
			log.error("address is null or empty");
			return new ResponseEntity<FireStation>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Delete - Delete a fire station
	 * 
	 * @param id - The id of the fire station to delete
	 */
	@DeleteMapping("/{address}")
	public ResponseEntity<Void> deleteFireStation(@PathVariable("address") final String address) {
		if (address != null && !address.isEmpty()) {
			log.info("Deleting fire station with address: " + address);
			fireStationService.deleteFireStation(address);
			return ResponseEntity.ok().build();
		} else {
			log.error("address is null or empty");
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
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

	@GetMapping("/phoneAlert")
	public ResponseEntity<MappingJacksonValue> getPhoneNumbersByFireStationNumber(
			@PathParam("firestationNumber") String firestationNumber) throws JsonProcessingException {
		if (firestationNumber != null && !firestationNumber.isEmpty()) {
			log.info("Finding Phone Number by firestation number: " + firestationNumber);
			return new ResponseEntity<MappingJacksonValue>(
					fireStationService.getPhoneNumbersByFireStationNumber(firestationNumber), HttpStatus.OK);
		} else {
			log.warn("Firestation number is empty");
			return new ResponseEntity<MappingJacksonValue>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/fire")
	public ResponseEntity<FireDTO> getPeopleAndFirestationNumbersByAddress(
			@PathParam("address") String address) {
		if (address != null && !address.isEmpty()) {
			log.info("Finding people by addres: " + address);
			return new ResponseEntity<FireDTO>(
					fireStationService.getPeopleAndFirestationNumbersByAddress(address), HttpStatus.OK);
		} else {
			log.warn("Firestation number is empty");
			return new ResponseEntity<FireDTO>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodDTO>> getPeopleByStationsNumbers(
			@RequestParam("stations") List<String> stations) {
		log.info("Finding people by the stations: " + stations);
		if (stations != null && !stations.isEmpty()) {
			return new ResponseEntity<List<FloodDTO>>(
					fireStationService.getPeopleByStationsNumbers(stations), HttpStatus.OK);
		} else {
			log.warn("Firestation number is empty");
			return new ResponseEntity<List<FloodDTO>>(HttpStatus.BAD_REQUEST);
		}
	}

}
