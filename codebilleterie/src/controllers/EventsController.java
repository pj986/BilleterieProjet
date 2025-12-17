package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Evenement;

import java.io.FileInputStream;
import java.util.List;

import dao.EvenementDAO;

public class EventsController {
    @FXML
    private GridPane gridEvents;

    // Récupération de la fenêtre
    private Stage getStage() {
        return (Stage) Stage.getWindows().filtered(w -> w.isShowing()).get(0);
    }

    // Change de scène
    private void switchScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = getStage();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retour accueil
    @FXML
    private void goToHome() {
        switchScene("/views/Home.fxml");
    }

    // Génération des cartes d'événement
    @FXML
    private void initialize() {

        // Charger les événements depuis MySQL
    List<Evenement> events = EvenementDAO.getAll();
    System.out.println("Nombre d'événements chargés : " + events.size());

    int col = 0;
    int row = 0;

    try {
        for (Evenement evt : events) {

            VBox card = new VBox();
            card.getStyleClass().add("card-event");
            card.setSpacing(10);

            // Image
            ImageView img = new ImageView(new Image(evt.getImage()));
            img.setFitWidth(200);
            img.setFitHeight(120);
            img.setPreserveRatio(false);

            // Titre
            Label lblTitre = new Label(evt.getTitre());
            lblTitre.getStyleClass().add("event-title");

            // Date
            Label lblDate = new Label("📅 " + evt.getDate());
            lblDate.getStyleClass().add("event-info");

            // Lieu
            Label lblLieu = new Label("📍 " + evt.getLieu());
            lblLieu.getStyleClass().add("event-info");

            // Bouton acheter
            Button btn = new Button("Acheter");
            btn.getStyleClass().add("btn-primary");

            card.getChildren().addAll(img, lblTitre, lblDate, lblLieu, btn);

            gridEvents.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
