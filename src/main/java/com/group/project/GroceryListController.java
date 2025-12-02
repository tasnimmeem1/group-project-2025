package com.group.project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class GroceryListController implements Initializable {

    @FXML
    private ListView<String> groceryListView;

    @FXML
    private TextField txtNewItem;

    @FXML
    private Label lblStatus;

    private final ObservableList<String> items =
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (groceryListView != null) {
            groceryListView.setItems(items);
        }
        setStatus("You can type items and click Add. Or open from a recipe to auto fill.");
    }

    /**
     * Called from MyRecipesController when user chooses "Grocery list from this recipe".
     */
    public void loadFromRecipe(RecipeModel recipe) {
        if (recipe == null) {
            return;
        }

        items.clear();

        String ing = recipe.getIngredients();
        if (ing == null || ing.isBlank()) {
            setStatus("This recipe does not have ingredients saved yet.");
            return;
        }

        String[] lines = ing.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                items.add(trimmed);
            }
        }

        setStatus("Loaded grocery list from recipe: " + recipe.getName());
    }

    // Add item typed by the user
    @FXML
    private void handleAddItem(ActionEvent event) {
        String text = txtNewItem != null ? txtNewItem.getText() : null;
        if (text != null) {
            text = text.trim();
        }

        if (text == null || text.isEmpty()) {
            setStatus("Please type an item first.");
            return;
        }

        items.add(text);
        txtNewItem.clear();
        setStatus("Item added.");
    }

    // Remove selected item
    @FXML
    private void handleRemoveSelected(ActionEvent event) {
        if (groceryListView == null) return;

        String selected = groceryListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select an item to remove.");
            return;
        }

        items.remove(selected);
        setStatus("Item removed.");
    }

    // Clear whole list
    @FXML
    private void handleClearAll(ActionEvent event) {
        items.clear();
        setStatus("Grocery list cleared.");
    }

    // Navigation
    @FXML
    private void goBackToMyRecipes(ActionEvent event) {
        switchScene("/MyRecipes.fxml", event);
    }

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

    private void setStatus(String msg) {
        if (lblStatus != null) {
            lblStatus.setText(msg);
        } else {
            System.out.println(msg);
        }
    }
}
