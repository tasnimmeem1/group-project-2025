package com.group.project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SaveRecipeController implements Initializable {

    @FXML
    private TextField txtName;

    // dropdowns
    @FXML
    private ChoiceBox<String> choiceCuisine;

    @FXML
    private ChoiceBox<String> choiceProtein;

    @FXML
    private ChoiceBox<String> choiceTime;

    @FXML
    private ChoiceBox<String> choiceDifficulty;

    @FXML
    private TextArea txtDetails;

    @FXML
    private TextArea txtIngredients;

    @FXML
    private Label   lblStatus;

    private final FirebaseMyRecipesService recipeService = new FirebaseMyRecipesService();

    // same kind of options as MyRecipesController, but without "Any"
    private final String[] cuisines = {
            "Chinese", "English", "Italian", "Spanish", "Middle Eastern", "Japanese"
    };

    private final String[] proteins = {
            "Chicken", "Beef", "Bean", "Tofu", "Fish", "Shellfish", "Veal", "Lamb"
    };

    private final String[] times = {
            "30 minutes No prep",
            "30 minutes Prep",
            "60-120 minutes No prep",
            "60-120 minutes Prep",
            "Multiple Hours prep"
    };

    private final String[] difficulties = {
            "Easy", "Medium", "Hard", "Chef Level"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (choiceCuisine != null) {
            choiceCuisine.getItems().addAll(cuisines);
        }
        if (choiceProtein != null) {
            choiceProtein.getItems().addAll(proteins);
        }
        if (choiceTime != null) {
            choiceTime.getItems().addAll(times);
        }
        if (choiceDifficulty != null) {
            choiceDifficulty.getItems().addAll(difficulties);
        }
    }

    // Save button
    @FXML
    private void handleSaveRecipe(ActionEvent event) {
        String name = safeText(txtName);

        String cuisine    = choiceCuisine != null && choiceCuisine.getValue() != null
                ? choiceCuisine.getValue()
                : "";
        String protein    = choiceProtein != null && choiceProtein.getValue() != null
                ? choiceProtein.getValue()
                : "";
        String time       = choiceTime != null && choiceTime.getValue() != null
                ? choiceTime.getValue()
                : "";
        String difficulty = choiceDifficulty != null && choiceDifficulty.getValue() != null
                ? choiceDifficulty.getValue()
                : "";

        String details = txtDetails != null && txtDetails.getText() != null
                ? txtDetails.getText().trim()
                : "";

        String ingredients = txtIngredients != null && txtIngredients.getText() != null
                ? txtIngredients.getText().trim()
                : "";

        if (name.isEmpty()) {
            setStatus("Please enter a recipe name.");
            return;
        }

        if (cuisine.isEmpty() || protein.isEmpty() || time.isEmpty() || difficulty.isEmpty()) {
            setStatus("Please select cuisine, protein, time and difficulty.");
            return;
        }

        RecipeModel recipe = new RecipeModel(
                null,
                name,
                cuisine,
                protein,
                time,
                difficulty,
                details,
                ingredients
        );

        try {
            String newId = recipeService.addRecipe(recipe);

            if (newId != null && !newId.isBlank()) {
                setStatus("Recipe saved. New ID: " + newId);
                clearForm();
            } else {
                setStatus("Recipe could not be saved. Try again.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            setStatus("Error connecting to Firebase.");
        }
    }

    // Back to My Recipes
    @FXML
    private void goBackToMyRecipes(ActionEvent event) {
        switchScene("/MyRecipes.fxml", event);
    }

    // Back to Home
    @FXML
    private void goBackHome(ActionEvent event) {
        switchScene("/HomePage.fxml", event);
    }

    private void switchScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error loading: " + fxmlPath);
        }
    }

    private String safeText(TextField field) {
        return (field == null || field.getText() == null)
                ? ""
                : field.getText().trim();
    }

    private void setStatus(String msg) {
        if (lblStatus != null) {
            lblStatus.setText(msg);
        } else {
            System.out.println(msg);
        }
    }

    private void clearForm() {
        if (txtName != null) txtName.clear();
        if (choiceCuisine != null) choiceCuisine.setValue(null);
        if (choiceProtein != null) choiceProtein.setValue(null);
        if (choiceTime != null) choiceTime.setValue(null);
        if (choiceDifficulty != null) choiceDifficulty.setValue(null);
        if (txtDetails != null) txtDetails.clear();
        if (txtIngredients != null) txtIngredients.clear();
    }
}