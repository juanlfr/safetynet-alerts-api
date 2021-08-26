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

import com.safetynet.safetynetalertsapi.model.Person;
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
	public List<Person> getPeople() {
		log.info("*************" + "call to service" + "****************");
		return personService.getPeople();
	}

	/**
	 * Read - Get one employee
	 * 
	 * @param id The id of the employee
	 * @return An person object full filled
	 */
	@GetMapping("/{id}")
	public Person getPerson(@PathVariable("id") final String id) {

		Person person = null;
		try {
			log.info("Getting person information with id: " + id);
			return personService.getPerson(id).get();

		} catch (NoSuchElementException e) {
			log.error("Person with id: " + id + " not found " + e);
		}
		return person;

	}

	@PostMapping
	public Person createPerson(@RequestBody Person person) {
		log.info("Creating person with id: " + person.toString());
		return personService.savePerson(person);
	}

	@PutMapping("/{id}")
	public Person updatePerson(@PathVariable("id") final String id, @RequestBody Person person)
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

		personService.savePerson(personUpdated);

		return personUpdated;

	}

	/**
	 * Delete - Delete an employee
	 * 
	 * @param id - The id of the employee to delete
	 */
	@DeleteMapping("/{id}")
	public void deletePerson(@PathVariable("id") final String id) {
		log.info("Deleting person with id: " + id);
		personService.deletePerson(id);
	}

}
