package controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscriptionController {

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private PasswordField tfConfirmPassword;
    @FXML
    private Label lblMessage;

    @FXML
    private void enregistrementInscription() {
        lblMessage.setVisible(true);

        String nom = tfNom.getText();
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        String confirmPassword = tfConfirmPassword.getText();

        if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            lblMessage.setText("Veuillez remplir tous les champs.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblMessage.setText("Les mots de passe ne correspondent pas.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // Hash password
        String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        boolean success = UserDAO.createUser(email, hash);

        if (success) {
            lblMessage.setText("Compte créé avec succès !");
            lblMessage.getStyleClass().setAll("label-success");
            switchScene("/views/Login.fxml");
        } else {
            lblMessage.setText("Erreur lors de la création du compte.");
            lblMessage.getStyleClass().setAll("label-error");
        }
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.err.println("Erreur changement de scène vers : " + fxmlPath);
            e.printStackTrace();
        }
    }
}