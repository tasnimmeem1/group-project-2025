package com.group.project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MyRecipesController implements Initializable{

    // ---------- Filters on left ----------
    @FXML
    private ChoiceBox<String> myCuisineChoicebox;

    @FXML
    private ChoiceBox<String> myProteinChoicebox;

    @FXML
    private ChoiceBox<String> myLengthChoicebox;

    @FXML
    private ChoiceBox<String> myDiffChoicebox;

    // ---------- Center list & right details ----------
    @FXML
    private ListView<RecipeModel> recipeList;

    @FXML
    private TextArea recipeDetails;

    // All recipes from Firebase
    private List<RecipeModel> allRecipes = new ArrayList<>();

    // Filter options (UI only, not from DB)
    private final String[] cuisines = {
            "Any", "Chinese", "English", "Italian", "Spanish", "Middle Eastern", "Japanese"
    };

    private final String[] proteins = {
            "Any", "Chicken", "Beef", "Bean", "Tofu", "Fish", "Shellfish", "Veal", "Lamb"
    };

    private final String[] times = {
            "Any",
            "30 minutes No prep",
            "30 minutes Prep",
            "60-120 minutes No prep",
            "60-120 minutes Prep",
            "Multiple Hours prep"
    };

    private final String[] difficulties = {
            "Any", "Easy", "Medium", "Hard", "Chef Level"
    };

    // Firebase service
    private final FirebaseMyRecipesService recipeService = new FirebaseMyRecipesService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Fill filters
        if (myCuisineChoicebox != null) {
            myCuisineChoicebox.getItems().addAll(cuisines);
            myCuisineChoicebox.setValue("Any cuisine");
        }
        if (myProteinChoicebox != null) {
            myProteinChoicebox.getItems().addAll(proteins);
            myProteinChoicebox.setValue("Any protein");
        }
        if (myLengthChoicebox != null) {
            myLengthChoicebox.getItems().addAll(times);
            myLengthChoicebox.setValue("No specific length");
        }
        if (myDiffChoicebox != null) {
            myDiffChoicebox.getItems().addAll(difficulties);
            myDiffChoicebox.setValue("Any difficulty");
        }

        // Load recipes from Firebase
        try {
            allRecipes = recipeService.fetchAllRecipes();
            if (recipeList != null) {
                recipeList.getItems().setAll(allRecipes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (recipeDetails != null) {
                recipeDetails.setText("Error loading recipes from Firebase:\n" + e.getMessage());
            }
        }

        // When a recipe is selected, show details on right
        if (recipeList != null) {
            recipeList.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((obs, oldVal, newVal) -> {
                        if (newVal != null) {
                            showRecipeDetails(newVal);
                        }
                    });
        }
    }

    private void showRecipeDetails(RecipeModel recipe) {
        if (recipeDetails == null || recipe == null) return;

        String text = recipe.getName() + "\n\n" +
                "Cuisine: " + recipe.getCuisine() + "\n" +
                "Protein: " + recipe.getProtein() + "\n" +
                "Time: " + recipe.getTime() + "\n" +
                "Difficulty: " + recipe.getDifficulty() + "\n\n" +
                recipe.getDetails();

        recipeDetails.setText(text);
    }

    // Left button: "My Recipe" → apply filters
    @FXML
    private void showMyRecipes(ActionEvent e) {
        applyFilters();
    }

    private void applyFilters() {
        if (recipeList == null) return;

        String selCuisine = myCuisineChoicebox.getValue();
        String selProtein = myProteinChoicebox.getValue();
        String selTime = myLengthChoicebox.getValue();
        String selDiff = myDiffChoicebox.getValue();

        List<RecipeModel> filtered = allRecipes.stream()
                .filter(r -> "Any".equals(selCuisine) || selCuisine.equals(r.getCuisine()))
                .filter(r -> "Any".equals(selProtein) || selProtein.equals(r.getProtein()))
                .filter(r -> "Any".equals(selTime) || selTime.equals(r.getTime()))
                .filter(r -> "Any".equals(selDiff) || selDiff.equals(r.getDifficulty()))
                .collect(Collectors.toList());

        recipeList.getItems().setAll(filtered);
        recipeDetails.clear();
    }

    // Button: Back to Home
    @FXML
    private void goBackHome(ActionEvent e) {
        switchScene("/HomePage.fxml", e);
    }

    private void switchScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error loading: " + fxmlPath);
        }
    }

    // ---------- Grocery list: open for currently selected recipe ----------
    // In MyRecipesController.java

    @FXML
    private void openGroceryForSelected(ActionEvent event) {
        if (recipeList == null) return;

        RecipeModel selected = recipeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (recipeDetails != null) {
                recipeDetails.setText("Please select a recipe first to create grocery list.");
            }
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GroceryList.fxml"));
            Parent root = loader.load();

            GroceryListController controller = loader.getController();
            controller.loadFromRecipe(selected);   // this fills grocery list

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            if (recipeDetails != null) {
                recipeDetails.setText("Error opening grocery list screen.");
            }
        }
    }

}