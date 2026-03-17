module com.example.aurafarming {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.aurafarming to javafx.fxml;
    exports com.example.aurafarming;
}