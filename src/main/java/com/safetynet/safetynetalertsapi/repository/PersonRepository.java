package com.safetynet.safetynetalertsapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.safetynetalertsapi.model.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

}
