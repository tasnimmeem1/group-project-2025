package com.example.recipes;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToMyRcp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/recipes/MyRecipe.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToFindRcp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/recipes/FindRecipe.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToShareRcp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/recipes/ShareRecipe.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToNutrient(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/recipes/Nutrient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
