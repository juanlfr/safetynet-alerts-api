package com.safetynet.safetynetalertsapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.repository.FireStationRepository;

@Service
public class FireStationService {

	@Autowired
	private FireStationRepository fireStationRepository;

	public Optional<FireStation> getFireStation(final String id) {
		return fireStationRepository.findById(id);
	}

	public List<FireStation> getFireStations() {
		return fireStationRepository.findAll();
	}

	public FireStation saveFireStation(FireStation fireStation) {
		return fireStationRepository.save(fireStation);
	}

	public void deleteFireStation(final String id) {
		fireStationRepository.deleteById(id);
	}

}
