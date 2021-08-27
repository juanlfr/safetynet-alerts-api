package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.repository.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Override
	public Optional<Person> getPerson(String id) {
		return personRepository.findById(id);
	}

	@Override
	public List<Person> getPeople() {
		return personRepository.findAll();
	}

	@Override
	public Person savePerson(Person person) {
		Person savedPerson = personRepository.save(person);
		return savedPerson;
	}

	@Override
	public void deletePerson(String id) {
		personRepository.deleteById(id);
	}

	@Override
	public List<Person> getPeopleByAddress(List<String> adresses) {
		return personRepository.findAllPeopleByAddress(adresses);
	}

}
