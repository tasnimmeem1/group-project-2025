package com.group.project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Recipe extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Recipe.class.getResource("/Login.fxml")  // Ensure this is correct
        );

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Recipe App Login");
        stage.setScene(scene);

        // 🔹 THIS WAS MISSING
        stage.setMaximized(true);  // Ensures full screen
        // OR stage.setFullScreen(true);  // Optional (true fullscreen mode)

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
