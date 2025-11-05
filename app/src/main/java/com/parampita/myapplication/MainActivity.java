package com.parampita.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText apiKeyInput, systemInput, userInput, temperatureInput, topPInput, maxTokensInput;
    private TextView outputTextView;
    private Spinner modelSpinner;
    private OkHttpClient client;
    private Gson gson;
    private String selectedModel = "gpt-3.5-turbo"; // Default model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiKeyInput = findViewById(R.id.apiKeyInput);
        modelSpinner = findViewById(R.id.modelSpinner);
        systemInput = findViewById(R.id.systemInput);
        userInput = findViewById(R.id.userInput);
        temperatureInput = findViewById(R.id.temperatureInput);
        topPInput = findViewById(R.id.topPInput);
        maxTokensInput = findViewById(R.id.maxTokensInput);
        Button goButton = findViewById(R.id.goButton);
        outputTextView = findViewById(R.id.outputTextView);

        client = new OkHttpClient();
        gson = new Gson();

        // Set up the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gpt_models, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "Selected model: " + selectedModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default model remains unchanged
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiKey = apiKeyInput.getText().toString();
                String systemText = systemInput.getText().toString();
                String userText = userInput.getText().toString();
                String temperature = temperatureInput.getText().toString();
                String topP = topPInput.getText().toString();
                String maxTokens = maxTokensInput.getText().toString();

                sendChatRequest(apiKey, systemText, userText, temperature, topP, maxTokens);
            }
        });
    }

    private void sendChatRequest(String apiKey, String systemText, String userText, String temperature, String topP, String maxTokens) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String url = "https://api.openai.com/v1/chat/completions";

        GPTRequest gptRequest = new GPTRequest(selectedModel, systemText, userText, Double.parseDouble(temperature), Double.parseDouble(topP), Integer.parseInt(maxTokens));
        String requestBody = gson.toJson(gptRequest);
        RequestBody body = RequestBody.create(requestBody, JSON);

        Log.d(TAG, "Selected Model for Request: " + selectedModel);
        Log.d(TAG, "Request Body: " + requestBody);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Network request failed", e);
                runOnUiThread(() -> {
                    outputTextView.setText("Error: " + e.getMessage());
                    Log.e(TAG, "Exception: ", e);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody;
                try {
                    responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Response Body: " + responseBody);
                        GPTResponse gptResponse = gson.fromJson(responseBody, GPTResponse.class);
                        runOnUiThread(() -> {
                            if (gptResponse.getChoices() != null && !gptResponse.getChoices().isEmpty()) {
                                outputTextView.setText(gptResponse.getChoices().get(0).getMessage().getContent().trim());
                            } else {
                                outputTextView.setText("Error: No choices returned");
                            }
                        });
                    } else {
                        Log.e(TAG, "Response not successful: " + response.message() + ", Response Body: " + responseBody);
                        runOnUiThread(() -> outputTextView.setText("Error: " + response.message() + ", " + responseBody));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error reading response", e);
                    runOnUiThread(() -> outputTextView.setText("Error reading response: " + e.getMessage()));
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    private static class GPTRequest {
        private final String model;
        private final List<Message> messages;
        private final double temperature;
        private final double top_p;
        private final int max_tokens;

        public GPTRequest(String model, String systemText, String userText, double temperature, double top_p, int max_tokens) {
            this.model = model;
            this.messages = new ArrayList<>();
            this.messages.add(new Message("system", systemText));
            this.messages.add(new Message("user", userText));
            this.temperature = temperature;
            this.top_p = top_p;
            this.max_tokens = max_tokens;
        }
    }

    private static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    private static class GPTResponse {
        private final List<Choice> choices;

        public GPTResponse() {
            this.choices = Collections.emptyList();
        }

        public List<Choice> getChoices() {
            return choices;
        }

        private static class Choice {
            @SerializedName("message")
            private final Message message;

            public Choice(Message message) {
                this.message = message;
            }

            public Message getMessage() {
                return message;
            }
        }
    }
}
