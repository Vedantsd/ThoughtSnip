package com.example.thoughtsnip;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityAskGemini extends AppCompatActivity {

    private static final String TAG = "GeminiAPI";

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask_gemini);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView = findViewById(R.id.geminiresult);

        String title = getIntent().getStringExtra("title");
        String problem = getIntent().getStringExtra("problem");
        String solution = getIntent().getStringExtra("solution");

        String prompt = "Title: " + title +
                "\nProblem: " + problem +
                "\nSolution: " + solution +
                "\n\nReview my idea and tell me the pros and cons in under 300 words. Don't use any text designing, just give plain text. Don't use bold, italic or any kind of formatting, just plain txt";

        String apiKey = BuildConfig.GOOGLE_API_KEY;

        new Thread(() -> {
            String result = callGemini(prompt, apiKey);
            runOnUiThread(() -> textView.setText(result));
        }).start();
    }

    private String callGemini(String prompt, String apiKey) {
        try {
            JSONObject textPart = new JSONObject().put("text", prompt);
            JSONArray parts = new JSONArray().put(textPart);

            JSONObject content = new JSONObject()
                    .put("role", "user")
                    .put("parts", parts);

            JSONArray contents = new JSONArray().put(content);

            JSONObject jsonRequest = new JSONObject()
                    .put("contents", contents);

            MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(jsonRequest.toString(), MEDIA_TYPE_JSON);

            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("x-goog-api-key", apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Request failed. Code: " + response.code() + " Body: " + responseBody);
                    return "Error " + response.code() + ":\n" + responseBody;
                }

                Log.d(TAG, "Gemini Response: " + responseBody);
                return extractText(responseBody);
            }

        } catch (IOException e) {
            Log.e(TAG, "Network error", e);
            return "Network error: " + e.getMessage();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error", e);
            return "Unexpected error: " + e.getMessage();
        }
    }

    private String extractText(String jsonResponse) {
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray candidates = root.getJSONArray("candidates");
            if (candidates.length() == 0) {
                return "No candidates returned from model.";
            }

            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");

            if (parts.length() == 0) {
                return "No text parts in response.";
            }

            return parts.getJSONObject(0).getString("text");

        } catch (Exception e) {
            Log.e(TAG, "Parsing error", e);
            return "Failed to parse response.\n" + e.getMessage();
        }
    }
}
