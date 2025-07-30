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
    // private String apiKey="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";
    // //System.getenv("GEMINI_API_KEY");
    // // String geminiApiKey = System.getenv("GEMINI_API_KEY");


    // private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    // public String getSuggestedMenu(String prompt) {
    //     RestTemplate restTemplate = new RestTemplate();

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     Map<String, Object> content = Map.of(
    //         "contents", List.of(
    //             Map.of("parts", List.of(Map.of("text", prompt)))
    //         )
    //     );

    //     HttpEntity<Map<String, Object>> request = new HttpEntity<>(content, headers);


    //     try {
    //         ResponseEntity<Map> response = restTemplate.postForEntity(
    //             GEMINI_API_URL + apiKey, request, Map.class
    //         );

    //         // System.out.println(apiKey);

    //         List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
    //         Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
    //         List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

    //         return (String) parts.get(0).get("text");

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return "Gemini API call failed: " + e.getMessage();
    //     }
    // }
    // Load from env or config if you prefer
    private final String apiKey = "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"; // Replace with System.getenv("OPENROUTER_API_KEY") if needed

    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public String getSuggestedMenu(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        int maxRetries = 5;
        int retryCount = 0;
        int waitTimeSeconds = 3; // initial wait time

        while (retryCount < maxRetries) {
            try {
                // Prepare headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + apiKey);
                headers.set("HTTP-Referer", "http://52.184.83.81:8080"); // optional
                headers.set("X-Title", "DineWise"); // optional

                // Prepare request body
                Map<String, Object> body = new HashMap<>();
                body.put("model", "qwen/qwen3-coder:free");
                body.put("messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ));

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

                // Send request
                ResponseEntity<Map> response = restTemplate.postForEntity(OPENROUTER_API_URL, request, Map.class);

                // Extract response
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                } else {
                    return "No content returned from OpenRouter.";
                }

            } catch (Exception e) {
                String errorMsg = e.getMessage();
                System.err.println("Error during API call: " + errorMsg);
            }
        }

        return "Max retries exceeded. OpenRouter request aborted.";
    }
}
