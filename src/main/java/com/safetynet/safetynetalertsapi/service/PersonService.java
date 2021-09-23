package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.Person;

@Service
public interface PersonService {

	Optional<Person> getPerson(final String id);

	List<Person> getPeople();

	Person savePerson(Person person);

	void deletePerson(final String id);

	List<Person> getPeopleByAddress(List<String> adresses);

	ChildAlertDTO getPeopleByAddress(String address);

}
