package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class InscriptionController {
     @FXML
    private Label lblMessage;

    @FXML
    private PasswordField tfconfirmpassword;

    @FXML
    private PasswordField tfpassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private AnchorPane txtNom;

    @FXML
    void enregistrementInscription(ActionEvent event) {

        System.err.println("test");
    }
    
    private void register() {

        lblMessage.setVisible(true); // on affiche le label quand on veut envoyer un message

        // Vérification des champs vides
        if (tfNom.getText().isEmpty() || tfEmail.getText().isEmpty() ||
            tfpassword.getText().isEmpty() || tfconfirmpassword.getText().isEmpty()) {
            
            lblMessage.setText("Veuillez remplir tous les champs.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // Vérification du mot de passe
        if (!tfpassword.getText().equals(tfconfirmpassword.getText())) {
            lblMessage.setText("Les mots de passe ne correspondent pas.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // Si tout va bien → succès
        lblMessage.setText("Compte créé avec succès !");
        lblMessage.getStyleClass().setAll("label-success");
    }
    @FXML
    private void goToLogin() {
    switchScene("/views/Login.fxml");
}
}
