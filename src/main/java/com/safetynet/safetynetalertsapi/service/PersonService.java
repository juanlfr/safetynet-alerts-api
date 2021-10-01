package com.safetynet.safetynetalertsapi.service;

import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonInfoDTO;

@Service
public interface PersonService {

	Person getPerson(final String fullName);

	List<Person> getPeople();

	Person savePerson(Person person);

	void deletePerson(final String fullName);

	List<Person> getPeopleByAddress(List<String> adresses);

	ChildAlertDTO getPeopleByAddress(String address);

	List<PersonInfoDTO> getPeopleByName(String firstName, String lastName);

	MappingJacksonValue getEmailByCity(String city);

	void saveAll(List<Person> people);

}
