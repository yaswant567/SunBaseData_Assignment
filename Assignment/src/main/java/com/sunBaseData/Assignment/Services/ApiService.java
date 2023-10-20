package com.sunBaseData.Assignment.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunBaseData.Assignment.Model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


@Service
public class ApiService {

    @Autowired
    private final RestTemplate restTemplate;
    @Value("${apiBaseUrl}")
    private String apiBaseUrl;
    private String bearerToken;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String authenticateUser(String loginId, String password)
    {
//        RestTemplate restTemplate = new RestTemplate();
        String authenticationUrl = apiBaseUrl + "/assignment_auth.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"login_id\": \"" + loginId + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(authenticationUrl, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            // Parsing the JSON response
            JsonNode jsonNode = objectMapper.readTree(response);
            bearerToken = jsonNode.get("access_token").asText();
            System.out.println("Access Token: " + bearerToken);
        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return bearerToken;
    }


    public ResponseEntity<String> createNewCustomer(String bearerToken, Customer customer)
    {
        String authorizationUrl = apiBaseUrl + "/assignment.jsp?cmd=create";
//        String authorizationUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("first_name", customer.getFirstName());
        requestBody.put("last_name", customer.getLastName());
        requestBody.put("street", customer.getStreet());
        requestBody.put("address", customer.getAddress());
        requestBody.put("city", customer.getCity());
        requestBody.put("state", customer.getState());
        requestBody.put("email", customer.getEmail());
        requestBody.put("phone", customer.getPhone());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        System.out.println("entity="+ entity);
        return restTemplate.exchange(authorizationUrl, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> displayCustomerList(String bearerToken)
    {
        String authorizationUrl = apiBaseUrl + "/assignment.jsp?cmd=get_customer_list";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(authorizationUrl, HttpMethod.GET, entity, String.class);
            System.out.println("1 :- " + response);
            return response;
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

    public void deleteCustomer(String id) {
        String authorizationUrl = apiBaseUrl + "/assignment.jsp?cmd=delete&uuid={id}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authorizationUrl);
        URI uri = builder.buildAndExpand(id).toUri();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("Deleting Customer");
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException ex) {
            System.out.println(ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString()));
        }
    }

    public void editCustomer(String id, Customer customer)
    {
        System.out.println("customer"+customer);

    }

}
