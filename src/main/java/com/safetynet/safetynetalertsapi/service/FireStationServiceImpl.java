package com.safetynet.safetynetalertsapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.controller.FireStationController;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.FireDTO;
import com.safetynet.safetynetalertsapi.model.DTO.FireFloodDTO;
import com.safetynet.safetynetalertsapi.model.DTO.FloodDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonCoveredByStationNumberDTO;
import com.safetynet.safetynetalertsapi.model.DTO.StationNumberDTO;
import com.safetynet.safetynetalertsapi.repository.FireStationRepository;
import com.safetynet.safetynetalertsapi.repository.PersonRepository;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

@Service
public class FireStationServiceImpl implements FireStationService {

	private Logger log = LogManager.getLogger(FireStationController.class);

	@Autowired
	private FireStationRepository fireStationRepository;
	@Autowired
	private PersonService personService;
	@Autowired
	private MedicalRecordService medicalService;
	@Autowired
	private PersonRepository personRepository;

	@Override
	public Optional<FireStation> getFireStation(String id) {
		return fireStationRepository.findById(id);
	}

	@Override
	public List<FireStation> getFireStations() {
		return fireStationRepository.findAll();
	}

	@Override
	public FireStation saveFireStation(FireStation fireStation) {
		return fireStationRepository.save(fireStation);
	}

	@Override
	public void deleteFireStation(String id) {
		fireStationRepository.deleteById(id);

	}

	@Override
	public StationNumberDTO getPeopleCoveredByStationNumber(String stationNumber) {

		List<Person> peopleFiltred = getPeopleByFirestationNumber(stationNumber);

		List<MedicalRecord> medicalRecordsChildren = new ArrayList<MedicalRecord>();
		StationNumberDTO returnedPeopleDTOList = new StationNumberDTO();
		List<PersonCoveredByStationNumberDTO> peopleCoveredByStationNumber = new ArrayList<PersonCoveredByStationNumberDTO>();

		for (Person person : peopleFiltred) {
			PersonCoveredByStationNumberDTO personCoveredByStationNumber = new PersonCoveredByStationNumberDTO();
			personCoveredByStationNumber.setAddress(person.getAddress());
			personCoveredByStationNumber.setPhone(person.getPhone());
			personCoveredByStationNumber.setFirstName(person.getFirstName());
			personCoveredByStationNumber.setLastName(person.getLastName());
			peopleCoveredByStationNumber.add(personCoveredByStationNumber);

			MedicalRecord medicalRecordFound = medicalService.getMedicalRecordByFullName(person.getLastName(),
					person.getFirstName());
			if (medicalRecordFound != null) {

				int personsAge = SafetyAlertsNetUtil.ageCalculator(medicalRecordFound);
				if (personsAge < 18) {
					medicalRecordsChildren.add(medicalRecordFound);
				}
			}
		}
		int numberOfChildren = medicalRecordsChildren.size();
		int totalPeopleFound = peopleFiltred.size();
		returnedPeopleDTOList.setPeopleCoveredByStationNumberDTO(peopleCoveredByStationNumber);
		returnedPeopleDTOList.setNumberOfChildren(numberOfChildren);
		returnedPeopleDTOList.setNumberOfAdults(totalPeopleFound - numberOfChildren);

		return returnedPeopleDTOList;

	}

	@Override
	public MappingJacksonValue getPhoneNumbersByFireStationNumber(String stationNumber) throws JsonProcessingException {

		List<Person> peopleFiltred = getPeopleByFirestationNumber(stationNumber);

		if (peopleFiltred != null && !peopleFiltred.isEmpty()) {

			SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.filterOutAllExcept("phone");

			FilterProvider filters = new SimpleFilterProvider().addFilter("FiltreDynamique", myFilter)
					.setFailOnUnknownId(false);

			MappingJacksonValue filtredProp = new MappingJacksonValue(peopleFiltred);

			filtredProp.setFilters(filters);

			return filtredProp;
		}

		return null;

	}

	private List<Person> getPeopleByFirestationNumber(String stationNumber) {
		List<FireStation> fireStationsFiltred = fireStationRepository.findAllFireStationsByStationNumber(stationNumber);
		List<String> adresses = new ArrayList<String>();
		for (FireStation fireStation : fireStationsFiltred) {
			adresses.add(fireStation.getAdresse());
		}
		List<Person> peopleFiltred = personService.getPeopleByAddress(adresses);
		return peopleFiltred;
	}

	@Override
	public FireDTO getPeopleAndFirestationNumbersByAddress(String address) {

		FireStation firestation = fireStationRepository.findFireStationByAdresse(address);

		log.debug("firestation " + firestation);

		if (firestation != null) {
			FireDTO fireDTO = new FireDTO();
			fireDTO.setFirestationNumber(firestation.getStation());

			List<FireFloodDTO> peopleInFireAndFlood = new ArrayList<FireFloodDTO>();

			List<Person> people = getPeopleByFirestationNumber(firestation.getStation());

			for (Person person : people) {
				FireFloodDTO fireFloodDTO = new FireFloodDTO();
				personTofireFloodDTO(fireFloodDTO, person);
				peopleInFireAndFlood.add(fireFloodDTO);
			}
			fireDTO.setPersonInfoInFireOrFloodDTO(peopleInFireAndFlood);
			return fireDTO;
		} else {
			log.info("Firestation not found");
			return null;
		}
	}

	private void personTofireFloodDTO(FireFloodDTO fireFloodDTO, Person person) {

		fireFloodDTO.setFirstName(person.getFirstName());
		fireFloodDTO.setLastName(person.getLastName());
		fireFloodDTO.setPhone(person.getPhone());

		MedicalRecord medicalRecordFound = medicalService.getMedicalRecordByFullName(person.getLastName(),
				person.getFirstName());
		if (medicalRecordFound != null) {
			fireFloodDTO.setMedications(medicalRecordFound.getMedications());
			fireFloodDTO.setAllergies(medicalRecordFound.getAllergies());
			fireFloodDTO.setAge(SafetyAlertsNetUtil.ageCalculator(medicalRecordFound));
		}
	}

	@Override
	public List<FloodDTO> getPeopleByStationsNumbers(List<String> stations) {

		List<FloodDTO> floodDTOlist = new ArrayList<FloodDTO>();

		for (String stationNumber : stations) {
			List<FireStation> fireStationsFiltred = fireStationRepository
					.findAllFireStationsByStationNumber(stationNumber);
			for (FireStation fireStation : fireStationsFiltred) {
				FloodDTO floodDTO = new FloodDTO();
				List<FireFloodDTO> fireFloodDTOlist = new ArrayList<FireFloodDTO>();
				floodDTO.setAddress(fireStation.getAdresse());
				List<Person> people = personRepository.findAllPeopleByAddress(fireStation.getAdresse());
				for (Person person : people) {
					FireFloodDTO fireFloodDTO = new FireFloodDTO();
					personTofireFloodDTO(fireFloodDTO, person);
					fireFloodDTOlist.add(fireFloodDTO);
				}
				floodDTO.setPersonInfoInFireOrFloodDTO(fireFloodDTOlist);
				floodDTOlist.add(floodDTO);
			}
		}

		return floodDTOlist;

	}
}
