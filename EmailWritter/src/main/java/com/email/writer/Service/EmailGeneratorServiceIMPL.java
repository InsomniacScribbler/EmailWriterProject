package com.email.writer.Service;

import com.email.writer.Entity.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;


@Service
public class EmailGeneratorServiceIMPL implements EmailGeneratorService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String GeminiAPIURL;
    @Value("${gemini.api.key}")
    private String GeminiAPIKey;

    public EmailGeneratorServiceIMPL(WebClient webClient) {
        this.webClient = webClient;
    }

    // constructer to inject webclient at the run time using parameterised constructor


    @Override
    public String generateEmailRequest(EmailRequest emailRequest) {
        //buuild prompt for the Gemini API
        String prompt = buildPrompt(emailRequest);

        //craft a request in the format
        Map<String,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );
        //do request
        String response = webClient.post()
                .uri(GeminiAPIURL+GeminiAPIKey)
                .header("Content-Type","application/json")
                .retrieve().bodyToMono(String.class).block();

        //Extract respones
        return extractResposnseContent(response);

        //Return

    }

    private String extractResposnseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content").path("parts").get(0).path("text").asText();
        }
        catch (Exception e) {
            return "Error Processing Request"+e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt= new StringBuilder();
        prompt.append("Generate a professional Email Reply for the following content. Please don't generate a subject line");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
            prompt.append("\n use a ").append(emailRequest.getTone()).append(" tone.");

        }
    }
}
