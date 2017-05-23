package com.jd.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.models.User;
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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setupMock() {

    }

    @Test
    public void testGetAllUsers() throws IOException {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/users", String.class, entity);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        assertThat(responseJson.asText(), equalTo(""));
    }

    @Test
    public void testCreateUserSuccess() throws IOException {
        User user = new User();
        user.setUsername("Test");
        user.setPassword("12345");

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(requestBody, header);

        ResponseEntity<User> response = restTemplate.postForEntity("/api/user/create", entity, User.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getUsername(), equalTo("Test"));
        assertNotEquals(response.getBody().getId(), null);
    }

    @Test
    public void testFindUserByName() throws IOException {
        User user = new User();
        user.setUsername("Test");
        user.setPassword("12345");

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(requestBody, header);

        restTemplate.postForEntity("/api/user/create", entity, User.class);

        ResponseEntity<User> response = restTemplate.getForEntity("/api/user/Test", User.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getUsername(), equalTo("Test"));
        assertThat(response.getBody().getId(), equalTo(1l));
    }
}
