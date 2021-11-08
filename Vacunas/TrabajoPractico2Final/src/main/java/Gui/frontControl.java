package Gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import transacciones.Departamento;
import transacciones.Departamentos;

import java.io.File;
import java.util.Collection;

public class frontControl {
    public Button closeButton;
    public Button browseButton;
    public Button loadButton;

    public ComboBox selectCMB;
    public ComboBox taskCMB;
    public ListView displayLV;
    public Label pathLBL;
    public Label rbLBL;
    public Label taskLBL;
    public Label currentLBL;
    public Pane pane2;
    public Pane pane3;
    public Pane pane4;
    public AnchorPane containerAP;


    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void browseButtonAction(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(null);
        if (file != null){
            pathLBL.setText(file.getAbsolutePath());}
        loadButton.setDisable(false);
        currentLBL.setText("Trabajando en su pedido...");
    }

    public void loadButtonAction(ActionEvent actionEvent) {
        Departamentos departamentos = new Departamentos();
        departamentos.contarVacunas(pathLBL.getText()+ "\\datos_nomivac_covid19.csv");
        currentLBL.setText("Solucion");
        pane3.setDisable(false);
        pane4.setDisable(false);
        selectCMB.setDisable(false);


        ObservableList ol;
        Collection deptos = departamentos.getDeptos();
        ol = FXCollections.observableArrayList(deptos);
        selectCMB.setItems(ol);

        ol = FXCollections.observableArrayList("Por_dosis","Por_sexo","Por_vacuna");
        taskCMB.setItems(ol);
    }

   /* public void oneRBAction(ActionEvent actionEvent) {
        selectCMB.setDisable(false);
    }

    public void allRBAction(ActionEvent actionEvent) {
        selectCMB.setDisable(true);
    }*/


    public void taskCMBAction(ActionEvent actionEvent) {

        ObservableList ol;


        Departamento depto = (Departamento) selectCMB.getValue();

        if (taskCMB.getValue() == "Por_dosis") {
            ol = FXCollections.observableArrayList(depto.getPrimeraDosis(),depto.getSegundaDosis());
            displayLV.setItems(ol);
        }
        else if (taskCMB.getValue() == "Por_sexo") {
            ol = FXCollections.observableArrayList(depto.getVacHombre(),depto.getVacMujer());
            displayLV.setItems(ol);
        }
        else {
            ol = FXCollections.observableArrayList(depto.getVacunas());
            displayLV.setItems(ol);
        }


    }
}