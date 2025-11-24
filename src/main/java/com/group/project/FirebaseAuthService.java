package com.group.project;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseAuthService {
    public String signUp(String email, String password) throws IOException {
        String jsonInput = """
            {
              "email": "%s",
              "password": "%s",
              "returnSecureToken": true
            }
        """.formatted(email, password);

        return sendPostRequest(FirebaseConfig.FIREBASE_AUTH_SIGNUP_URL, jsonInput);
    }

    public String signIn(String email, String password) throws IOException {
        String jsonInput = """
            {
              "email": "%s",
              "password": "%s",
              "returnSecureToken": true
            }
        """.formatted(email, password);

        return sendPostRequest(FirebaseConfig.FIREBASE_AUTH_SIGNIN_URL, jsonInput);
    }

    private String sendPostRequest(String urlString, String jsonInput) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInput.getBytes());
        }

        int status = con.getResponseCode();
        InputStream is = (status == 200) ? con.getInputStream() : con.getErrorStream();
        String response = new String(is.readAllBytes());

        con.disconnect();

        if (status != 200) {
            throw new IOException("Firebase error: " + response);
        }

        return response;
    }
}