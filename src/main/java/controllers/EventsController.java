package controllers;

import app.Session;
import dao.EvenementDAO;
import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import models.Evenement;
import java.util.stream.Collectors;
import app.SceneManager;
import javafx.event.ActionEvent;

import java.util.Comparator;
import java.util.List;

public class EventsController {

    @FXML
    private FlowPane eventsContainer;

    @FXML
    private TextField searchField;

    @FXML
    private Label lblCount;

    private List<Evenement> allEvents;

    @FXML
    public void initialize() {
        allEvents = EvenementDAO.getAll();
        displayEvents(allEvents);
    }

    private void displayEvents(List<Evenement> events) {
        eventsContainer.getChildren().clear();

        if (lblCount != null) {
            lblCount.setText(events.size() + " événement(s) trouvé(s)");
        }

        for (Evenement event : events) {
            VBox card = new VBox();
            card.getStyleClass().add("card-event");
            card.setSpacing(10);
            card.setPrefWidth(230);

            Label title = new Label(event.getTitre());
            title.getStyleClass().add("event-title");

            Label category = new Label("Catégorie : " + event.getCategorie());
            category.getStyleClass().add("event-info");

            Label date = new Label("Date : " + event.getDate());
            date.getStyleClass().add("event-info");

            Label lieu = new Label("Lieu : " + event.getLieu());
            lieu.getStyleClass().add("event-info");

            Label prix = new Label("Prix : " + event.getPrix() + " €");
            prix.getStyleClass().add("event-info");

            Button btn = new Button("Réserver");
            btn.getStyleClass().add("btn-primary");

            if (Session.getEmail() != null &&
                    ReservationDAO.exists(Session.getEmail(), event.getId())) {

                btn.setText("Réservé ✔");
                btn.setDisable(true);
            }

            btn.setOnAction(e -> {
                String email = Session.getEmail();

                if (email == null) {
                    showAlert("Erreur", "Veuillez vous connecter pour réserver.");
                    return;
                }

                if (ReservationDAO.exists(email, event.getId())) {
                    showAlert("Information", "Vous avez déjà réservé cet événement.");
                    return;
                }

                ReservationDAO.create(email, event.getId());

                showAlert("Succès", "Réservation confirmée !");
                btn.setText("Réservé ✔");
                btn.setDisable(true);
            });

            card.getChildren().addAll(title, category, date, lieu, prix, btn);
            eventsContainer.getChildren().add(card);
        }
    }
    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }

    @FXML
    private void filterAll() {
        displayEvents(allEvents);
    }

    @FXML
    private void filterConcert() {
        filterByCategory("concert");
    }

    @FXML
    private void filterSport() {
        filterByCategory("sport");
    }

    @FXML
    private void filterCinema() {
        filterByCategory("cinéma");
    }

    @FXML
    private void filterSpectacle() {
        filterByCategory("spectacle");
    }

    private void filterByCategory(String category) {
        List<Evenement> filtered = allEvents.stream()
                .filter(e -> e.getCategorie() != null &&
                        e.getCategorie().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());

        displayEvents(filtered);
    }

    @FXML
    private void sortByDateAsc() {
        List<Evenement> sorted = allEvents.stream()
                .sorted(Comparator.comparing(Evenement::getDate))
                .collect(Collectors.toList());

        displayEvents(sorted);
    }

    @FXML
    private void sortByDateDesc() {
        List<Evenement> sorted = allEvents.stream()
                .sorted(Comparator.comparing(Evenement::getDate).reversed())
                .collect(Collectors.toList());

        displayEvents(sorted);
    }

    @FXML
    private void sortByPriceAsc() {
        List<Evenement> sorted = allEvents.stream()
                .sorted(Comparator.comparingDouble(Evenement::getPrix))
                .collect(Collectors.toList());

        displayEvents(sorted);
    }

    @FXML
    private void sortByPriceDesc() {
        List<Evenement> sorted = allEvents.stream()
                .sorted(Comparator.comparingDouble(Evenement::getPrix).reversed())
                .collect(Collectors.toList());

        displayEvents(sorted);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            displayEvents(allEvents);
            return;
        }

        List<Evenement> filtered = allEvents.stream()
                .filter(e ->
                        safe(e.getTitre()).contains(keyword)
                                || safe(e.getCategorie()).contains(keyword)
                                || safe(e.getLieu()).contains(keyword)
                )
                .collect(Collectors.toList());

        displayEvents(filtered);
    }

    private String safe(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}