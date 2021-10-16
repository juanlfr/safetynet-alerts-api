
package com.safetynet.safetynetalertsapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.safetynet.safetynetalertsapi.model.MedicalRecord;
import com.safetynet.safetynetalertsapi.model.Person;
import com.safetynet.safetynetalertsapi.model.DTO.ChildAlertDTO;
import com.safetynet.safetynetalertsapi.model.DTO.PersonInfoDTO;
import com.safetynet.safetynetalertsapi.repository.PersonRepository;
import com.safetynet.safetynetalertsapi.service.MedicalRecordService;
import com.safetynet.safetynetalertsapi.service.PersonServiceImpl;
import com.safetynet.safetynetalertsapi.utils.SafetyAlertsNetUtil;

@ActiveProfiles("test")
@SpringBootTest
public class PersonServiceTest {

	@MockBean
	PersonRepository personRepository;

	@MockBean
	MedicalRecordService medicalRecordService;

	@Autowired
	PersonServiceImpl personServiceImpl;

	private Person person;

	@BeforeEach
	private void setUpTest() {

		person = new Person();
		person.setFirstName("Jean");
		person.setLastName("Fuentes");
		person.setAddress("rue test");
		person.setCity("Nantes");
		person.setEmail("email@mail.com");
		person.setPhone("012345678");
		person.setZip("75000");
		person.setId("1");

	}

	@Test
	public void getPeopleByNameNullListTest() {

		when(personRepository.findAll()).thenReturn(null);

		List<PersonInfoDTO> personInfoDTO = personServiceImpl.getPeopleByName("xxx", "yyy");

		assertThat(personInfoDTO).isNullOrEmpty();

	}

	@Test
	public void getPeopleByNameReturnMoreThanOnePersonTest() {

		Person person2 = new Person();
		person2.setFirstName("Louis");
		person2.setLastName("Fuentes");
		person2.setAddress("rue test");
		person2.setCity("Nantes");
		person2.setEmail("email@mail.com");
		person2.setPhone("012345678");
		person2.setZip("75000");
		person2.setId("2");

		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setBirthdate("09/06/1990");

		List<Person> people = new ArrayList<Person>(Arrays.asList(person, person2));
		when(personRepository.findAllPeopleByLastName("Fuentes")).thenReturn(people);
		when(medicalRecordService.getMedicalRecordByFullName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(medicalRecord);

		List<PersonInfoDTO> personInfoDTO = personServiceImpl.getPeopleByName("Juan", "Fuentes");

		assertThat(personInfoDTO).hasSize(2);

	}

	@Test
	public void getPeopleByNameReturnOnePersonTest() {

		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setBirthdate("09/06/1990");

		List<Person> people = new ArrayList<Person>(Arrays.asList(person));
		when(personRepository.findAllPeopleByLastName("Fuentes")).thenReturn(people);
		when(medicalRecordService.getMedicalRecordByFullName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(medicalRecord);

		List<PersonInfoDTO> personInfoDTO = personServiceImpl.getPeopleByName("Juan", "Fuentes");

		assertThat(personInfoDTO).isNull();

	}

	@Test
	public void getPeopleByAddressNullPeopleListTest() {

		when(personRepository.findAllPeopleByAddress(Mockito.anyString())).thenReturn(null);
		ChildAlertDTO childAlertDTO = personServiceImpl.getPeopleByAddress("address");
		assertThat(childAlertDTO).isNull();
	}

	@Test
	public void getPersonTest() {

		when(personRepository.findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString())).thenReturn(person);
		Person person = personServiceImpl.getPerson("Jean Fuentes");
		String[] name = SafetyAlertsNetUtil.splitName(" Jean Fuentes ");

		assertThat(name[0]).isEqualTo("Jean");
		assertThat(name[1]).isEqualTo("Fuentes");
		assertThat(person).isEqualTo(this.person);
	}

	@Test
	public void deletePersonTest() {

		personServiceImpl.deletePerson("Jean Fuentes");

		String[] name = SafetyAlertsNetUtil.splitName(" Jean Fuentes ");

		assertThat(name[0]).isEqualTo("Jean");
		assertThat(name[1]).isEqualTo("Fuentes");
		verify(personRepository,
				Mockito.times(1)).deleteByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString());
	}
}
