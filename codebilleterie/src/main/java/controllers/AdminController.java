package controllers;

import dao.EvenementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Evenement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminController {

    @FXML private Label lblMessage;
    @FXML private TextField tfTitle;
    @FXML private TextField tfCategory;
    @FXML private TextField tfDate;
    @FXML private TextField tfLocation;
    @FXML private TextField tfPrice;
    @FXML private TextField tfImage;

    @FXML
    private void addEvent() {
        lblMessage.setVisible(true);

        String title = tfTitle.getText();
        String category = tfCategory.getText();
        String date = tfDate.getText();
        String location = tfLocation.getText();
        String price = tfPrice.getText();
        String image = tfImage.getText();

        if (image == null || image.isEmpty()) {
            image = "https://picsum.photos/300/150";
        }

        // Vérifier que tous les champs sont remplis
        if (title.isEmpty() || category.isEmpty() || date.isEmpty() || location.isEmpty() || price.isEmpty()) {
            lblMessage.setText("Tous les champs doivent être remplis.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // Vérification du prix
        double parsedPrice = 0;
        try {
            parsedPrice = Double.parseDouble(price);
            if (parsedPrice <= 0) {
                lblMessage.setText("Le prix doit être supérieur à zéro.");
                lblMessage.getStyleClass().setAll("label-error");
                return;
            }
        } catch (NumberFormatException e) {
            lblMessage.setText("Le prix doit être un nombre valide.");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // Vérification du format de la date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (Exception e) {
            lblMessage.setText("Le format de la date doit être : yyyy-MM-dd");
            lblMessage.getStyleClass().setAll("label-error");
            return;
        }

        // Créer l'événement
        Evenement evenement = new Evenement(
                title,
                category,
                date,
                location,
                parsedPrice,
                image
        );

        // Ajouter l'événement à la base de données
        boolean success = EvenementDAO.addEvent(evenement);

        if (success) {
            lblMessage.setText("Événement ajouté avec succès !");
            lblMessage.getStyleClass().setAll("label-success");
        } else {
            lblMessage.setText("Erreur lors de l'ajout de l'événement.");
            lblMessage.getStyleClass().setAll("label-error");
        }
    }
}