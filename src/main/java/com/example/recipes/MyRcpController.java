package com.example.recipes;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MyRcpController implements Initializable {

    @FXML
    private Label myLabel;

    @FXML
    private ChoiceBox<String> myCuisineChoicebox;

    @FXML
    private ChoiceBox<String> myProteinChoicebox;

    @FXML
    private ChoiceBox<String> myLengthChoicebox;

    @FXML
    private ChoiceBox<String> myDiffChoicebox;

    @FXML
    private TextField myTextField;

    private String[] cuisine = {"Chinese" , "English" , "Italian" , "Spanish" , "Middle Eastern" , "Japanese"};
    private String[] protein = {"Chicken" , "Beef" , "Bean" , "Tofu" , "Fish" , "Shellfish" , "Veal" , "Lamb"};
    private String[] time = {"30 minutes No prep" , "30 minutes Prep" , "60-120 minutes No prep", "60-120 minutes Prep", "Multiple Hours prep"};
    private String[] diff = {"Easy", "Medium", "Hard", "Chef Level"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myCuisineChoicebox.getItems().addAll(cuisine);
        myProteinChoicebox.getItems().addAll(protein);
        myLengthChoicebox.getItems().addAll(time);
        myDiffChoicebox.getItems().addAll(diff);

    }
}
