package controllers;

import app.SceneManager;
import app.Session;
import dao.ReservationDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeController {

    @FXML private Label lblUser;
    @FXML private Label lblReservations;
    @FXML private Label lblWelcome;
    @FXML private Label lblRole;
    @FXML private Label lblStatus;
    @FXML private Label lblAdminDetails;
    @FXML private Label lblMiniRole;
    @FXML private Label lblMiniEmail;
    @FXML private Label lblMiniReservations;


    @FXML private Button btnEvents;
    @FXML private Button btnAdmin;
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;


    @FXML
    public void initialize() {

        if (Session.isLogged()) {

            String email = Session.getEmail();
            int nb = ReservationDAO.countByEmail(email);

            lblUser.setText("👤 " + email);
            lblReservations.setText("🎟️ " + nb + " réservation(s)");
            lblMiniReservations.setText(String.valueOf(nb));
            lblMiniEmail.setText(email);

            if (Session.isAdmin()) {
                lblRole.setText("ADMINISTRATEUR");
                lblWelcome.setText("Bienvenue dans votre console d’administration");
                lblStatus.setText("Compte administrateur authentifié et actif");
                lblAdminDetails.setText("Le compte " + email + " est connecté avec les privilèges d’administration.");
                lblMiniRole.setText("Administrateur");
                btnAdmin.setVisible(true);
            } else {
                lblRole.setText("UTILISATEUR");
                lblWelcome.setText("Bienvenue sur la billetterie");
                lblStatus.setText("Compte utilisateur connecté");
                lblAdminDetails.setText("Compte standard connecté à l’application.");
                lblMiniRole.setText("Utilisateur");
                btnAdmin.setVisible(false);
            }

            btnLogin.setVisible(false);
            btnRegister.setVisible(false);

        } else {

            lblUser.setText("👤 Invité");
            lblReservations.setText("🎟️ 0 réservation");
            lblWelcome.setText("Bienvenue sur la plateforme Tic’n Go");
            lblRole.setText("VISITEUR");
            lblStatus.setText("Aucune session ouverte");
            lblAdminDetails.setText("Aucun compte administrateur n’est connecté actuellement.");
            lblMiniRole.setText("Visiteur");
            lblMiniEmail.setText("Non connecté");
            lblMiniReservations.setText("0");

            btnAdmin.setVisible(false);
            btnLogin.setVisible(true);
            btnRegister.setVisible(true);
        }
    }

    private void refreshHome() {

        if (Session.isLogged()) {

            String email = Session.getEmail();

            lblUser.setText("👤 " + email);

            refreshReservationCount();

            btnLogin.setVisible(false);
            btnRegister.setVisible(false);
            btnAdmin.setVisible(Session.isAdmin());

        } else {

            lblUser.setText("👤 Invité");
            lblReservations.setText("🎟️ 0 réservation");

            btnAdmin.setVisible(false);
            btnLogin.setVisible(true);
            btnRegister.setVisible(true);
        }
    }

    private void refreshReservationCount() {

        String email = Session.getEmail();

        if (email == null || email.isBlank()) {
            lblReservations.setText("🎟️ 0 réservation");
            return;
        }

        int nb = ReservationDAO.countByEmail(email);

        if (nb <= 1) {
            lblReservations.setText("🎟️ " + nb + " réservation");
        } else {
            lblReservations.setText("🎟️ " + nb + " réservations");
        }

        System.out.println("[HOME] Réservations de " + email + " : " + nb);
    }

    @FXML
    private void goToEvents(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Events.fxml");
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Login.fxml");
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Inscription.fxml");
    }

    @FXML
    private void goToAdmin(ActionEvent event) {

        if (Session.isAdmin()) {
            SceneManager.switchScene(event, "/views/AdminDashboard.fxml");
        }
    }

    @FXML
    private void logout(ActionEvent event) {

        System.out.println("[AUTH] Logout");

        Session.logout();

        SceneManager.switchScene(event, "/views/Login.fxml");
    }

    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }
}