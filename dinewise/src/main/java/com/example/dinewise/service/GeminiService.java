package com.example.dinewise.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

// https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent

@Service
public class GeminiService {

    // @Value("${gemini.api.key}")
    private String apiKey="AIzaSyA19-LltIbULGS5ly9Fr_W77HIYcujfwac";
    //System.getenv("GEMINI_API_KEY");
    // String geminiApiKey = System.getenv("GEMINI_API_KEY");


    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    public String getSuggestedMenu(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> content = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(Map.of("text", prompt)))
            )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(content, headers);


        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                GEMINI_API_URL + apiKey, request, Map.class
            );

            // System.out.println(apiKey);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Gemini API call failed: " + e.getMessage();
        }
    }
}
