package com.safetynet.safetynetalertsapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.safetynetalertsapi.controller.PersonController;
import com.safetynet.safetynetalertsapi.service.PersonServiceImpl;

//@ExtendWith(SpringExtension.class)
//@AutoConfigureMockMvc
//@TestPropertySource("classpath:application.properties")
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	public PersonServiceImpl personService;

//	@Test
//	public void testGetPeople() throws Exception {
//
//		mockMvc.perform(get("/person/people")).andDo(print()).andExpect(status().isOk())
//				.andExpect(jsonPath("$[0].firstName", is("John")));
//
//	}

//	@Test
//	public void personCreationTest() throws Exception {
//
//		Person personToSave = new Person();
//		personToSave.setFirstName("Juan");
//		personToSave.setLastName("Fuentes");
//		personToSave.setAddress("rue test");
//		personToSave.setCity("Nantes");
//		personToSave.setEmail("email@mail.com");
//		personToSave.setPhone("012345678");
//		personToSave.setZip("75000");
//
//		when(personService.savePerson(personToSave)).thenReturn(personToSave);
//
//		mockMvc.perform(post("/person").
//				.content(toJsonString(personToSave))
//				.contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isCreated())
//				.andExpect(content().contentType("application/json;charset=UTF-8"))
//				.andExpect(jsonPath("$.firstName", is("Juan")));
//	}
//
//	private String toJsonString(final Object obj) {
//		try {
//			return new ObjectMapper().writeValueAsString(obj);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	public void createStudentCourse() throws Exception {
//	    Course mockCourse = new Course("1", "Smallest Number", "1",Arrays.asList("1", "2", "3", "4"));
//
//	    // studentService.addCourse to respond back with mockCourse
//	    Mockito.when(studentService.addCourse(Mockito.anyString(),Mockito.any(Course.class))).thenReturn(mockCourse);
//
//	    // Send course as body to /students/Student1/courses
//	    RequestBuilder requestBuilder = MockMvcRequestBuilders
//	            .post("/students/Student1/courses")
//	            .accept(MediaType.APPLICATION_JSON)
//	            .content(exampleCourseJson)
//	            .contentType(MediaType.APPLICATION_JSON);
//
//	    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//	    MockHttpServletResponse response = result.getResponse();
//
//	    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
//
//	    assertEquals("http://localhost/students/Student1/courses/1",response.getHeader(HttpHeaders.LOCATION));
//	}
}
