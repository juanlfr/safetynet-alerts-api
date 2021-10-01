package com.safetynet.safetynetalertsapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.safetynetalertsapi.controller.MedicalRecordController;
import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.service.MedicalRecordService;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

//@ActiveProfiles("test")
@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalRecordService medicalRecordService;

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
	public void createMedicalRecordTest() throws Exception {
		when(medicalRecordService.saveMedicalRecord(Mockito.any(MedicalRecord.class))).thenReturn(medicalRecord);

		this.mockMvc.perform(post("/medicalRecord")
				.content(SafetyAlertsNetUtil.toJsonString(medicalRecord))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	public void updateMedicalRecordTest() throws Exception {

		MedicalRecord medicalRecordDataUpdated = new MedicalRecord();
		medicalRecordDataUpdated.setFirstName("test2");
		medicalRecordDataUpdated.setLastName("test2");
		medicalRecordDataUpdated.setBirthdate("01/01/1980");
		medicalRecordDataUpdated.setAllergies(new ArrayList<String>(Arrays.asList("allergie3", "allergie4")));
		medicalRecordDataUpdated.setMedications(new ArrayList<String>(Arrays.asList("medication3")));

		when(medicalRecordService.getMedicalRecord(Mockito.anyString())).thenReturn(medicalRecord);

		when(medicalRecordService.saveMedicalRecord(Mockito.any(MedicalRecord.class))).thenReturn(medicalRecord);

		this.mockMvc.perform(put("/medicalRecord/name test")
				.content(SafetyAlertsNetUtil.toJsonString(medicalRecordDataUpdated))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		assertThat(medicalRecord.getBirthdate()).isEqualTo("01/01/1980");
		assertThat(medicalRecord.getLastName()).isNotEqualTo("test2");

	}

	@Test
	public void deleteMedicalRecord() throws Exception {

		this.mockMvc.perform(delete("/medicalRecord/name test")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		verify(medicalRecordService, Mockito.times(1)).deleteMedicalRecord(Mockito.any(String.class));
	}

}
