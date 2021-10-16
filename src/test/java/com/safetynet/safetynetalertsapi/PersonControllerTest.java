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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.safetynet.safetynetalertsapi.controller.PersonController;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.DTO.ChildDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.service.PersonService;

@ActiveProfiles("test")
@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService personService;

	private Person person;

	@BeforeEach
	private void setUpPerTest() {
		person = new Person();
		person.setFirstName("Juan");
		person.setLastName("Fuentes");
		person.setAddress("rue test");
		person.setCity("Nantes");
		person.setEmail("email@mail.com");
		person.setPhone("012345678");
		person.setZip("75000");
		person.setId("1");
	}

	@Test
	public void personCreationTest() throws Exception {

		when(personService.savePerson(Mockito.any(Person.class))).thenReturn(person);

		this.mockMvc.perform(post("/person")
				.content(toJsonString(person))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(redirectedUrl("http://localhost/person/1"));

	}

	@Test
	public void updatePersonTest() throws Exception {

		Person personUpdatedData = new Person();
		personUpdatedData.setFirstName("Juan");
		personUpdatedData.setLastName("Rojas");
		personUpdatedData.setAddress("Another rue");
		personUpdatedData.setCity("Rennes");
		personUpdatedData.setEmail("anothermail@mail.com");
		personUpdatedData.setPhone("0180000000");
		personUpdatedData.setZip("35000");

		when(personService.getPerson(Mockito.any(String.class))).thenReturn(person);
		when(personService.savePerson(Mockito.any(Person.class))).thenReturn(person);

		this.mockMvc.perform(put("/person/test Name")
				.content(toJsonString(personUpdatedData))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		assertThat(person.getAddress()).isEqualTo("Another rue");
		assertThat(person.getCity()).isEqualTo("Rennes");
		assertThat(person.getLastName()).isEqualTo("Fuentes");
	}

	@Test
	public void deletePersonTest() throws Exception {

		this.mockMvc.perform(delete("/person/name test")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		verify(personService, Mockito.times(1)).deletePerson(Mockito.any(String.class));

	}

	@Test
	public void getChildByAddressTest() throws Exception {

		ChildDTO child = new ChildDTO();
		child.setAge(3);
		child.setFirstName("Marie");
		child.setLastName("Le Coq");

		ChildDTO child2 = new ChildDTO();
		child.setAge(4);
		child.setFirstName("Pierre");
		child.setLastName("Le Foll");

		ArrayList<ChildDTO> childs = new ArrayList<ChildDTO>();
		childs.add(child);
		childs.add(child2);

		List<Person> houseHoldMembers = new ArrayList<Person>();
		houseHoldMembers.add(person);

		ChildAlertDTO childAlertDTO = new ChildAlertDTO();
		childAlertDTO.setChilds(childs);
		childAlertDTO.setHouseHoldMembers(houseHoldMembers);

		when(personService.getPeopleByAddress(Mockito.any(String.class))).thenReturn(childAlertDTO);

		this.mockMvc.perform(get("/person/childAlert?address=123 street test"))
				.andDo(print())
				.andExpect(jsonPath("$.childs").isArray())
				.andExpect(jsonPath("$.houseHoldMembers[0].firstName", is("Juan")))
				.andExpect(status().isOk());

	}

	@Test
	public void getPeopleByNameTest() throws Exception {

		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<PersonInfoDTO>();
		ArrayList<String> medications = new ArrayList<String>(Arrays.asList("Paracetamol", "Naproxen"));
		ArrayList<String> allergies = new ArrayList<String>(Arrays.asList("Arachides"));

		PersonInfoDTO personInfoDto = new PersonInfoDTO(
				person.getFirstName(), person.getLastName(), person.getAddress(),
				30, person.getEmail(), medications, allergies);

		listPersonInfoDTO.add(personInfoDto);

		when(personService.getPeopleByName(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(listPersonInfoDTO);

		this.mockMvc.perform(get("/person/personInfo?firstName=Juan&lastName=Fuentes"))
				.andDo(print())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].firstName", is("Juan")))
				.andExpect(jsonPath("$[0].medications[0]", is("Paracetamol")))
				.andExpect(status().isOk());
	}

	@Test
	public void getEmailByCityTest() throws Exception {

		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(person);

		SimpleBeanPropertyFilter myFilter = SimpleBeanPropertyFilter.filterOutAllExcept("email");

		FilterProvider filters = new SimpleFilterProvider().addFilter("FiltreDynamique", myFilter)
				.setFailOnUnknownId(false);

		mappingJacksonValue.setFilters(filters);

		when(personService.getEmailByCity(Mockito.anyString())).thenReturn(mappingJacksonValue);

		this.mockMvc.perform(get("/person/communityEmail?city=Vezin"))
				.andDo(print())
				.andExpect(jsonPath("$.email", is("email@mail.com")))
				.andExpect(status().isOk());
	}

	private String toJsonString(final Object obj) {
		try {
			return new ObjectMapper().setFilterProvider(new SimpleFilterProvider()
					.setFailOnUnknownId(false)).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
