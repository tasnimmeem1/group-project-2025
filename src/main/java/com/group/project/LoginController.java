package com.group.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final FirebaseAuthService firebaseAuthService = new FirebaseAuthService();

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter email and password.");
            return;
        }

        try {
            // Call Firebase sign in
            String response = firebaseAuthService.signIn(email, password);
            System.out.println("Sign in response: " + response);

            // Extract idToken from the JSON response
            String idToken = extractJsonValue(response, "idToken");
            if (idToken == null || idToken.isEmpty()) {
                errorLabel.setText("Login succeeded but token not found.");
                return;
            }

            // Save token and email for later
            FirebaseAuthService.userIdToken = idToken;
            FirebaseAuthService.userEmail = email;

            errorLabel.setText("");

            // Open home page
            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not sign in. Please try again.");
        }
    }

    @FXML
    public void openSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/Signup.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not open signup screen.");
        }
    }

    private void goToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/HomePage.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            if (errorLabel != null) {
                errorLabel.setText("Could not open home page.");
            }
        }
    }

    /**
     * Very small helper to pull a string value from JSON like
     * {"idToken":"ABC","email":"123@testing.com",...}
     */
    private String extractJsonValue(String json, String key) {
        if (json == null || key == null) return null;

        String quotedKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(quotedKey);
        if (keyIndex == -1) return null;

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return null;

        int start = colonIndex + 1;
        int len = json.length();

        // skip spaces and quotes
        while (start < len && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) {
            start++;
        }

        int end = start;
        while (end < len && json.charAt(end) != '\"'
                && json.charAt(end) != ','
                && json.charAt(end) != '}') {
            end++;
        }

        return json.substring(start, end);
    }
}