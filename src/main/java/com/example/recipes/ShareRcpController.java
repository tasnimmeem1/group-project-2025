package com.example.recipes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ShareRcpController implements Initializable {

    @FXML private ChoiceBox<String> cuisineChoice;
    @FXML private ChoiceBox<String> proteinChoice;
    @FXML private ChoiceBox<String> timeChoice;
    @FXML private ChoiceBox<String> difficultyChoice;

    @FXML private TextArea recipeText;
    @FXML private ImageView imagePreview;

    private final String[] cuisine = {"Chinese", "English", "Italian", "Spanish", "Middle Eastern", "Japanese"};
    private final String[] protein = {"Chicken", "Beef", "Bean", "Tofu", "Fish", "Shellfish", "Veal", "Lamb"};
    private final String[] time = {"30 minutes No prep", "30 minutes Prep", "60-120 minutes No prep", "60-120 minutes Prep", "Multiple Hours prep"};
    private final String[] diff = {"Easy", "Medium", "Hard", "Chef Level"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (cuisineChoice != null) cuisineChoice.getItems().addAll(cuisine);
        if (proteinChoice != null) proteinChoice.getItems().addAll(protein);
        if (timeChoice != null) timeChoice.getItems().addAll(time);
        if (difficultyChoice != null) difficultyChoice.getItems().addAll(diff);
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Recipe Image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            imagePreview.setImage(new Image(file.toURI().toString()));
        }
    }

}