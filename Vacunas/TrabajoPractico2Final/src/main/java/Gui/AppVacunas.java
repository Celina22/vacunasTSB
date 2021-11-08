package Gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AppVacunas extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppVacunas.class.getResource("vacunas-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 750);
        stage.setTitle("Bienvenido");
        stage.setScene(scene);
        stage.centerOnScreen();
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}