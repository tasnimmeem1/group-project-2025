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
            // Call your existing Firebase sign in method
            String response = firebaseAuthService.signIn(email, password);
            System.out.println("Sign in response: " + response);

            // If we reach here, assume login success
            errorLabel.setText("");

            // 👇 Open home page
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
                    getClass().getResource("/Home.fxml")  // Correct file path
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();      // Adjust to preferred size
            stage.centerOnScreen();   // Center it properly
            stage.show();             // Display it
        } catch (IOException e) {
            e.printStackTrace();
            if (errorLabel != null) {
                errorLabel.setText("Could not open home page.");
            }
        }
    }


}