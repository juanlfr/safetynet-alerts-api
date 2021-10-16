package com.safetynet.safetynetalertsapi;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc

public class SafetyNetAlertIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void childAlertTest() throws Exception {

		this.mockMvc.perform(get("http://localhost:8080/person/childAlert?address=1509 Culver St"))
				.andDo(print())
				.andExpect(jsonPath("$.childs[0].firstName", is("Tenley")))
				.andExpect(status().isOk());
	}

	@Test
	public void getPeopleByNameTest() throws Exception {
		this.mockMvc.perform(get("/person/personInfo?firstName=Reginold&lastName=Walker"))
				.andDo(print())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].firstName", is("Reginold")))
				.andExpect(jsonPath("$[0].medications[0]", is("thradox:700mg")))
				.andExpect(status().isOk());
	}

	@Test
	public void coveredPeopleByStationTest() throws Exception {

		this.mockMvc.perform(get("/firestation?stationNumber=1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numberOfChildren", is(1)));
	}

	@Test
	public void fireAlertTest() throws Exception {

		this.mockMvc.perform(get("/firestation/fire?address=29 15th St"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firestationNumber", is("2")))
				.andExpect(jsonPath("$.personInfoInFireOrFloodDTO").isArray())
				.andExpect(jsonPath("$.personInfoInFireOrFloodDTO", Matchers.hasSize(5)));
	}

	@Test
	public void emailsFromCityTest() throws Exception {

		this.mockMvc.perform(get("/person/communityEmail?city=Culver"))
				.andDo(print())
				.andExpect(jsonPath("$").isArray())
				.andExpect(status().isOk());
	}

	@Test
	public void floodAlertTest() throws Exception {

		this.mockMvc.perform(get("/firestation/flood/stations?stations=1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].personInfoInFireOrFloodDTO", Matchers.hasSize(1)));
	}

	@Test
	public void phoneAlertTest() throws Exception {

		this.mockMvc.perform(get("/firestation/phoneAlert?firestationNumber=1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].phone", is("841-874-6512")));
	}
}
