package com.email.writer.Service;

import com.email.writer.Entity.EmailRequest;
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


        //return respones
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt= new StringBuilder();
        prompt.append("Generate a professional Email Reply for the following content. Please don't generate a subject line");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
            prompt.append("\n use a ").append(emailRequest.getTone()).append(" tone.");

        }
    }
}
