package com.group.project;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class CalorieCalculatorController {
    @FXML
    private TextField txtRecipeName, txtServings, txtProtein, txtCalories, txtFat, txtCarbs, txtVitamins;

    @FXML
    private Label lblProteinPercent, lblCaloriesPercent, lblFatPercent,
            lblCarbsPercent, lblVitaminsPercent, lblAdvice, lblCaloriesCenter;

    @FXML
    private Label lblChartDescription;   // ok if not in FXML

    @FXML
    private Button btnCalculate, btnSearch;

    @FXML
    private PieChart caloriePieChart;

    @FXML
    private ImageView backgroundImageView;

    // ---------- Initialize ----------
    @FXML
    private void initialize() {
        URL bgUrl = getClass().getResource("/images/Calculator.jpeg");
        System.out.println("Background URL = " + bgUrl);

        if (bgUrl != null && backgroundImageView != null) {
            backgroundImageView.setImage(new Image(bgUrl.toExternalForm()));
        } else {
            System.err.println("Background image not found at /images/Calculator.jpeg");
        }

        if (caloriePieChart != null) {
            updatePieChart(0);
        }
    }

    // ---------- Search recipe in Firebase ----------
    @FXML
    private void handleSearch(ActionEvent event) {
        String name = txtRecipeName.getText();

        if (name == null || name.isBlank()) {
            lblAdvice.setText("Please enter a recipe name to search.");
            return;
        }

        try {
            FirebaseRecipeService.RecipeNutrition rn =
                    FirebaseRecipeService.getNutritionForRecipe(name);

            if (rn == null) {
                lblAdvice.setText("Recipe not found. Enter values manually.");
                return;
            }

            txtProtein.setText(String.valueOf(rn.protein));
            txtCalories.setText(String.valueOf(rn.calories));
            txtFat.setText(String.valueOf(rn.fat));
            txtCarbs.setText(String.valueOf(rn.carbs));
            txtVitamins.setText(String.valueOf(rn.vitamins));

            lblAdvice.setText("Nutrition loaded from Firebase. Adjust servings and click Calculate.");

        } catch (IOException e) {
            e.printStackTrace();
            lblAdvice.setText("Could not connect to Firebase.");
        }
    }

    // ---------- Calculate percentages ----------
    @FXML
    private void calculatePercentages(ActionEvent event) {
        try {
            double servings = Double.parseDouble(txtServings.getText());
            double protein  = Double.parseDouble(txtProtein.getText());
            double calories = Double.parseDouble(txtCalories.getText());
            double fat      = Double.parseDouble(txtFat.getText());
            double carbs    = Double.parseDouble(txtCarbs.getText());
            double vitamins = Double.parseDouble(txtVitamins.getText());

            if (servings <= 0) {
                resetPercents();
                lblAdvice.setText("Servings must be at least 1.");
                updatePieChart(0);
                return;
            }

            double total = protein + calories + fat + carbs + vitamins;
            if (total <= 0) {
                resetPercents();
                lblAdvice.setText("Enter valid nutrition values.");
                updatePieChart(0);
                return;
            }

            double proteinPercent  = (protein  / total) * 100;
            double caloriesPercent = (calories / total) * 100;
            double fatPercent      = (fat      / total) * 100;
            double carbsPercent    = (carbs    / total) * 100;
            double vitaminsPercent = (vitamins / total) * 100;

            lblProteinPercent.setText(String.format("%.1f %%", proteinPercent));
            lblCaloriesPercent.setText(String.format("%.1f %%", caloriesPercent));
            lblFatPercent.setText(String.format("%.1f %%", fatPercent));
            lblCarbsPercent.setText(String.format("%.1f %%", carbsPercent));
            lblVitaminsPercent.setText(String.format("%.1f %%", vitaminsPercent));

            double totalCalories = calories * servings;
            updatePieChart(totalCalories);

            lblAdvice.setText(getCalorieAdvice(totalCalories, proteinPercent, fatPercent));

        } catch (NumberFormatException e) {
            resetPercents();
            lblAdvice.setText("Please enter valid numeric values.");
            updatePieChart(0);
        }
    }

    // ---------- Back button (matches your FXML onAction="#goBackHome") ----------
    @FXML
    private void goBackHome(ActionEvent event) {
        switchScene("/HomePage.fxml", event);
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

    // ---------- Helpers ----------
    private void resetPercents() {
        lblProteinPercent.setText("0 %");
        lblCaloriesPercent.setText("0 %");
        lblFatPercent.setText("0 %");
        lblCarbsPercent.setText("0 %");
        lblVitaminsPercent.setText("0 %");
    }

    private void updatePieChart(double totalCalories) {
        if (caloriePieChart == null) return;

        double dailyGoal = 2000;
        double consumed  = Math.min(totalCalories, dailyGoal);
        double remaining = Math.max(0, dailyGoal - consumed);

        caloriePieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Consumed", consumed),
                new PieChart.Data("Remaining", remaining)
        ));

        caloriePieChart.setTitle("Calories vs remaining");

        if (lblCaloriesCenter != null) {
            lblCaloriesCenter.setText(String.format("%.0f cal", totalCalories));
        }
    }

    private String getCalorieAdvice(double totalCalories,
                                    double proteinPercent,
                                    double fatPercent) {

        double dailyGoal     = 2000;
        double percentOfGoal = (totalCalories / dailyGoal) * 100;

        StringBuilder advice = new StringBuilder();
        advice.append(String.format(
                "Total calories: %.0f kcal | ~%.0f%% of daily intake. ",
                totalCalories, percentOfGoal));

        if (proteinPercent < 15) {
            advice.append("Low protein. Add eggs, lean meat, or beans. ");
        }
        if (fatPercent > 40) {
            advice.append("High fat. Try lighter ingredients. ");
        }
        if (totalCalories > 800) {
            advice.append("Very high calorie. Consider a smaller portion. ");
        }
        if (advice.length() == 0) {
            advice.append("Good nutritional balance.");
        }

        return advice.toString();
    }
}