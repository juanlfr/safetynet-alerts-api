package com.safetynet.safetynetalertsapi.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.PersonCoveredByStationNumberDTO;
import com.safetynet.safetynetalertsapi.model.StationNumberDTO;
import com.safetynet.safetynetalertsapi.repository.FireStationRepository;

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

		List<FireStation> fireStationsFiltred = fireStationRepository.findAllFireStationsByStationNumber(stationNumber);
		List<String> adresses = new ArrayList<String>();

		for (FireStation fireStation : fireStationsFiltred) {
			adresses.add(fireStation.getAdresse());
		}
		List<Person> peopleFiltred = personService.getPeopleByAddress(adresses);
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

				int personsAge = ageCalculator(medicalRecordFound);
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

//			peopleFiltred.stream()
//			.filter(p -> p.getLastName())
		// .map(MedicalRecordService::getMedicalRecordByFullName(person.getLastName(),
		// person.getFirstName()));

		// List<StationNumberDTO>

		// List<People>
		// medicalService.getPeopleByZone(peopleFiltred)
		// return peopleFiltred;
		return returnedPeopleDTOList;

	}

	private static int ageCalculator(MedicalRecord medicalRecordFound) {
		String stringBirthDate = medicalRecordFound.getBirthdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(stringBirthDate, formatter);
		int personsAge = Period.between(birthDate, LocalDate.now()).getYears();
		return personsAge;
	}

}
