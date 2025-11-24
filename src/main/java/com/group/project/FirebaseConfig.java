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
}
