package controllers;

import app.Session;
import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import models.Evenement;
import app.SceneManager;
import javafx.event.ActionEvent;

import java.util.List;

public class ReservationsController {

    @FXML
    private FlowPane reservationsContainer;

    @FXML
    public void initialize() {
        loadReservations();
    }

    private void loadReservations() {

        String email = Session.getEmail();

        if (email == null) {
            System.out.println("Pas connecté !");
            return;
        }

        List<Evenement> list = ReservationDAO.getByUser(email);

        reservationsContainer.getChildren().clear();

        for (Evenement event : list) {

            VBox card = new VBox();
            card.getStyleClass().add("card-event");
            card.setSpacing(10);

            Label title = new Label(event.getTitre());
            title.getStyleClass().add("event-title");

            Label date = new Label(event.getDate());
            date.getStyleClass().add("event-info");

            Label prix = new Label("Prix : " + event.getPrix() + "€");

            card.getChildren().addAll(title, date, prix);

            reservationsContainer.getChildren().add(card);
        }
    }
    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }
}