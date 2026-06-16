package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final String geminiApiKey;

    // Inject your property configurations directly into the constructor arguments
    public GeminiService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.url}") String geminiUrl,
            @Value("${gemini.api.key}") String geminiApiKey) {

        this.geminiApiKey = geminiApiKey;
        // This ensures the client is baked with the root host correctly right at startup
        this.webClient = webClientBuilder
                .baseUrl(geminiUrl)
                .build();
    }

    public String getAnswer(String question) {
        // Constructing your nested payload map
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                }
        );

        // Execute the call using the established client instance
        return this.webClient.post()
                .uri("/v1beta/models/gemini-flash-latest:generateContent") // Appends path seamlessly onto baseUrl
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-goog-api-key", this.geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}