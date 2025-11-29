package com.example.recipes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class MyRcpController implements Initializable {

    @FXML private Label topTitle;
    @FXML private ComboBox<String> cuisineBox;
    @FXML private ComboBox<String> proteinBox;
    @FXML private ComboBox<String> timeBox;
    @FXML private ComboBox<String> difficultyBox;

    @FXML private ListView<String> recipeList;
    @FXML private TextArea recipeDetails;


    private final String[] cuisine = { "Italian", "Indian", "Chinese", "American" };
    private final String[] protein = { "Chicken", "Beef", "Vegetarian", "Fish" };
    private final String[] time = { "Under 20 min", "30-45 min", "1 hour+" };
    private final String[] diff = { "Easy", "Medium", "Hard" };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // populate the search
        cuisineBox.getItems().addAll(cuisine);
        proteinBox.getItems().addAll(protein);
        timeBox.getItems().addAll(time);
        difficultyBox.getItems().addAll(diff);

        // sample recipe list
        recipeList.getItems().addAll(
                "Chicken Marsala",
                "Chicken Parmesan",
                "Chicken Tikka Masala",
                "Veal Cutlet",
                "Vegetarian Shashuka"
        );

        // when a recipe is selected, load details into the right pane
        recipeList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadRecipe(newVal);
            }
        });
    }


    private void loadRecipe(String recipeName) {
        String sample = recipeName + "\n\nIngredients:\n- Ingredient A\n- Ingredient B\n\nSteps:\n1) Step one...\n2) Step two...\n3) Serve.";
        recipeDetails.setText(sample);
    }


    @FXML
    private void showFavorites(ActionEvent e) {
        // implement your favorites filtering / logic
        recipeList.getItems().clear();
        recipeList.getItems().addAll(
                "Chicken Marsala",
                "Chicken Parmesan",
                "Vegetarian Shashuka"
        );
    }

    @FXML
    private void goHome(ActionEvent e) throws IOException {
        new SceneController().switchToMyRcp(e);
    }
}
