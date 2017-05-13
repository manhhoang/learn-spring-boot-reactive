package com.jd.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.exception.ErrorResponse;
import com.jd.models.Hero;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HeroControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setupMock() {

    }

    @Test
    public void testGetAllHeroButEmpty() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/heroes", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        assertThat(responseJson.asText(), equalTo(""));
    }

    @Test
    public void testCreateSuperHeroSuccess() throws IOException {
        Hero hero = new Hero();
        hero.setName("Supper Man");

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(hero);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(requestBody, header);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/hero/create", entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        if(response.getStatusCode() == HttpStatus.OK) {
            Hero heroCreated = objectMapper.readValue(response.getBody(), Hero.class);
            assertThat(heroCreated.getHeroId(), notNullValue());
            assertThat(heroCreated.getName(), equalTo("Supper Man"));
            assertThat(heroCreated.getAllies(), nullValue());
        } else {
            ErrorResponse error = objectMapper.readValue(response.getBody(), ErrorResponse.class);
            assertThat(error.getErrorCode(), equalTo("100"));
            assertThat(error.getErrorMessage(), equalTo("Hero is exist"));
        }
    }

    @Test
    public void testGetDetailASuperHeroWithoutAlly() throws IOException {
        Hero hero = new Hero();
        hero.setName("Supper Man");

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(hero);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(requestBody, header);

        restTemplate.postForEntity("/api/hero/create", entity, Hero.class);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/heroes", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        List<Hero> heroCreated = objectMapper.readValue(response.getBody(), ArrayList.class);
        assertThat(heroCreated, notNullValue());
    }
}
