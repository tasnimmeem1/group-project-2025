package com.group.project;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FirestoreService {
    public void saveUserProfile(String userId,
                                String name,
                                String email,
                                String username,
                                String address) throws IOException {

        // Collection "users", document id = userId
        String urlString = FirebaseConfig.FIRESTORE_BASE_URL
                + "/users?documentId=" + userId
                + "&key=" + FirebaseConfig.API_KEY;

        String jsonBody = """
            {
              "fields": {
                "name":     { "stringValue": "%s" },
                "email":    { "stringValue": "%s" },
                "username": { "stringValue": "%s" },
                "address":  { "stringValue": "%s" }
              }
            }
            """.formatted(escape(name), escape(email), escape(username), escape(address));

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int status = con.getResponseCode();

        // Read response (success OR error) for debugging
        InputStream is = (status >= 200 && status < 300)
                ? con.getInputStream()
                : con.getErrorStream();

        String response = "";
        if (is != null) {
            response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            is.close();
        }

        if (status < 200 || status >= 300) {
            System.err.println("Firestore error, status " + status + ": " + response);
            throw new IOException("Firestore write failed: " + response);
        } else {
            System.out.println("Firestore write OK: " + response);
        }

        con.disconnect();
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }
}