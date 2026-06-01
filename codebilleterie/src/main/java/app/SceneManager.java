package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneManager {

    public static void switchTo(Node anyNodeInScene, String fxmlPath) {
        try {

            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            SceneManager.class.getResource(fxmlPath),
                            "FXML introuvable : " + fxmlPath
                    )
            );

            Stage stage = (Stage) anyNodeInScene.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            System.err.println("Erreur changement de scène vers : " + fxmlPath);
            e.printStackTrace();
        }
    }
}
