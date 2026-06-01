package controllers;

import app.SceneManager;
import app.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class HomeController {

    @FXML private Label lblUser;
    @FXML private Button btnEvents;   // bouton vers les événements
    @FXML private Button btnAdmin;    // bouton admin (si présent)

    @FXML
    public void initialize() {

        if (!Session.isLogged()) {
            SceneManager.switchTo(lblUser, "/views/Login.fxml");
            return;
        }

        lblUser.setText("Connecté : " + Session.getEmail());

        // Si utilisateur non admin, cacher bouton admin
        if (btnAdmin != null && !Session.isAdmin()) {
            btnAdmin.setVisible(false);
        }
    }

    @FXML
    private void goToEvents() {
        SceneManager.switchTo(btnEvents, "/views/Events.fxml");
    }

    @FXML
    private void goToAdmin() {
        if (Session.isAdmin()) {
            SceneManager.switchTo(btnAdmin, "/views/AdminDashboard.fxml");
        }
    }

    @FXML
    private void logout() {
        Session.logout();
        SceneManager.switchTo(lblUser, "/views/Login.fxml");
    }
}