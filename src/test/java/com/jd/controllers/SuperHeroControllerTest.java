package com.jd.controllers;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class SuperHeroControllerTest {
	RestTemplate template = new TestRestTemplate();

	@Test
	public void testRequest() throws Exception {
		HttpHeaders headers = template.getForEntity("http://localhost:8080", String.class).getHeaders();
		// assertThat(headers.getLocation().toString(),
		// containsString("Batman"));
	}
}
