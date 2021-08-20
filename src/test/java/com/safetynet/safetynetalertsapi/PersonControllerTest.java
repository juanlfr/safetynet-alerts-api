package com.safetynet.safetynetalertsapi;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class PersonControllerTest {

	@Autowired
	public MockMvc mockMvc;
	// @Autowired
	// private WebApplicationContext webApplicationContext;
//	@Autowired
//	public PersonController personController;

//	@BeforeEach
//	public void setUp() {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
//	}

	@Test
	public void testGetPeople() throws Exception {

		mockMvc.perform(get("/people")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName", is("John")));

	}

}
