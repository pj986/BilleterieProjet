package app;

import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Chargement de la première page (à modifier si nécessaire)
            //Parent root = FXMLLoader.load(getClass().getResource("/views/MainScene.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/views/inscription.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/views/Home.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/views/Events.fxml"));
            Database.getConnection();

            // Création de la scène
            Scene scene = new Scene(root);

            // Ajout du fichier CSS global
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            // Fenêtre
            stage.setTitle("Application Billetterie");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors du chargement de l'application.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
