package com.sunBaseData.Assignment.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.sunBaseData.Assignment.Model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.net.http.HttpResponse;


@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${apiBaseUrl}")
    private String apiBaseUrl;

    public String authenticateUser(String loginId, String password)
    {
//        RestTemplate restTemplate = new RestTemplate();
        String authenticationUrl = apiBaseUrl + "/assignment_auth.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"login_id\": \"" + loginId + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(authenticationUrl, requestBody, String.class);
        return response;
    }


    public ResponseEntity<String> createNewCustomer(String bearerToken, Customer customer)
    {
        String authorizationUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);
        return restTemplate.exchange(authorizationUrl, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> displayCustomerList(String bearerToken)
    {
        String authorizationUrl = apiBaseUrl + "/assignment.jsp?cmd=get_customer_list";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(authorizationUrl, HttpMethod.GET, entity, String.class);
            return response;
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }
}
