package com.cmpe277.chatgptapp;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "";

    public static String callOpenAIChatGPT(String prompt) {
        String jsonPayload = String.format(
                "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}",
                prompt);

        RequestBody body = RequestBody.create(jsonPayload, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}

