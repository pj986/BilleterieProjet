package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {
    private Stage getStage() {
        return (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
    }

    private void switchScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = getStage();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToEvents() {
        switchScene("/views/Events.fxml");
    }

    @FXML
    private void goToLogin() {
        switchScene("/views/Login.fxml");
    }

    @FXML
    private void goToRegister() {
        switchScene("/views/Inscription.fxml");
    }
    
    @FXML
    private void goToEvents() {
    switchScene("/views/Events.fxml");
}

}
    

