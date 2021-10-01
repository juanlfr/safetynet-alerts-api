package com.safetynet.safetynetalertsapi;

import java.io.InputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalertsapi.model.Data;

//@Profile("!test")
@SpringBootApplication
public class SafetynetAlertsApiApplication implements CommandLineRunner {
//
//	@Autowired
//	private PersonServiceImpl personService;
//
//	@Autowired
//	private MedicalRecordServiceImpl medicalRecordService;
//
//	@Autowired
//	private FireStationServiceImpl fireStationService;

	public static void main(String[] args) {
		SpringApplication.run(SafetynetAlertsApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		TypeReference<Data> typeReference = new TypeReference<Data>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");

		Data dataFromJsonFile = mapper.readValue(inputStream, typeReference);

//		List<FireStation> fireStations = dataFromJsonFile.getFireStations();
//		fireStationService.saveAll(fireStations);
//		List<Person> people = dataFromJsonFile.getPersons();
//		personService.saveAll(people);
//		List<MedicalRecord> medicalRecords = dataFromJsonFile.getMedicalrecords();
//		medicalRecordService.saveAll(medicalRecords);
//		System.out.println(dataFromJsonFile);
	}

}
