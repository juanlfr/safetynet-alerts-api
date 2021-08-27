package com.safetynet.safetynetalertsapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.repository.FireStationRepository;

@Service
public class FireStationServiceImpl implements FireStationService {

	@Autowired
	private FireStationRepository fireStationRepository;
	@Autowired
	private PersonService personService;

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
	public List<FireStation> getPeopleCoveredByStationNumber(String stationNumber) {

		List<FireStation> fireStationsFiltred = fireStationRepository.findAllFireStationsByStationNumber(stationNumber);
		List<String> adresses = new ArrayList<String>();
		for (FireStation fireStation : fireStationsFiltred) {
			adresses.add(fireStation.getAdresse());
		}
		List<Person> peopleFiltred = personService.getPeopleByAddress(adresses);
		return fireStationsFiltred;

	}

}
