module com.alsiyabii.calculatorapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.alsiyabii.calculatorapp to javafx.fxml;
    exports com.alsiyabii.calculatorapp;
}