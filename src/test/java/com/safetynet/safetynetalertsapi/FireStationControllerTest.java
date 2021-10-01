package com.safetynet.safetynetalertsapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.controller.FireStationController;
import com.safetynet.safetynetalertsapi.model.FireStation;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.FireDTO;
import com.safetynet.safetynetalertsapi.model.DTO.FireFloodDTO;
import com.safetynet.safetynetalertsapi.model.DTO.FloodDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonCoveredByStationNumberDTO;
import com.safetynet.safetynetalertsapi.model.DTO.StationNumberDTO;
import com.safetynet.safetynetalertsapi.service.FireStationService;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

//@ActiveProfiles("test")
@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FireStationService fireStationService;

	private FireStation fireStation;

	@BeforeEach
	private void setUpPerTest() {
		fireStation = new FireStation();
		fireStation.setAdresse("1 test st");
		fireStation.setStation("1");
	}

	@Test
	public void createfireStationTest() throws Exception {

		when(fireStationService.saveFireStation(Mockito.any(FireStation.class))).thenReturn(fireStation);

		this.mockMvc.perform(post("/firestation")
				.content(SafetyAlertsNetUtil.toJsonString(fireStation))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	public void updateFireStationTest() throws Exception {

		FireStation fireStationDataToUpdate = new FireStation();
		fireStationDataToUpdate.setStation("1");

		when(fireStationService.getFireStation(Mockito.anyString())).thenReturn(fireStation);
		when(fireStationService.saveFireStation(Mockito.any(FireStation.class))).thenReturn(fireStation);

		this.mockMvc.perform(put("/firestation/1 test st")
				.content(SafetyAlertsNetUtil.toJsonString(fireStationDataToUpdate))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		assertThat(fireStation.getStation()).isEqualTo("1");
	}

	@Test
	public void deleteFireStationTest() throws Exception {

		this.mockMvc.perform(delete("/firestation/address test")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		verify(fireStationService, Mockito.times(1)).deleteFireStation(Mockito.any(String.class));
	}

	@Test
	public void getPeopleCoveredByStationNumber() throws Exception {

		StationNumberDTO dto = new StationNumberDTO();
		dto.setNumberOfAdults(2);
		dto.setNumberOfChildren(1);
		List<PersonCoveredByStationNumberDTO> peopleCoveredByStationNumber = new ArrayList<PersonCoveredByStationNumberDTO>();
		PersonCoveredByStationNumberDTO person = new PersonCoveredByStationNumberDTO();
		person.setAddress("Address test");
		person.setFirstName("Test Firstname");
		person.setLastName("Test Lastname");
		person.setPhone("0123456789");
		peopleCoveredByStationNumber.add(person);
		dto.setPeopleCoveredByStationNumberDTO(peopleCoveredByStationNumber);

		when(fireStationService.getPeopleCoveredByStationNumber(Mockito.anyString())).thenReturn(dto);

		this.mockMvc.perform(get("/firestation?stationNumber=1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numberOfChildren", is(1)));
	}

	@Test
	public void getPhoneNumbersByFireStationNumberTest() throws Exception {

		Person person = new Person();
		person.setFirstName("Juan");
		person.setLastName("Fuentes");
		person.setAddress("rue test");
		person.setCity("Nantes");
		person.setEmail("email@mail.com");
		person.setPhone("012345678");
		person.setZip("75000");
		person.setId("1");

		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(person);

		SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.filterOutAllExcept("phone");

		FilterProvider filters = new SimpleFilterProvider().addFilter("FiltreDynamique", myFilter)
				.setFailOnUnknownId(false);

		mappingJacksonValue.setFilters(filters);
		when(fireStationService.getPhoneNumbersByFireStationNumber(Mockito.anyString()))
				.thenReturn(mappingJacksonValue);

		this.mockMvc.perform(get("/firestation/phoneAlert?firestationNumber=2"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.phone", is("012345678")));
	}

	@Test
	public void getPeopleAndFirestationNumbersByAddressTest() throws Exception {

		FireDTO fireDto = new FireDTO();
		fireDto.setFirestationNumber("1");
		List<FireFloodDTO> peopleInFireAndFlood = setFireFloodTestValues();

		fireDto.setPersonInfoInFireOrFloodDTO(peopleInFireAndFlood);

		when(fireStationService.getPeopleAndFirestationNumbersByAddress(Mockito.anyString())).thenReturn(fireDto);

		this.mockMvc.perform(get("/firestation/fire?address=1 test St"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firestationNumber", is("1")))
				.andExpect(jsonPath("$.personInfoInFireOrFloodDTO").isArray())
				.andExpect(jsonPath("$.personInfoInFireOrFloodDTO", Matchers.hasSize(1)));
	}

	@Test
	public void getPeopleByStationsNumbersTest() throws Exception {

		List<FloodDTO> returnedLisDTO = new ArrayList<FloodDTO>();
		FloodDTO floodDTO = new FloodDTO();
		List<FireFloodDTO> peopleInFireAndFlood = setFireFloodTestValues();

		floodDTO.setAddress("test address");
		floodDTO.setPersonInfoInFireOrFloodDTO(peopleInFireAndFlood);
		returnedLisDTO.add(floodDTO);

		when(fireStationService.getPeopleByStationsNumbers(Mockito.anyList())).thenReturn(returnedLisDTO);

		this.mockMvc.perform(get("/firestation/flood/stations?stations=1,2"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].personInfoInFireOrFloodDTO", Matchers.hasSize(1)));
	}

	private List<FireFloodDTO> setFireFloodTestValues() {
		List<FireFloodDTO> peopleInFireAndFlood = new ArrayList<FireFloodDTO>();
		FireFloodDTO fireFloodDTO = new FireFloodDTO();
		fireFloodDTO.setAge(25);
		fireFloodDTO.setAllergies(new ArrayList<String>(Arrays.asList("allergie1", "allergie2")));
		fireFloodDTO.setMedications(new ArrayList<String>(Arrays.asList("medication1", "medication2")));
		fireFloodDTO.setFirstName("Test");
		fireFloodDTO.setLastName("Test");
		fireFloodDTO.setPhone("0123456789");
		peopleInFireAndFlood.add(fireFloodDTO);
		return peopleInFireAndFlood;
	}

}
