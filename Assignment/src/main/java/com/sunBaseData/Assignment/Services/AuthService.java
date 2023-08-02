package com.sunBaseData.Assignment.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AuthService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${apiAuthUrl}")
    private String apiAuthUrl;

    public String authenticateUser(String loginId, String password)
    {
        String authenticationUrl = apiAuthUrl + "/assignment_auth.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"login_id\": \"" + loginId + "\", \"password\": \"" + password + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(authenticationUrl, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            return responseBody.substring(responseBody.indexOf(" ") + 1);
        } else {
            throw new RuntimeException("Authentication failed");
        }

    }
}
