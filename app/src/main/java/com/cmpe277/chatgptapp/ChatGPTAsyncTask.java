package com.cmpe277.chatgptapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

public class ChatGPTAsyncTask extends AsyncTask<String, Void, String> {

    private final Consumer<String> onResponse;

    public ChatGPTAsyncTask(Consumer<String> onResponse) {
        this.onResponse = onResponse;
    }

    @Override
    protected String doInBackground(String... prompts) {
        return NetworkUtils.callOpenAIChatGPT(prompts[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            // Convert the response string to a JSONObject
            JSONObject jsonResponse = new JSONObject(result);

            // Navigate through the JSON to get to the "choices" array and then the "content" of the first choice
            String content = jsonResponse.getJSONArray("choices").getJSONObject(0)
                    .getJSONObject("message").getString("content");

            // Now you can use just the content string
            onResponse.accept(content);
        } catch (JSONException e) {
            e.printStackTrace();
            onResponse.accept("Error parsing response: " + e.getMessage());
        }
    }

}
