module fi.tuni {
    requires javafx.fxml;
    requires javafx.controls;
    requires commons.math3;
    opens fi.tuni to javafx.graphics;
    exports fi.tuni;
}