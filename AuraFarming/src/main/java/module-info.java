module com.aurafarming {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.aurafarming to javafx.fxml;
    opens com.aurafarming.controller to javafx.fxml;

    exports com.aurafarming;
    exports com.aurafarming.model;
}