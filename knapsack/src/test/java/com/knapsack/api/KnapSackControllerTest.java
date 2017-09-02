package com.knapsack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knapsack.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KnapSackControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setupMock() {
        BasicAuthorizationInterceptor interceptor = new BasicAuthorizationInterceptor("user", "password");
        testRestTemplate.getRestTemplate().getInterceptors().add(interceptor);
    }

    @Test
    public void testOptimizing() throws IOException {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        List<Item> body = new ArrayList<>();
        HttpEntity<List<Item>> entity = new HttpEntity<>(body, header);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/v1/knapsack/5", entity, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        List<Item> items = objectMapper.readValue(response.getBody(), List.class);
        assertEquals(0, items.size());
    }
}
