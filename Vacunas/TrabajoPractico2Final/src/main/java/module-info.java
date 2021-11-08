module interfaz.hola {
    requires javafx.controls;
    requires javafx.fxml;


    opens Gui to javafx.fxml;
    exports Gui;
}