package com.group.project;

public class FirebaseConfig {
    // 🔐 From firebaseConfig
    public static final String API_KEY = "AIzaSyCQEBcoOxbem4wmSXiWKkCOgaDlPk-7Shw";
    public static final String PROJECT_ID = "recipeappgroupproject";

    // 🔗 Firebase Authentication endpoint
    public static final String FIREBASE_AUTH_SIGNUP_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;

    public static final String FIREBASE_AUTH_SIGNIN_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

    // 🗂 Firestore base REST URL
    public static final String FIRESTORE_BASE_URL =
            "https://firestore.googleapis.com/v1/projects/"
                    + PROJECT_ID + "/databases/(default)/documents";

    public static final String FIREBASE_DB_BASE =
            "https://recipeappgroupproject-default-rtdb.firebaseio.com";

    // 🍽 Used by Calorie Calculator
    public static final String FIREBASE_DB_CALORIE_RECIPES_URL =
            FIREBASE_DB_BASE + "/calorieRecipes";

    // 📚 Used by My Recipes page (FULL)
    public static final String FIREBASE_DB_MY_RECIPES_URL =
            FIREBASE_DB_BASE + "/myRecipes.json";
}