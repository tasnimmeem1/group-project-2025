
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
public class SignupController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    private final FirebaseAuthService firebaseAuthService = new FirebaseAuthService();

    @FXML
    public void handleSignup(ActionEvent event) {
        System.out.println("Sign up button clicked"); // debug

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            // Firebase usually requires 6+ characters
            errorLabel.setText("Password must be at least 6 characters.");
            return;
        }

        try {
            // Call your existing signUp method
            String response = firebaseAuthService.signUp(email, password);
            System.out.println("Sign up response: " + response);

            // If we reach here, assume success and go back to login
            errorLabel.setText("");
            backToLogin(event);

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not create account. Try again.");
        }
    }

    @FXML
    public void backToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/Login.fxml")   // or "/login.fxml" → match file name exactly
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            if (errorLabel != null) {
                errorLabel.setText("Could not go back to login.");
            }
        }
    }
}