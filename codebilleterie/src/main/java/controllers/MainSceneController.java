package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML
    private TextField tfTitre;

    @FXML
    void btnOk() {
        Stage mainWindow = (Stage) tfTitre.getScene().getWindow();
        mainWindow.setTitle(tfTitre.getText());
        System.out.println("test");

    }

}