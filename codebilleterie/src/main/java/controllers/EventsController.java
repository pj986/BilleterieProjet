package controllers;

import dao.EvenementDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import models.Evenement;

import java.util.List;
import java.util.stream.Collectors;

public class EventsController {

    @FXML
    private VBox eventsContainer;

    @FXML
    private Label lblCount;

    @FXML
    private TextField searchField;

    private List<Evenement> allEvents;
    private String selectedCategory = "ALL";
    private String searchKeyword = "";
    private String sortType = "DATE_ASC";

    @FXML
    private void initialize() {

        // 🔹 Charger les événements depuis la base
        allEvents = EvenementDAO.getAll();

        // 🔥 RECHERCHE EN LIVE
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            searchKeyword = newVal.toLowerCase().trim();
            updateView();
        });

        updateView();
    }

    private void updateView() {

        List<Evenement> result = allEvents;

        // 🎯 FILTRE CATÉGORIE
        if (!selectedCategory.equals("ALL")) {
            result = result.stream()
                    .filter(e -> e.getCategorie().equalsIgnoreCase(selectedCategory))
                    .collect(Collectors.toList());
        }

        // 🔍 RECHERCHE TEXTE
        if (!searchKeyword.isEmpty()) {
            result = result.stream()
                    .filter(e -> e.getTitre().toLowerCase().contains(searchKeyword))
                    .collect(Collectors.toList());
        }

        // ⚡ TRI
        switch (sortType) {
            case "DATE_ASC":
                result = result.stream()
                        .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                        .collect(Collectors.toList());
                break;

            case "DATE_DESC":
                result = result.stream()
                        .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                        .collect(Collectors.toList());
                break;

            case "PRICE_ASC":
                result = result.stream()
                        .sorted((e1, e2) -> Double.compare(e1.getPrix(), e2.getPrix()))
                        .collect(Collectors.toList());
                break;

            case "PRICE_DESC":
                result = result.stream()
                        .sorted((e1, e2) -> Double.compare(e2.getPrix(), e1.getPrix()))
                        .collect(Collectors.toList());
                break;
        }

        displayEvents(result);
    }

    private void displayEvents(List<Evenement> events) {

        eventsContainer.getChildren().clear();

        int count = events.size();
        lblCount.setText(count == 1 ? "1 événement trouvé" : count + " événements trouvés");

        for (Evenement evt : events) {

            VBox card = new VBox();
            card.setSpacing(8);

            // 🎨 STYLE CARD
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #e0e0e0;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
            );

            // 🖼 IMAGE ROBUSTE
            String imageUrl = evt.getImage();
            Image imgObj;

            if (imageUrl == null || imageUrl.isEmpty()) {
                imgObj = new Image("https://picsum.photos/300/150");
            } else {
                Image temp = new Image(imageUrl, true);
                imgObj = temp.isError()
                        ? new Image("https://picsum.photos/300/150")
                        : temp;
            }

            ImageView img = new ImageView(imgObj);
            img.setFitWidth(250);
            img.setFitHeight(150);
            img.setPreserveRatio(false);

            Label lblCategorie = new Label("🎭 " + evt.getCategorie().toUpperCase());
            Label lblTitre = new Label(evt.getTitre());
            Label lblDate = new Label("📅 " + evt.getDate());
            Label lblLieu = new Label("📍 " + evt.getLieu());
            Label lblPrix = new Label("💰 " + evt.getPrix() + " €");

            // 🔘 BOUTONS
            Button btnBuy = new Button("Acheter");
            Button btnDelete = new Button("Supprimer");
            Button btnEdit = new Button("Modifier");
            btnEdit.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

            btnEdit.setOnAction(e -> {

                Dialog<Evenement> dialog = new Dialog<>();
                dialog.setTitle("Modifier événement");

                // Champs
                TextField tfCategorie = new TextField(evt.getCategorie());
                TextField tfDate = new TextField(evt.getDate());
                TextField tfLieu = new TextField(evt.getLieu());
                TextField tfPrix = new TextField(String.valueOf(evt.getPrix()));
                TextField tfImage = new TextField(evt.getImage());

                VBox box = new VBox(10,
                        new Label("Catégorie"), tfCategorie,
                        new Label("Date"), tfDate,
                        new Label("Lieu"), tfLieu,
                        new Label("Prix"), tfPrix,
                        new Label("Image"), tfImage
                );

                dialog.getDialogPane().setContent(box);

                ButtonType btnSave = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(btnSave, ButtonType.CANCEL);

                dialog.setResultConverter(button -> {
                    if (button == btnSave) {

                        return new Evenement(
                                evt.getTitre(), // on garde le titre
                                tfCategorie.getText(),
                                tfDate.getText(),
                                tfLieu.getText(),
                                Double.parseDouble(tfPrix.getText()),
                                tfImage.getText()
                        );
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(updatedEvent -> {

                    boolean success = EvenementDAO.updateEvent(updatedEvent);

                    if (success) {
                        allEvents = EvenementDAO.getAll();
                        updateView();
                    }
                });
            });

            // 🎨 STYLE BOUTONS
            btnBuy.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            btnDelete.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

            // 🗑 DELETE AVEC CONFIRMATION
            btnDelete.setOnAction(e -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Supprimer cet événement ?");
                alert.setContentText(evt.getTitre());

                if (alert.showAndWait().get() == ButtonType.OK) {

                    boolean success = EvenementDAO.deleteEvent(evt.getTitre());

                    if (success) {
                        allEvents = EvenementDAO.getAll();
                        updateView();
                    }
                }
            });

            card.getChildren().addAll(
                    img,
                    lblCategorie,
                    lblTitre,
                    lblDate,
                    lblLieu,
                    lblPrix,
                    btnBuy,
                    btnEdit,
                    btnDelete
            );

            eventsContainer.getChildren().add(card);
        }
    }

    // 🎯 FILTRES
    @FXML
    private void filterAll() {
        selectedCategory = "ALL";
        updateView();
    }

    @FXML
    private void filterCinema() {
        selectedCategory = "cinema";
        updateView();
    }

    @FXML
    private void filterSport() {
        selectedCategory = "sport";
        updateView();
    }

    @FXML
    private void filterConcert() {
        selectedCategory = "concert";
        updateView();
    }

    // ⚡ TRI
    @FXML
    private void sortByDateAsc() {
        sortType = "DATE_ASC";
        updateView();
    }

    @FXML
    private void sortByDateDesc() {
        sortType = "DATE_DESC";
        updateView();
    }

    @FXML
    private void sortByPriceAsc() {
        sortType = "PRICE_ASC";
        updateView();
    }

    @FXML
    private void sortByPriceDesc() {
        sortType = "PRICE_DESC";
        updateView();
    }
}