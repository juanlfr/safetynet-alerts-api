package com.safetynet.safetynetalertsapi.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
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
@Validated
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
	public ResponseEntity<Void> createfireStation(@RequestBody @NotNull FireStation fireStation) {

		FireStation fireStationAdded = fireStationService.saveFireStation(fireStation);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{address}")
				.buildAndExpand(fireStationAdded.getAdresse().replace(" ", ""))
				.toUri();
		return ResponseEntity.created(location).build();

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
	public ResponseEntity<FireStation> updateFireStation(@PathVariable("address") @NotBlank final String address,
			@RequestBody @NotNull FireStation fireStation) {

		FireStation fireStationUpdated = this.getFireStation(address);

		if (fireStationUpdated != null) {

			log.info("Updating fireStation information " + fireStationUpdated.toString());
			fireStationUpdated.setStation(fireStation.getStation());

			return new ResponseEntity<FireStation>(fireStationService.saveFireStation(fireStationUpdated),
					HttpStatus.OK);
		} else {
			log.error("Firestation not founded" + fireStationUpdated);
			throw new NoSuchElementException();
		}
	}

	/**
	 * Delete - Delete a fire station
	 * 
	 * @param id - The id of the fire station to delete
	 */
	@DeleteMapping("/{address}")
	public ResponseEntity<Void> deleteFireStation(@PathVariable("address") @NotBlank final String address) {
		log.info("Deleting fire station with address: " + address);
		fireStationService.deleteFireStation(address);
		return ResponseEntity.ok().build();

	}

	@GetMapping
	public ResponseEntity<StationNumberDTO> getPeopleCoveredByStationNumber(
			@PathParam("stationNumber") @NotBlank String stationNumber) {

		log.info("Finding people by firestation number: " + stationNumber);
		return new ResponseEntity<StationNumberDTO>(
				fireStationService.getPeopleCoveredByStationNumber(stationNumber), HttpStatus.OK);

	}

	@GetMapping("/phoneAlert")
	public ResponseEntity<MappingJacksonValue> getPhoneNumbersByFireStationNumber(
			@PathParam("firestationNumber") @NotBlank String firestationNumber) throws JsonProcessingException {

		log.info("Finding Phone Number by firestation number: " + firestationNumber);
		return new ResponseEntity<MappingJacksonValue>(
				fireStationService.getPhoneNumbersByFireStationNumber(firestationNumber), HttpStatus.OK);
	}

	@GetMapping("/fire")
	public ResponseEntity<FireDTO> getPeopleAndFirestationNumbersByAddress(
			@PathParam("address") @NotBlank String address) {

		log.info("Finding people by addres: " + address);
		return new ResponseEntity<FireDTO>(
				fireStationService.getPeopleAndFirestationNumbersByAddress(address), HttpStatus.OK);
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodDTO>> getPeopleByStationsNumbers(
			@RequestParam("stations") @NotEmpty List<String> stations) {
		log.info("Finding people by the stations: " + stations);

		return new ResponseEntity<List<FloodDTO>>(
				fireStationService.getPeopleByStationsNumbers(stations), HttpStatus.OK);

	}

}
