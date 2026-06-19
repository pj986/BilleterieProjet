package controllers;

import app.SceneManager;
import dao.EvenementDAO;
import dao.ReservationDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Evenement;
import dao.DashboardDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminController {

    // Champs utilisés sur la page d'ajout d'événement
    @FXML private Label lblMessage;
    @FXML private TextField tfTitle;
    @FXML private TextField tfCategory;
    @FXML private TextField tfDate;
    @FXML private TextField tfLocation;
    @FXML private TextField tfPrice;
    @FXML private TextField tfImage;

    // Champs utilisés sur le Dashboard
    @FXML private Label lblTotalReservations;
    @FXML private Label lblTotalEvents;
    @FXML private Label lblTotalClients;
    @FXML private Label lblChiffreAffaires;

    @FXML
    public void initialize() {
        System.out.println("[ADMIN] Initialisation du Dashboard");

        if (lblTotalReservations != null) {
            int totalBillets = DashboardDAO.countBillets();
            lblTotalReservations.setText(String.valueOf(totalBillets));
        }

        if (lblTotalEvents != null) {
            int totalEvents = DashboardDAO.countEvents();
            lblTotalEvents.setText(String.valueOf(totalEvents));
        }

        if (lblTotalClients != null) {
            int totalClients = DashboardDAO.countClients();
            lblTotalClients.setText(String.valueOf(totalClients));
        }

        if (lblChiffreAffaires != null) {
            double chiffreAffaires = DashboardDAO.getChiffreAffaires();
            lblChiffreAffaires.setText(String.format("%.2f €", chiffreAffaires));
        }
        if (lblBilletsValides != null) {
            lblBilletsValides.setText(String.valueOf(DashboardDAO.countBilletsByStatus("valide")));
        }

        if (lblBilletsAnnules != null) {
            lblBilletsAnnules.setText(String.valueOf(DashboardDAO.countBilletsByStatus("annule")));
        }

        if (lblBilletsRembourses != null) {
            lblBilletsRembourses.setText(String.valueOf(DashboardDAO.countBilletsByStatus("rembourse")));
        }

        if (lblTotalSeances != null) {
            lblTotalSeances.setText(String.valueOf(DashboardDAO.countSeances()));
        }
    }

    @FXML
    private void addEvent() {
        if (lblMessage == null || tfTitle == null || tfCategory == null || tfDate == null
                || tfLocation == null || tfPrice == null || tfImage == null) {
            System.err.println("[ADMIN] Formulaire événement introuvable dans cette page.");
            return;
        }

        lblMessage.setVisible(true);

        String title = tfTitle.getText().trim();
        String category = tfCategory.getText().trim();
        String date = tfDate.getText().trim();
        String location = tfLocation.getText().trim();
        String price = tfPrice.getText().trim();
        String image = tfImage.getText().trim();

        if (image.isEmpty()) {
            image = "https://picsum.photos/300/150";
        }

        if (title.isEmpty() || category.isEmpty() || date.isEmpty() || location.isEmpty() || price.isEmpty()) {
            showMessage("Tous les champs doivent être remplis.", "label-error");
            return;
        }

        double parsedPrice;

        try {
            parsedPrice = Double.parseDouble(price);

            if (parsedPrice <= 0) {
                showMessage("Le prix doit être supérieur à zéro.", "label-error");
                return;
            }

        } catch (NumberFormatException e) {
            showMessage("Le prix doit être un nombre valide.", "label-error");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            Date parsedDate = dateFormat.parse(date);
        } catch (Exception e) {
            showMessage("Le format de la date doit être : yyyy-MM-dd", "label-error");
            return;
        }

        Evenement evenement = new Evenement(
                title,
                category,
                date,
                location,
                parsedPrice,
                image
        );

        boolean success = EvenementDAO.addEvent(evenement);

        if (success) {
            showMessage("Événement ajouté avec succès !", "label-success");
            clearEventForm();
        } else {
            showMessage("Erreur lors de l'ajout de l'événement.", "label-error");
        }
    }

    private void showMessage(String message, String styleClass) {
        if (lblMessage != null) {
            lblMessage.setText(message);
            lblMessage.getStyleClass().setAll(styleClass);
        }
    }

    private void clearEventForm() {
        if (tfTitle != null) tfTitle.clear();
        if (tfCategory != null) tfCategory.clear();
        if (tfDate != null) tfDate.clear();
        if (tfLocation != null) tfLocation.clear();
        if (tfPrice != null) tfPrice.clear();
        if (tfImage != null) tfImage.clear();
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        SceneManager.switchScene(event, "/views/AdminDashboard.fxml");
    }

    @FXML
    private void goToBillets(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Billets.fxml");
    }

    @FXML
    private void goToEvents(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Events.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Login.fxml");
    }
    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }
    @FXML private Label lblBilletsValides;
    @FXML private Label lblBilletsAnnules;
    @FXML private Label lblBilletsRembourses;
    @FXML private Label lblTotalSeances;
    @FXML
    private void goToClients(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Clients.fxml");
    }

    @FXML
    private void goToSeances(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Seances.fxml");
    }

    @FXML
    private void goToTarifs(ActionEvent event) {
        SceneManager.switchScene(event, "/views/Tarifs.fxml");
    }
}