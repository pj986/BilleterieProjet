package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MainSceneController {

    @FXML
    private TextField tfTitre;

    @FXML
    void btnOk(ActionEvent event) {
        Stage mainWindow = (stage) tfTitre.getScene().getWindow();
        mainWindow.setTitle(tfTitre.getText());
        System.out.println("test");

    }

}