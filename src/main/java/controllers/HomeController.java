package controllers;

import app.SceneManager;
import app.Session;
import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
public class HomeController {

    @FXML private Label lblUser;
    @FXML private Button btnEvents;   // bouton vers les événements
    @FXML private Button btnAdmin; // bouton admin (si présent)
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Label lblReservations;

    @FXML
    public void initialize() {

        if (Session.isLogged()) {

            String email = Session.getEmail();

            lblUser.setText("👤 " + email);

            int nb = ReservationDAO.countByEmail(email);

            lblReservations.setText("🎟️ " + nb + " réservations");

            btnLogin.setVisible(false);
            btnRegister.setVisible(false);
            btnAdmin.setVisible(Session.isAdmin());

        } else {

            lblUser.setText("👤 Invité");
            lblReservations.setText("");

            btnAdmin.setVisible(false);
            btnLogin.setVisible(true);
            btnRegister.setVisible(true);
        }
    }

    @FXML
    private void goToEvents() {
        SceneManager.switchScene(btnEvents, "/views/Events.fxml");
    }

    @FXML
    private void goToAdmin() {
        if (Session.isAdmin()) {
            SceneManager.switchScene(btnAdmin, "/views/AdminDashboard.fxml");
        }
    }

    @FXML
    private void logout(javafx.event.ActionEvent event) {

        System.out.println("[AUTH] Logout");

        Session.logout();

        app.SceneManager.switchScene(event, "/views/Login.fxml");
    }
    @FXML
    private void goToEvents(javafx.event.ActionEvent event) {
        app.SceneManager.switchScene(event, "/views/Events.fxml");
    }


    @FXML
    private void goToLogin(javafx.event.ActionEvent event) {
        app.SceneManager.switchScene(event, "/views/Login.fxml");
    }


    @FXML
    private void goToRegister(javafx.event.ActionEvent event) {
        app.SceneManager.switchScene(event, "/views/Inscription.fxml");
    }


    @FXML
    private void goToAdmin(javafx.event.ActionEvent event) {
        app.SceneManager.switchScene(event, "/views/AdminDashboard.fxml");
    }
    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }
}