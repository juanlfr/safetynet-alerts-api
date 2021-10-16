package com.safetynet.safetynetalertsapi;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalertsapi.model.Data;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.service.FireStationService;
import com.safetynet.safetynetalertsapi.service.MedicalRecordService;
import com.safetynet.safetynetalertsapi.service.PersonService;

@Profile("!test")
@Component
public class CommandLineSaveDBExecutor implements CommandLineRunner {

	@Autowired
	private PersonService personService;

	@Autowired
	private MedicalRecordService medicalRecordService;

	@Autowired
	private FireStationService fireStationService;

	@Override
	public void run(String... args) throws Exception {

		List<Person> peopleFromDB = personService.findAll();
		List<FireStation> fireStationsFromDB = fireStationService.findAll();
		List<MedicalRecord> medicalRecordsFromDB = medicalRecordService.findAll();

		if (peopleFromDB.isEmpty() && fireStationsFromDB.isEmpty() && medicalRecordsFromDB.isEmpty()) {

			ObjectMapper mapper = new ObjectMapper();

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			TypeReference<Data> typeReference = new TypeReference<Data>() {
			};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");

			Data dataFromJsonFile = mapper.readValue(inputStream, typeReference);

			List<FireStation> fireStations = dataFromJsonFile.getFireStations();
			fireStationService.saveAll(fireStations);
			List<Person> people = dataFromJsonFile.getPersons();
			personService.saveAll(people);
			List<MedicalRecord> medicalRecords = dataFromJsonFile.getMedicalrecords();
			medicalRecordService.saveAll(medicalRecords);

			System.out.println("Saving data to data base" + dataFromJsonFile);

		}

	}

}
