package com.safetynet.safetynetalertsapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.controller.PersonController;
import com.safetynet.safetynetalertsapi.model.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.repository.PersonRepository;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

@Service
public class PersonServiceImpl implements PersonService {

	private Logger log = LogManager.getLogger(PersonController.class);

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MedicalRecordService medicalService;

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
		return personRepository.findAllPeopleByAddressIn(adresses);
	}

	@Override
	public ChildAlertDTO getPeopleByAddress(String address) {

		List<Person> peopleFiltredByAddress = personRepository.findAllPeopleByAddress(address);
		ChildAlertDTO childDTO = new ChildAlertDTO();

		if (peopleFiltredByAddress != null && !peopleFiltredByAddress.isEmpty()) {

			List<Person> houseHoldMembers = new ArrayList<Person>();

			for (Person person : peopleFiltredByAddress) {

				Person personFound = new Person();
				personFound.setFirstName(person.getFirstName());
				personFound.setLastName(person.getLastName());
				personFound.setAddress(person.getAddress());
				personFound.setPhone(person.getPhone());
				personFound.setCity(person.getCity());
				personFound.setEmail(person.getEmail());
				personFound.setZip(person.getZip());

				MedicalRecord medicalRecordFound = medicalService.getMedicalRecordByFullName(personFound.getLastName(),
						personFound.getFirstName());

				if (medicalRecordFound != null) {
					int personsAge = SafetyAlertsNetUtil.ageCalculator(medicalRecordFound);
					if (personsAge <= 18) {
						childDTO.setFirstName(personFound.getFirstName());
						childDTO.setLastName(personFound.getLastName());
						childDTO.setAge(personsAge);
					} else {
						houseHoldMembers.add(personFound);
						childDTO.setHouseHoldMembers(houseHoldMembers);
					}
				} else {
					log.info("medical record not found");
				}
			}
			if (childDTO.getFirstName() == null) {
				log.info("No childs founded at the address: " + address);
				return null;
			} else {
				return childDTO;
			}
		}
		log.info("People not found at address: " + address);
		return null;

	}

}
