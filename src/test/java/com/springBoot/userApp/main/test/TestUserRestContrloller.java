package com.springBoot.userApp.main.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBoot.userApp.main.model.UserModel;
import com.springBoot.userApp.main.rest.UserRestContrloller;
import com.springBoot.userApp.main.service.UserService;

@WebMvcTest(value = UserRestContrloller.class)
public class TestUserRestContrloller {
	@MockBean
	private UserService service;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSaveUser() throws Exception {
		when(service.saveData(ArgumentMatchers.any())).thenReturn(true);

		UserModel userModel = new UserModel("Nagpur", "India", "10/10/2020", "Radhey", "male", "Maharashtra",
				"r@gmail.com", "Garode", "9096852147", "445896", "10/10/2020");

		ObjectMapper mapper = new ObjectMapper();
		String userJSON = mapper.writeValueAsString(userModel);

		MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.post("/save")
				.contentType(MediaType.APPLICATION_JSON).content(userJSON);

		ResultActions perform = mockMvc.perform(reqBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(201, status);
	}
}
