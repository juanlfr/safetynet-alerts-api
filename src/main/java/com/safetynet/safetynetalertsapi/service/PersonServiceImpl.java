package com.safetynet.safetynetalertsapi.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.controller.PersonController;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.DTO.ChildDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonInfoDTO;
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
	public Person getPerson(String fullName) {
		String[] name = fullName.trim().split("\\s+");
		System.out.println(name[0] + name[1]);
		return personRepository.findByFirstNameAndLastName(name[0], name[1]);

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
	public void deletePerson(String fullName) {
		String[] name = fullName.trim().split("\\s+");
		System.out.println(name[0] + name[1]);
		personRepository.deleteByFirstNameAndLastName(name[0], name[1]);
	}

	@Override
	public List<Person> getPeopleByAddress(List<String> adresses) {
		return personRepository.findAllPeopleByAddressIn(adresses);
	}

	@Override
	public ChildAlertDTO getPeopleByAddress(String address) {

		List<Person> peopleFiltredByAddress = personRepository.findAllPeopleByAddress(address);
		ChildAlertDTO childAlertDTO = new ChildAlertDTO();
		List<ChildDTO> childs = new ArrayList<ChildDTO>();

		if (peopleFiltredByAddress != null && !peopleFiltredByAddress.isEmpty()) {

			List<Person> houseHoldMembers = new ArrayList<Person>();

			for (Person person : peopleFiltredByAddress) {

				MedicalRecord medicalRecordFound = medicalService.getMedicalRecordByFullName(person.getLastName(),
						person.getFirstName());

				if (medicalRecordFound != null) {
					int personsAge = SafetyAlertsNetUtil.ageCalculator(medicalRecordFound);
					if (personsAge <= 18) {
						ChildDTO childDTO = new ChildDTO();
						childDTO.setFirstName(person.getFirstName());
						childDTO.setLastName(person.getLastName());
						childDTO.setAge(personsAge);
						childs.add(childDTO);
					} else {
						Person personFound = new Person();
						personFound.setFirstName(person.getFirstName());
						personFound.setLastName(person.getLastName());
						personFound.setAddress(person.getAddress());
						personFound.setPhone(person.getPhone());
						personFound.setCity(person.getCity());
						personFound.setEmail(person.getEmail());
						personFound.setZip(person.getZip());
						houseHoldMembers.add(personFound);
					}
				} else {
					log.info("medical record not found");
				}
			}
			childAlertDTO.setChilds(childs);
			childAlertDTO.setHouseHoldMembers(houseHoldMembers);
			if (childs.isEmpty()) {
				log.info("No childs founded at the address: " + address);
				return null;
			} else {
				return childAlertDTO;
			}
		}
		log.info("People not found at address: " + address);
		return null;

	}

	@Override
	public List<PersonInfoDTO> getPeopleByName(String firstName, String lastName) {

		List<Person> people = personRepository.findAllPeopleByLastName(lastName);
		if (people != null && !people.isEmpty()) {
			List<PersonInfoDTO> personInfoDTOList = new ArrayList<PersonInfoDTO>();
			if (people.size() > 1) {
				for (Person person : people) {
					personToPersonInfoDTO(personInfoDTOList, person);
				}
				return personInfoDTOList;
			} else {
				if (people.get(0).getFirstName().equals(firstName)) {
					personToPersonInfoDTO(personInfoDTOList, people.get(0));
					return personInfoDTOList;
				} else {
					log.info("person named: " + firstName + " " + lastName + " not found");
					return null;
				}
			}
		} else {
			return null;
		}
	}

	@Override
	public MappingJacksonValue getEmailByCity(String city) {
		List<Person> peopleFiltred = personRepository.findAllPeopleByCity(city);
		if (peopleFiltred != null && !peopleFiltred.isEmpty()) {

			SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.filterOutAllExcept("email");

			FilterProvider filters = new SimpleFilterProvider().addFilter("FiltreDynamique", myFilter)
					.setFailOnUnknownId(false);

			MappingJacksonValue filtredProp = new MappingJacksonValue(peopleFiltred);

			filtredProp.setFilters(filters);

			return filtredProp;
		}

		return null;
	}

	private void personToPersonInfoDTO(List<PersonInfoDTO> personInfoDTOList, Person person) {
		PersonInfoDTO personInfoDTO = new PersonInfoDTO();
		personInfoDTO.setFirstName(person.getFirstName());
		personInfoDTO.setLastName(person.getLastName());
		MedicalRecord medicalRecordFound = medicalService.getMedicalRecordByFullName(person.getLastName(),
				person.getFirstName());
		personInfoDTO.setAddress(person.getAddress());
		personInfoDTO.setEmail(person.getEmail());
		personInfoDTO.setMedications(medicalRecordFound.getMedications());
		personInfoDTO.setAllergies(medicalRecordFound.getAllergies());
		personInfoDTO.setAge(SafetyAlertsNetUtil.ageCalculator(medicalRecordFound));
		personInfoDTOList.add(personInfoDTO);
	}

	@Override
	public void saveAll(List<Person> people) {
		personRepository.saveAll(people);

	}

}
