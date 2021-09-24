package com.safetynet.safetynetalertsapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.safetynetalertsapi.model.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

	List<Person> findAllPeopleByAddressIn(List<String> adresses);

	List<Person> findAllPeopleByAddress(String address);

	List<Person> findAllPeopleByLastName(String lastName);

}
