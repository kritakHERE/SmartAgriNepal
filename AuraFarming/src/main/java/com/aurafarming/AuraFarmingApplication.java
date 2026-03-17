package com.aurafarming;

import com.aurafarming.util.FileUtil;
import com.aurafarming.util.SceneRouter;
import javafx.application.Application;
import javafx.stage.Stage;

public class AuraFarmingApplication extends Application {
    @Override
    public void start(Stage stage) {
        FileUtil.ensureAppDirectories();
        SceneRouter.initialize(stage);
        SceneRouter.goTo("/com/aurafarming/view/login.fxml", "AuraFarming - Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
