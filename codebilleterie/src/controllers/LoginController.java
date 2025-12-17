package controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private Label lblMessage;

    // Récupère la fenêtre principale
    private Stage getStage() {
        return (Stage) Stage.getWindows().filtered(Window -> Window.isShowing()).get(0);
    }

    // Change de scène proprement
    private void switchScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = getStage();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔑 Connexion
    @FXML
    private void login() {
        lblMessage.setVisible(true);

        if (tfEmail.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            lblMessage.setText("Veuillez remplir tous les champs.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // 🔥 Ici tu mettras la connexion SQL + vérification du compte
        // Exemple de simulation :
        if (tfEmail.getText().equals("admin@admin.com")
                && tfPassword.getText().equals("admin")) {

            lblMessage.setText("Connexion réussie !");
            lblMessage.getStyleClass().setAll("label-success");

            // Aller vers la page d'accueil ou dashboard
            switchScene("/views/Home.fxml");
            return;
        }

        lblMessage.setText("Email ou mot de passe incorrect.");
        lblMessage.getStyleClass().setAll("label-error");
    }

    // ➡ Aller vers inscription
    @FXML
    private void goToRegister() {
        switchScene("/views/Inscription.fxml");
    }

    // ➡ Retour à l'accueil
    @FXML
    private void goToHome() {
        switchScene("/views/Home.fxml");
    }
    
}
