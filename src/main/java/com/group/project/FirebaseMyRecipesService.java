package com.group.project;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseMyRecipesService {
    /**
     * Read all recipes from:
     * https://recipeappgroupproject-default-rtdb.firebaseio.com/myRecipes.json
     */
    public List<RecipeModel> fetchAllRecipes() throws IOException {
        String urlString = FirebaseConfig.FIREBASE_DB_MY_RECIPES_URL;
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new IOException("Firebase GET failed with code " + code);
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        String json = sb.toString();
        if (json == null || json.isBlank() || "null".equals(json)) {
            return new ArrayList<>();
        }

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        List<RecipeModel> result = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            String id = entry.getKey();
            JsonObject obj = entry.getValue().getAsJsonObject();

            String name        = optString(obj, "name");
            String cuisine     = optString(obj, "cuisine");
            String protein     = optString(obj, "protein");
            String time        = optString(obj, "time");
            String difficulty  = optString(obj, "difficulty");
            String details     = optString(obj, "details");
            String ingredients = optString(obj, "ingredientsText"); // new

            result.add(new RecipeModel(
                    id,
                    name,
                    cuisine,
                    protein,
                    time,
                    difficulty,
                    details,
                    ingredients
            ));
        }

        return result;
    }

    private static String optString(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        return (el == null || el.isJsonNull()) ? "" : el.getAsString();
    }

    /**
     * Add a new recipe.
     * POST to /myRecipes.json so Firebase auto generates a unique id.
     */
    public String addRecipe(RecipeModel recipe) throws IOException {
        URL url = new URL(FirebaseConfig.FIREBASE_DB_MY_RECIPES_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        JsonObject body = new JsonObject();
        body.addProperty("name",           recipe.getName());
        body.addProperty("cuisine",        recipe.getCuisine());
        body.addProperty("protein",        recipe.getProtein());
        body.addProperty("time",           recipe.getTime());
        body.addProperty("difficulty",     recipe.getDifficulty());
        body.addProperty("details",        recipe.getDetails());
        body.addProperty("ingredientsText", recipe.getIngredients());   // new

        String bodyString = body.toString();
        try (OutputStream os = conn.getOutputStream()) {
            os.write(bodyString.getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK && code != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Firebase POST failed with code " + code);
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject resp = JsonParser.parseString(sb.toString()).getAsJsonObject();
        return resp.has("name") ? resp.get("name").getAsString() : null;
    }
}