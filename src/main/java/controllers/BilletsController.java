package controllers;

import dao.BilletDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Billet;
import app.SceneManager;
import javafx.event.ActionEvent;

import java.util.UUID;

public class BilletsController {

    @FXML
    private TextField searchField;

    @FXML
    private TextField clientIdField;

    @FXML
    private TextField seanceIdField;

    @FXML
    private TextField prixFinalField;

    @FXML
    private TextField placeField;

    @FXML
    private ComboBox<String> tarifCombo;

    @FXML
    private ComboBox<String> statutCombo;

    @FXML
    private TableView<Billet> billetTable;

    @FXML
    private TableColumn<Billet, Integer> idColumn;

    @FXML
    private TableColumn<Billet, String> numeroColumn;

    @FXML
    private TableColumn<Billet, Integer> clientColumn;

    @FXML
    private TableColumn<Billet, Integer> seanceColumn;

    @FXML
    private TableColumn<Billet, String> tarifColumn;

    @FXML
    private TableColumn<Billet, Double> prixColumn;

    @FXML
    private TableColumn<Billet, String> placeColumn;

    @FXML
    private TableColumn<Billet, String> statutColumn;

    @FXML
    private TableColumn<Billet, String> dateColumn;

    @FXML
    private Label messageLabel;

    private final ObservableList<Billet> billets = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tarifCombo.getItems().addAll("plein", "reduit", "groupe", "etudiant", "enfant");
        statutCombo.getItems().addAll("valide", "annule", "rembourse", "utilise");

        tarifCombo.setValue("plein");
        statutCombo.setValue("valide");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numeroUnique"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        seanceColumn.setCellValueFactory(new PropertyValueFactory<>("idSeance"));
        tarifColumn.setCellValueFactory(new PropertyValueFactory<>("categorieTarif"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixFinal"));
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("placeNumero"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));

        loadBillets();
    }

    @FXML
    public void loadBillets() {
        billets.clear();
        billets.addAll(BilletDAO.getAll());
        billetTable.setItems(billets);
        messageLabel.setText("Billets chargés.");
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();

        billets.clear();

        if (keyword.isEmpty()) {
            billets.addAll(BilletDAO.getAll());
        } else {
            billets.addAll(BilletDAO.search(keyword));
        }

        billetTable.setItems(billets);
        messageLabel.setText(billets.size() + " billet(s) trouvé(s).");
    }

    @FXML
    private void handleCreate() {
        try {
            int idClient = Integer.parseInt(clientIdField.getText().trim());
            int idSeance = Integer.parseInt(seanceIdField.getText().trim());
            double prixFinal = Double.parseDouble(prixFinalField.getText().trim());

            String categorieTarif = tarifCombo.getValue();
            String statut = statutCombo.getValue();
            String placeNumero = placeField.getText().trim();

            if (categorieTarif == null || statut == null || placeNumero.isEmpty()) {
                messageLabel.setText("Veuillez remplir tous les champs.");
                return;
            }

            Billet billet = new Billet();
            billet.setNumeroUnique(generateNumeroUnique());
            billet.setIdClient(idClient);
            billet.setIdSeance(idSeance);
            billet.setPrixFinal(prixFinal);
            billet.setCategorieTarif(categorieTarif);
            billet.setPlaceNumero(placeNumero);
            billet.setStatut(statut);
            billet.setQrCode("QR-" + billet.getNumeroUnique());

            boolean success = BilletDAO.create(billet);

            if (success) {
                messageLabel.setText("Billet créé avec succès.");
                clearForm();
                loadBillets();
            } else {
                messageLabel.setText("Erreur lors de la création du billet.");
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("ID client, ID séance et prix doivent être valides.");
        }
    }

    @FXML
    private void handleValidate() {
        updateSelectedBilletStatus("valide");
    }

    @FXML
    private void handleCancel() {
        updateSelectedBilletStatus("annule");
    }

    @FXML
    private void handleRefund() {
        updateSelectedBilletStatus("rembourse");
    }

    private void updateSelectedBilletStatus(String statut) {
        Billet selected = billetTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Veuillez sélectionner un billet.");
            return;
        }

        boolean success = BilletDAO.updateStatus(selected.getId(), statut);

        if (success) {
            messageLabel.setText("Statut mis à jour : " + statut);
            loadBillets();
        } else {
            messageLabel.setText("Erreur lors de la mise à jour du statut.");
        }
    }

    private String generateNumeroUnique() {
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "TNG-" + random;
    }

    private void clearForm() {
        clientIdField.clear();
        seanceIdField.clear();
        prixFinalField.clear();
        placeField.clear();
        tarifCombo.setValue("plein");
        statutCombo.setValue("valide");
    }
    @FXML
    private void goToDashboard(ActionEvent event) {
        SceneManager.switchScene(event, "/views/AdminDashboard.fxml");
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


}