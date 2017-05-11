package com.jd.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.models.Hero;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeroControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Before
  public void setupMock() {

  }

  @Test
  public void testGetAllHeroButEmpty() throws IOException {
    ResponseEntity<String> response = restTemplate.getForEntity("/heroes", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode responseJson = objectMapper.readTree(response.getBody());
    assertThat(responseJson.asText(), equalTo(""));
  }

  @Test
  public void testCreateSuperHeroSuccess() throws IOException {
    ResponseEntity<Hero> response = restTemplate.postForEntity("/hero/create","{1,2,3}", Hero.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody(), equalTo("Super hero succesfully created!"));
  }

  @Test
  public void testCreateSuperHeroFail() throws JsonProcessingException, IOException {
    ResponseEntity<String> response =
        restTemplate.getForEntity("https://localhost:8888/create?name=", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody(), equalTo("Name of super hero must not be empty"));
  }

  @Test
  public void testGetDetailASuperHeroWithoutAlly() throws JsonProcessingException, IOException {
    restTemplate.getForEntity(
        "https://localhost:8888/create?name=Spiderman&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=&dateOfAppearance=1980-09-01",
        String.class);

    ResponseEntity<String> response =
        restTemplate.getForEntity("https://localhost:8888/get-by-name?name=Spiderman", String.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    Hero heroCreated = objectMapper.readValue(response.getBody(), Hero.class);
    assertThat(heroCreated.getSuperHeroId(), notNullValue());
    assertThat(heroCreated.getName(), equalTo("Spiderman"));
    assertThat(heroCreated.getAllies().size(), equalTo(0));
  }

  @Test
  public void testGetDetailASuperHeroWithAlly() throws JsonProcessingException, IOException {
    restTemplate.getForEntity(
        "https://localhost:8888/create?name=Robin&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=&dateOfAppearance=1980-09-01",
        String.class);

    restTemplate.getForEntity(
        "https://localhost:8888/create?name=Batman&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=Robin&dateOfAppearance=1980-09-01",
        String.class);

    ResponseEntity<String> response =
        restTemplate.getForEntity("https://localhost:8888/get-by-name?name=Batman", String.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    Hero heroCreated = objectMapper.readValue(response.getBody(), Hero.class);
    assertThat(heroCreated.getSuperHeroId(), notNullValue());
    assertThat(heroCreated.getName(), equalTo("Batman"));
    assertThat(heroCreated.getAllies().size(), equalTo(1));
    for (Hero hero : heroCreated.getAllies()) {
      assertThat(hero.getName(), equalTo("Robin"));
    }
  }
}
