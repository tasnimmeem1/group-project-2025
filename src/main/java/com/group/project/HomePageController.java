package com.group.project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {
    // --------- Helper to switch scenes ---------
    private void switchScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading: " + fxmlPath);
        }
    }

    // --------- Button handlers (must match HomePage.fxml) ---------

    @FXML
    private void openMyRecipes(ActionEvent event) {
        // Your main recipes screen
        switchScene("/MyRecipes.fxml", event);
    }

    @FXML
    private void openSaveRecipe(ActionEvent event) {
        // For now this can go to the same MyRecipes page,
        // later you can change it to /FindRecipe.fxml when you build that screen.
        switchScene("/SaveRecipe.fxml", event);
    }

   // @FXML
  //  private void openShareRecipe(ActionEvent event) {
        // For now this can go to your "add/share recipe" screen.
        // Change the path if your FXML name is different.
     //   switchScene("/AddRecipe.fxml", event);
  //  }
   @FXML
   private void openShareRecipe(ActionEvent event) {
       // Now this will open Grocery List screen
       switchScene("/GroceryList.fxml", event);
   }

    @FXML
    private void openCalorieCalculator(ActionEvent event) {
        switchScene("/calorie_calculator.fxml", event);
    }


}