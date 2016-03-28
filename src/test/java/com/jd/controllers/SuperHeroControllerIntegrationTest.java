package com.jd.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
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
import com.jd.models.SuperHero;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SuperHeroControllerIntegrationTest {

  SSLConnectionSocketFactory socketFactory = null;
  HttpClient httpClient = null;

  @Before
  public void setupMock() throws KeyManagementException, NoSuchAlgorithmException,
      KeyStoreException {

    // Setup SSL connection
    socketFactory =
        new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null,
            new TrustSelfSignedStrategy()).build());

    // Setup credentials
    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials("test",
        "password"));

    // Setup http client
    httpClient =
        HttpClients.custom().setSSLSocketFactory(socketFactory).setDefaultCredentialsProvider(
            credsProvider).build();
  }

  @Test
  public void testFailAuthentication() {
    HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    ResponseEntity<String> response =
        rest.getForEntity("https://localhost:8888/get-all", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void testGetAllSuperHeroButEmpty() throws JsonProcessingException, IOException {
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    ResponseEntity<String> response =
        rest.getForEntity("https://localhost:8888/get-all", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode responseJson = objectMapper.readTree(response.getBody());
    assertThat(responseJson.asText(), equalTo(""));
  }

  @Test
  public void testCreateSuperHeroSuccess() throws JsonProcessingException, IOException {
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    ResponseEntity<String> response =
        rest.getForEntity(
            "https://localhost:8888/create?name=Xman&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=Robin&dateOfAppearance=1980-09-01",
            String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody(), equalTo("Super hero succesfully created!"));
  }

  @Test
  public void testCreateSuperHeroFail() throws JsonProcessingException, IOException {
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    ResponseEntity<String> response =
        rest.getForEntity("https://localhost:8888/create?name=", String.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody(), equalTo("Name of super hero must not be empty"));
  }

  @Test
  public void testGetDetailASuperHeroWithoutAlly() throws JsonProcessingException, IOException {
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    rest.getForEntity(
        "https://localhost:8888/create?name=Spiderman&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=&dateOfAppearance=1980-09-01",
        String.class);

    ResponseEntity<String> response =
        rest.getForEntity("https://localhost:8888/get-by-name?name=Spiderman", String.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    SuperHero superHeroCreated = objectMapper.readValue(response.getBody(), SuperHero.class);
    assertThat(superHeroCreated.getSuperHeroId(), notNullValue());
    assertThat(superHeroCreated.getName(), equalTo("Spiderman"));
    assertThat(superHeroCreated.getAllies().size(), equalTo(0));
  }

  @Test
  public void testGetDetailASuperHeroWithAlly() throws JsonProcessingException, IOException {
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    rest.getForEntity(
        "https://localhost:8888/create?name=Robin&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=&dateOfAppearance=1980-09-01",
        String.class);

    rest.getForEntity(
        "https://localhost:8888/create?name=Batman&pseudonym=pseudonym&publisher=Marvel&skill=Fly&allies=Robin&dateOfAppearance=1980-09-01",
        String.class);

    ResponseEntity<String> response =
        rest.getForEntity("https://localhost:8888/get-by-name?name=Batman", String.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    SuperHero superHeroCreated = objectMapper.readValue(response.getBody(), SuperHero.class);
    assertThat(superHeroCreated.getSuperHeroId(), notNullValue());
    assertThat(superHeroCreated.getName(), equalTo("Batman"));
    assertThat(superHeroCreated.getAllies().size(), equalTo(1));
    for (SuperHero hero : superHeroCreated.getAllies()) {
      assertThat(hero.getName(), equalTo("Robin"));
    }
  }
}
