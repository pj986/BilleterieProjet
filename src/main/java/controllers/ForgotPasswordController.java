package controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import app.SceneManager;
import database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import java.time.LocalDateTime;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField codeField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleGenerateCode() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            messageLabel.setText("Veuillez saisir votre email.");
            return;
        }

        if (!adminExists(email)) {
            messageLabel.setText("Aucun compte administrateur trouvé avec cet email.");
            return;
        }

        String resetCode = generateCode();

        String sql = """
                UPDATE administrateur
                SET reset_code = ?, reset_code_expiration = DATE_ADD(NOW(), INTERVAL 10 MINUTE)
                WHERE email = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resetCode);
            ps.setString(2, email);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                /*
                 * Version simple pour ton projet BTS :
                 * On affiche le code à l'écran.
                 *
                 * Version professionnelle :
                 * Envoyer ce code par email.
                 */
                messageLabel.setText("Code généré : " + resetCode + " valable 10 minutes.");
            } else {
                messageLabel.setText("Erreur lors de la génération du code.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur de connexion à la base de données.");
        }
    }

    @FXML
    private void handleResetPassword() {
        String email = emailField.getText().trim();
        String code = codeField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || code.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        if (newPassword.length() < 8) {
            messageLabel.setText("Le mot de passe doit contenir au moins 8 caractères.");
            return;
        }

        if (!isResetCodeValid(email, code)) {
            messageLabel.setText("Code invalide ou expiré.");
            return;
        }

        String hashedPassword = BCrypt.withDefaults()
                .hashToString(12, newPassword.toCharArray());

        String sql = """
                UPDATE administrateur
                SET mot_de_passe = ?, reset_code = NULL, reset_code_expiration = NULL
                WHERE email = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                messageLabel.setText("Mot de passe réinitialisé avec succès.");
                clearFields();
            } else {
                messageLabel.setText("Erreur lors de la réinitialisation.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur de connexion à la base de données.");
        }
    }

    private boolean adminExists(String email) {
        String sql = "SELECT id_admin FROM administrateur WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isResetCodeValid(String email, String code) {
        String sql = """
                SELECT id_admin
                FROM administrateur
                WHERE email = ?
                  AND reset_code = ?
                  AND reset_code_expiration > NOW()
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, code);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void clearFields() {
        codeField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    private void goBackToLogin(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Login.fxml");
    }
}