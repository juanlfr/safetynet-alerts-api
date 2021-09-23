package com.safetynet.safetynetalertsapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.PersonCoveredByStationNumberDTO;
import com.safetynet.safetynetalertsapi.model.StationNumberDTO;
import com.safetynet.safetynetalertsapi.repository.FireStationRepository;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

@Service
public class FireStationServiceImpl implements FireStationService {

	@Autowired
	private FireStationRepository fireStationRepository;
	@Autowired
	private PersonService personService;
	@Autowired
	private MedicalRecordService medicalService;

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
	public String getPhoneNumbersByFireStationNumber(String stationNumber) throws JsonProcessingException {

		List<Person> peopleFiltred = getPeopleByFirestationNumber(stationNumber);

		if (peopleFiltred != null && !peopleFiltred.isEmpty()) {

			SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.filterOutAllExcept("phone");

			FilterProvider filters = new SimpleFilterProvider().addFilter("FiltreDynamique", myFilter)
					.setFailOnUnknownId(false);

			ObjectMapper mapper = new ObjectMapper();

			mapper.setFilterProvider(filters);

			String jsonData = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(peopleFiltred);

			return jsonData;
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

}
