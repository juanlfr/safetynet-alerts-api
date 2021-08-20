package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	private Logger log = LogManager.getLogger(PersonService.class);

	public Optional<Person> getPerson(final String id) {
		return personRepository.findById(id);
	}

	public List<Person> getPeople() {
		log.debug("*************" + "call to personRepository" + "****************");
		return personRepository.findAll();
	}

	public void deletePerson(final String id) {
		personRepository.deleteById(id);
	}

	public Person savePerson(Person person) {
		Person savedPerson = personRepository.save(person);
		return savedPerson;
	}
}
