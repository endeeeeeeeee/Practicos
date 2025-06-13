package com.simkernel.ai;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class GroqHelper {
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public GroqHelper(String apiKey) {
        this.apiKey = apiKey;
    }

    public String generarRespuesta(String prompt) throws IOException {
        String json = String.format(
                "{\"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"model\": \"llama3-8b-8192\"}",
                prompt.replace("\"", "\\\"")
        );

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return mapper.readTree(responseBody)
                    .get("choices").get(0)
                    .get("message").get("content")
                    .asText()
                    .trim();
        }
    }
}