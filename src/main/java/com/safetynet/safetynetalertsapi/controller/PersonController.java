package com.safetynet.safetynetalertsapi.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.service.PersonService;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;

	private Logger log = LogManager.getLogger(PersonController.class);

	@GetMapping("/people")
	public @ResponseBody List<Person> getPeople() {
		log.debug("*************" + "call to service" + "****************");
		return personService.getPeople();

	}

}
