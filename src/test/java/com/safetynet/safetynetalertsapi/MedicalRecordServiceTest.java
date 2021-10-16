package com.safetynet.safetynetalertsapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.repository.MedicalRecordRepository;
import com.safetynet.safetynetalertsapi.service.MedicalRecordServiceImpl;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

@SpringBootTest
public class MedicalRecordServiceTest {

	@Autowired
	private MedicalRecordServiceImpl medicalRecordService;
	@MockBean
	private MedicalRecordRepository medicalRecordRepository;

	private MedicalRecord medicalRecord;

	@BeforeEach
	private void setUpPerTest() {
		medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName("test");
		medicalRecord.setLastName("test");
		medicalRecord.setBirthdate("03/06/1984");
		medicalRecord.setAllergies(new ArrayList<String>(Arrays.asList("allergie1", "allergie2")));
		medicalRecord.setMedications(new ArrayList<String>(Arrays.asList("medication1", "medication2")));
	}

	@Test
	public void getMedicalRecord() {
		when(medicalRecordRepository.findByLastNameAndFirstName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(medicalRecord);
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord("Jean Fuentes");
		String[] name = SafetyAlertsNetUtil.splitName(" Jean Fuentes ");

		assertThat(name[0]).isEqualTo("Jean");
		assertThat(name[1]).isEqualTo("Fuentes");
		assertThat(medicalRecord).isEqualTo(this.medicalRecord);
	}

	@Test
	public void deletePersonTest() {

		medicalRecordService.deleteMedicalRecord("Jean Fuentes");

		String[] name = SafetyAlertsNetUtil.splitName(" Jean Fuentes ");

		assertThat(name[0]).isEqualTo("Jean");
		assertThat(name[1]).isEqualTo("Fuentes");
		verify(medicalRecordRepository,
				Mockito.times(1)).deleteByLastNameAndFirstName(Mockito.anyString(), Mockito.anyString());
	}
}
