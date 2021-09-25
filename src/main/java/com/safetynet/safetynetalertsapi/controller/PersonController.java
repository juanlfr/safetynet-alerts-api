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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	private Logger log = LogManager.getLogger(PersonController.class);

	/**
	 * Read - Get all people
	 * 
	 * @return - A List object of people full filled
	 */
	@GetMapping("/people")
	public ResponseEntity<MappingJacksonValue> getPeople() {
		log.info("*************" + "call to Person controller: method getPeople" + "****************");

		List<Person> people = personService.getPeople();
		if (people != null && !people.isEmpty()) {
			MappingJacksonValue responseDTO = setFiltersToFalse(people);
			return new ResponseEntity<MappingJacksonValue>(responseDTO, HttpStatus.OK);
		} else {
			log.warn("No people founded");
			return null;
		}
	}

	/**
	 * Read - Get one employee
	 * 
	 * @param id The id of the employee
	 * @return An person object full filled
	 */
	@GetMapping("/{id}")
	public Person getPerson(@PathVariable("id") final String id) {

		try {
			log.info("Getting person information with id: " + id);
			return personService.getPerson(id).get();

		} catch (NoSuchElementException e) {
			log.error("Person with id: " + id + " not found " + e);
		}
		return null;

	}

	@PostMapping
	public ResponseEntity<Void> createPerson(@RequestBody Person person) {
		log.info("Creating person with id: " + person.toString());
		try {
			Person personAdded = personService.savePerson(person);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(personAdded.getId())
					.toUri();
			return ResponseEntity.created(location).build();

		} catch (Exception e) {
			log.error("Error on person creation");
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable("id") final String id, @RequestBody Person person)
			throws NoSuchElementException {

		Person personUpdated = this.getPerson(id);

		if (person.getAddress() != null)
			personUpdated.setAddress(person.getAddress());
		if (person.getCity() != null)
			personUpdated.setCity(person.getCity());
		if (person.getZip() != null)
			personUpdated.setZip(person.getZip());
		if (person.getEmail() != null)
			personUpdated.setEmail(person.getEmail());
		if (person.getPhone() != null)
			personUpdated.setPhone(person.getPhone());

		log.info("Updating person information" + personUpdated.toString());

		return new ResponseEntity<Person>(personService.savePerson(personUpdated),
				HttpStatus.OK);

	}

	/**
	 * Delete - Delete a person
	 * 
	 * @param id - The id of the employee to delete
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePerson(@PathVariable("id") final String id) {
		log.info("Deleting person with id: " + id);
		personService.deletePerson(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/childAlert")
	public ResponseEntity<MappingJacksonValue> getChildsByAddress(@PathParam("address") String address) {
		if (address != null && !address.isEmpty()) {
			log.info("Finding childs by address: " + address);

			ChildAlertDTO childAlertDTO = personService.getPeopleByAddress(address);
			if (childAlertDTO != null) {
				MappingJacksonValue responseDTO = setFiltersToFalse(childAlertDTO);
				return new ResponseEntity<MappingJacksonValue>(responseDTO, HttpStatus.OK);
			} else {
				log.warn("No childs founded");
				return null;
			}
		} else {
			log.warn("Address is empty");
			return new ResponseEntity<MappingJacksonValue>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonInfoDTO>> getPeopleByName(@PathParam("firstName") String firstName,
			@PathParam("lastName") String lastName) {
		if (lastName != null && !lastName.isEmpty()) {
			log.info("Finding people by first name and last name: " + firstName + lastName);
			return new ResponseEntity<List<PersonInfoDTO>>(
					personService.getPeopleByName(firstName, lastName), HttpStatus.OK);
		} else {
			log.warn("lastName is empty");
			return new ResponseEntity<List<PersonInfoDTO>>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/communityEmail")
	public ResponseEntity<MappingJacksonValue> getEmailByCity(@PathParam("city") String city) {

		if (city != null && !city.isEmpty()) {
			log.info("Finding email by city: " + city);
			return new ResponseEntity<MappingJacksonValue>(
					personService.getEmailByCity(city), HttpStatus.OK);
		} else {
			log.warn("city is empty");
			return new ResponseEntity<MappingJacksonValue>(HttpStatus.BAD_REQUEST);
		}

	}

	private MappingJacksonValue setFiltersToFalse(Object o) {
		FilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(false);
		MappingJacksonValue responseDTO = new MappingJacksonValue(o);
		responseDTO.setFilters(filters);
		return responseDTO;
	}
}
