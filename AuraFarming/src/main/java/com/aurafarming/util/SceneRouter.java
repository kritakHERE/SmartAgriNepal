package com.aurafarming.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneRouter {
    private static Stage stage;

    private SceneRouter() {
    }

    public static void initialize(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void goTo(String fxmlPath, String title) {
        if (stage == null) {
            throw new IllegalStateException("SceneRouter is not initialized.");
        }
        try {
            Parent root = FXMLLoader.load(SceneRouter.class.getResource(fxmlPath));
            Scene scene = new Scene(root, 1200, 760);
            scene.getStylesheets().add(SceneRouter.class.getResource("/com/aurafarming/css/app.css").toExternalForm());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load scene: " + fxmlPath, e);
        }
    }
}
