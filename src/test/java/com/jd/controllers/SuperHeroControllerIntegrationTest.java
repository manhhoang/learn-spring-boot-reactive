package com.jd.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SuperHeroControllerIntegrationTest {

	@Test
	public void testFailAuthentication() {
		RestTemplate rest = new TestRestTemplate();
		ResponseEntity<String> response = rest.getForEntity("http://localhost:8888/get-all", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
	}

	@Test
	public void testGetAllSuperHeroButEmpty() throws JsonProcessingException, IOException {
		RestTemplate rest = new TestRestTemplate();

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials("test", "password"));
		HttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

		ResponseEntity<String> response = rest.getForEntity("http://localhost:8888/get-all", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode responseJson = objectMapper.readTree(response.getBody());
		assertThat(responseJson.asText(), equalTo(""));
		// SuperHero superHeroCreated = response.getBody();
		// assertThat(superHeroCreated.getSuperHeroId(), notNullValue());
		// assertThat(superHeroCreated.getName(), equalTo("Batman"));
	}

}
