package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Stack;

public class SceneManager {

    private static final String CSS_PATH = "/styles/app.css";

    private static final Stack<String> history = new Stack<>();
    private static String currentScenePath = null;

    public static void setInitialScene(String fxmlPath) {
        currentScenePath = fxmlPath;
    }

    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            if (currentScenePath != null && !currentScenePath.equals(fxmlPath)) {
                history.push(currentScenePath);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loadScene(stage, fxmlPath);
            currentScenePath = fxmlPath;

            System.out.println("Changement de scène vers : " + fxmlPath);

        } catch (Exception e) {
            System.err.println("Erreur de navigation vers : " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void switchScene(Node node, String fxmlPath) {
        try {
            if (currentScenePath != null && !currentScenePath.equals(fxmlPath)) {
                history.push(currentScenePath);
            }

            Stage stage = (Stage) node.getScene().getWindow();
            loadScene(stage, fxmlPath);
            currentScenePath = fxmlPath;

            System.out.println("Changement de scène vers : " + fxmlPath);

        } catch (Exception e) {
            System.err.println("Erreur de navigation vers : " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            if (currentScenePath != null && !currentScenePath.equals(fxmlPath)) {
                history.push(currentScenePath);
            }

            loadScene(stage, fxmlPath);
            currentScenePath = fxmlPath;

            System.out.println("Changement de scène vers : " + fxmlPath);

        } catch (Exception e) {
            System.err.println("Erreur de navigation vers : " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void goBack(ActionEvent event) {
        try {
            if (history.isEmpty()) {
                System.out.println("Aucune page précédente.");
                return;
            }

            String previousScene = history.pop();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loadScene(stage, previousScene);
            currentScenePath = previousScene;

            System.out.println("Retour vers : " + previousScene);

        } catch (Exception e) {
            System.err.println("Erreur lors du retour arrière.");
            e.printStackTrace();
        }
    }

    private static void loadScene(Stage stage, String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(
                        SceneManager.class.getResource(fxmlPath),
                        "Erreur : FXML introuvable pour le chemin " + fxmlPath
                )
        );

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        SceneManager.class.getResource(CSS_PATH),
                        "Erreur : CSS introuvable pour le chemin " + CSS_PATH
                ).toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }
}