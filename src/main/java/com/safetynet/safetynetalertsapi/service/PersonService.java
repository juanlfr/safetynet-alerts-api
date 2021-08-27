package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Person;

@Service
public interface PersonService {

	public Optional<Person> getPerson(final String id);

	public List<Person> getPeople();

	public Person savePerson(Person person);

	public void deletePerson(final String id);

	public List<Person> getPeopleByAddress(List<String> adresses);
}
