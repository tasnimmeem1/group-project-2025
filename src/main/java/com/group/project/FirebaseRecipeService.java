package com.group.project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FirebaseRecipeService {
    // Simple data holder for nutrition
    public static class RecipeNutrition {
        public final double protein;
        public final double calories;
        public final double fat;
        public final double carbs;
        public final double vitamins;

        public RecipeNutrition(double protein, double calories, double fat,
                               double carbs, double vitamins) {
            this.protein = protein;
            this.calories = calories;
            this.fat = fat;
            this.carbs = carbs;
            this.vitamins = vitamins;
        }
    }

    public static RecipeNutrition getNutritionForRecipe(String recipeName) throws IOException {
        if (recipeName == null || recipeName.isBlank()) {
            return null;
        }

        // Normalize recipe name: "Beef   Taco" → "beef-taco" (case-insensitive)
        String normalizedName = recipeName.trim()
                .toLowerCase()
                .replaceAll("\\s+", "-");

        // Encode properly for URL
        String encodedName = URLEncoder.encode(normalizedName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        // Build final Firebase URL (rules allow read, so no auth token)
        String urlString = FirebaseConfig.FIREBASE_DB_CALORIE_RECIPES_URL
                + "/" + encodedName + ".json";

        System.out.println("Requesting recipe URL: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        int responseCode = conn.getResponseCode();
        System.out.println("HTTP response code: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.err.println("Firebase recipe GET failed with code " + responseCode);
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String json = sb.toString();
        System.out.println("Firebase recipe JSON: " + json);

        if (json == null || json.equals("null") || json.isBlank()) {
            return null;
        }

        double calories = getDoubleField(json, "calories");
        double protein  = getDoubleField(json, "protein");
        double fat      = getDoubleField(json, "fat");
        double carbs    = getDoubleField(json, "carbs");
        double vitamins = getDoubleField(json, "vitamins");

        return new RecipeNutrition(protein, calories, fat, carbs, vitamins);
    }

    // Tiny helper to pull a number from simple JSON like {"calories":200,"carbs":45,...}
    private static double getDoubleField(String json, String fieldName) {
        String key = "\"" + fieldName + "\"";
        int idx = json.indexOf(key);
        if (idx == -1) return 0.0;

        idx = json.indexOf(":", idx);
        if (idx == -1) return 0.0;

        int start = idx + 1;
        int len = json.length();

        while (start < len && (json.charAt(start) == ' ' || json.charAt(start) == '"')) {
            start++;
        }

        int end = start;
        while (end < len) {
            char c = json.charAt(end);
            if (!((c >= '0' && c <= '9') || c == '.' || c == '-')) break;
            end++;
        }

        try {
            return Double.parseDouble(json.substring(start, end));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}