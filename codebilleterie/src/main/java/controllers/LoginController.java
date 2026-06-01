package controllers;

import app.Session;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        if (email.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Veuillez remplir tous les champs.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        User user = UserDAO.login(email, password);

        if (user != null) {
            Session.login(user.getEmail(), user.getRole());
            lblMessage.setText("Connexion réussie !");
            lblMessage.getStyleClass().setAll("label-success");

            switchScene("/views/Home.fxml");
            return;
        }

        lblMessage.setText("Email ou mot de passe incorrect.");
        lblMessage.getStyleClass().setAll("label-error");
    }

    private void switchScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}