package com.alsiyabii.calculatorapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the JavaFX Calculator application.
 * <p>
 * This class initializes the primary stage, sets up the scene with the CalculatorModel UI,
 * and applies the CSS styles for the application.
 */
public class MainApplication extends Application {

    /**
     * Starts the JavaFX application by setting up the primary stage and scene.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage stage)  {
        CalculatorModel calculatorModel = new CalculatorModel(new StringBuilder());
        Scene scene = new Scene(calculatorModel.getRoot());

        // Setting app title and icon
        stage.setTitle("Calculator");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/alsiyabii/calculatorapp/logo.jpg")));

        // Adding CSS styles to the main app's window
        String css = this.getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method serves as the standard Java entry point.
     * It launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
