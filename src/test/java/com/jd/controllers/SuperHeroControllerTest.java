package com.jd.controllers;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.jayway.restassured.config.Config;
import com.jd.models.SuperHero;
import com.jd.models.SuperHeroDao;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest("server.port:0")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
@WebAppConfiguration
@IntegrationTest
public class SuperHeroControllerTest {

	@Autowired
	private SuperHeroDao superHeroDao;

	SuperHero batMan;
	SuperHero spiderMan;
	SuperHero superMan;

	@Value("${local.server.port}")
	int port;

	// @Before
	// public void setUp() {
	// batMan = new SuperHero("Batman");
	// spiderMan = new SuperHero("Spiderman");
	// superMan = new SuperHero("Superman");
	//
	// superHeroDao.deleteAll();
	// superHeroDao.save(Arrays.asList(batMan, spiderMan, superMan));
	//
	// RestAssured.port = port;
	// }

	@Test
	public void canFetchBatman() {
		long batManId = batMan.getId();

		// when().get("/get-by-name/{id}",
		// batManId).then().statusCode(HttpStatus.SC_OK)
		// .body("name", Matchers.is("Mickey Mouse")).body("id", Matchers.is(batManId));
	}

	RestTemplate template = new TestRestTemplate();

	@Test
	public void testRequest() throws Exception {
		HttpHeaders headers = template.getForEntity("http://localhost:8080", String.class).getHeaders();
		// assertThat(headers.getLocation().toString(),
		// containsString("Batman"));
	}

	@Test
	public void shouldCreateSuperHero() {
		SuperHero superHero = new SuperHero("Batman");
		RestTemplate rest = new TestRestTemplate();
		ResponseEntity<SuperHero> response = rest.postForEntity("http://locahost:8080/create", superHero, SuperHero.class,
				Collections.EMPTY_MAP);
		// assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
		//
		// SuperHero superHeroCreated = response.getBody();
		// assertThat(superHeroCreated.getId(), notNullValue());
		// assertThat(superHeroCreated.getName(), equalTo("Batman"));
	}
}
