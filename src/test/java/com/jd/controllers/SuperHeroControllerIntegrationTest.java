package com.jd.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.Application;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest("server.port:0")
@RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = Config.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SuperHeroControllerIntegrationTest {

	@Test
	public void testGetAllSuperHero() throws JsonProcessingException, IOException {
		RestTemplate rest = new TestRestTemplate();
		ResponseEntity<String> response = rest.getForEntity("http://locahost:8080/get-all", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode responseJson = objectMapper.readTree(response.getBody());
		JsonNode messageJson = responseJson.path("message");

		assertThat(messageJson.asText(), equalTo("test"));
		// SuperHero superHeroCreated = response.getBody();
		// assertThat(superHeroCreated.getSuperHeroId(), notNullValue());
		// assertThat(superHeroCreated.getName(), equalTo("Batman"));
	}
}
