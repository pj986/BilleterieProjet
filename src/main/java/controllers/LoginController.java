package controllers;

import app.SceneManager;
import app.Session;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import models.User;

public class LoginController {

    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lblMessage;

    @FXML
    private void login() {

        lblMessage.setVisible(true);

        String email = tfEmail.getText();
        String password = tfPassword.getText();

        System.out.println("[AUTH] Tentative login : " + email);

        if (email.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Veuillez remplir tous les champs.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        User user = UserDAO.login(email, password);

        if (user != null) {



            Session.login(user.getEmail(), user.getRole());

            System.out.println("[AUTH] Connexion réussie pour : " + user.getEmail());

            lblMessage.setText("Connexion réussie !");
            lblMessage.getStyleClass().setAll("label-success");

            // 🔥 UTILISATION DU SCENEMANAGER
            app.SceneManager.switchScene(tfEmail, "/views/Home.fxml");

            return;
        }

        System.out.println("[AUTH] Échec connexion");

        lblMessage.setText("Email ou mot de passe incorrect.");
        lblMessage.getStyleClass().setAll("label-error");
    }

    @FXML
    private void goToRegister(javafx.event.ActionEvent event) {

        System.out.println("[NAV] → Inscription demandée");

        try {
            app.SceneManager.switchScene(event, "/views/Inscription.fxml");
            System.out.println("[NAV] → Succès : écran Inscription chargé");
        } catch (Exception e) {
            System.err.println("[NAV] ❌ Erreur navigation vers Inscription");
            e.printStackTrace();
        }
    }
    @FXML
    private void goToForgotPassword(ActionEvent event) {
        SceneManager.switchScene(event, "/views/ForgotPassword.fxml");
    }
}
