package controllers;

import app.SceneManager;
import dao.ClientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Client;

import java.util.Optional;

public class ClientsController {

    @FXML
    private TextField searchField;

    @FXML
    private Label lblCount;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, Integer> idColumn;

    @FXML
    private TableColumn<Client, String> nomColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> telephoneColumn;

    @FXML
    private TableColumn<Client, String> adresseColumn;

    @FXML
    private TableColumn<Client, String> dateColumn;

    private final ObservableList<Client> clients =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("idClient")
        );

        nomColumn.setCellValueFactory(
                new PropertyValueFactory<>("nom")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );

        telephoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("telephone")
        );

        adresseColumn.setCellValueFactory(
                new PropertyValueFactory<>("adresse")
        );

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateInscriptionFormatee")
        );

        clientsTable.setItems(clients);

        refreshClients();
    }

    @FXML
    private void refreshClients() {
        clients.clear();
        clients.addAll(ClientDAO.getAll());

        updateCount();
        showMessage(
                clients.size() + " client(s) chargé(s).",
                false
        );
    }

    @FXML
    private void handleSearch() {

        String keyword = searchField.getText();

        clients.clear();

        if (keyword == null || keyword.isBlank()) {
            clients.addAll(ClientDAO.getAll());
        } else {
            clients.addAll(ClientDAO.search(keyword));
        }

        updateCount();
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        refreshClients();
    }

    @FXML
    private void openAddForm() {

        Dialog<Client> dialog = createClientDialog(
                "Ajouter un client",
                null
        );

        Optional<Client> result = dialog.showAndWait();

        result.ifPresent(client -> {

            if (ClientDAO.emailExists(client.getEmail())) {
                showMessage(
                        "Cette adresse email est déjà utilisée.",
                        true
                );
                return;
            }

            boolean success = ClientDAO.create(client);

            if (success) {
                showMessage(
                        "Client ajouté avec succès.",
                        false
                );
                refreshClients();
            } else {
                showMessage(
                        "Erreur lors de l'ajout du client.",
                        true
                );
            }
        });
    }

    @FXML
    private void handleEdit() {

        Client selected =
                clientsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Veuillez sélectionner un client à modifier.",
                    true
            );
            return;
        }

        Dialog<Client> dialog = createClientDialog(
                "Modifier le client",
                selected
        );

        Optional<Client> result = dialog.showAndWait();

        result.ifPresent(updatedClient -> {

            updatedClient.setIdClient(selected.getIdClient());

            boolean success = ClientDAO.update(updatedClient);

            if (success) {
                showMessage(
                        "Client modifié avec succès.",
                        false
                );
                refreshClients();
            } else {
                showMessage(
                        "Erreur lors de la modification.",
                        true
                );
            }
        });
    }

    @FXML
    private void handleDelete() {

        Client selected =
                clientsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Veuillez sélectionner un client à supprimer.",
                    true
            );
            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Suppression");
        confirmation.setHeaderText(
                "Supprimer le client " + selected.getNom() + " ?"
        );
        confirmation.setContentText(
                "Cette action est définitive."
        );

        Optional<ButtonType> response =
                confirmation.showAndWait();

        if (response.isPresent()
                && response.get() == ButtonType.OK) {

            boolean success =
                    ClientDAO.delete(selected.getIdClient());

            if (success) {
                showMessage(
                        "Client supprimé avec succès.",
                        false
                );
                refreshClients();
            } else {
                showMessage(
                        "Suppression impossible. "
                                + "Le client possède peut-être des billets.",
                        true
                );
            }
        }
    }

    @FXML
    private void handleDetails() {

        Client selected =
                clientsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Veuillez sélectionner un client.",
                    true
            );
            return;
        }

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Détails du client");
        alert.setHeaderText(selected.getNom());

        String details =
                "Identifiant : " + selected.getIdClient()
                        + "\nEmail : " + valueOrDash(selected.getEmail())
                        + "\nTéléphone : " + valueOrDash(selected.getTelephone())
                        + "\nAdresse : " + valueOrDash(selected.getAdresse())
                        + "\nInscription : "
                        + selected.getDateInscriptionFormatee();

        alert.setContentText(details);
        alert.showAndWait();
    }

    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }

    private Dialog<Client> createClientDialog(
            String title,
            Client existingClient
    ) {

        Dialog<Client> dialog = new Dialog<>();

        dialog.setTitle(title);
        dialog.setHeaderText(
                existingClient == null
                        ? "Renseignez les informations du client."
                        : "Modifiez les informations du client."
        );

        ButtonType saveButtonType =
                new ButtonType(
                        "Enregistrer",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(saveButtonType, ButtonType.CANCEL);

        TextField nomField = new TextField();
        TextField emailField = new TextField();
        TextField telephoneField = new TextField();
        TextArea adresseArea = new TextArea();

        nomField.setPromptText("Nom du client");
        emailField.setPromptText("Adresse email");
        telephoneField.setPromptText("Téléphone");
        adresseArea.setPromptText("Adresse");
        adresseArea.setPrefRowCount(3);

        if (existingClient != null) {
            nomField.setText(existingClient.getNom());
            emailField.setText(existingClient.getEmail());
            telephoneField.setText(existingClient.getTelephone());
            adresseArea.setText(existingClient.getAdresse());
        }

        javafx.scene.layout.GridPane grid =
                new javafx.scene.layout.GridPane();

        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);

        grid.add(new Label("Email :"), 0, 1);
        grid.add(emailField, 1, 1);

        grid.add(new Label("Téléphone :"), 0, 2);
        grid.add(telephoneField, 1, 2);

        grid.add(new Label("Adresse :"), 0, 3);
        grid.add(adresseArea, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Button saveButton =
                (Button) dialog.getDialogPane()
                        .lookupButton(saveButtonType);

        saveButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {

                    String nom = nomField.getText().trim();
                    String email = emailField.getText().trim();

                    if (nom.isEmpty() || email.isEmpty()) {
                        showMessage(
                                "Le nom et l'email sont obligatoires.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    if (!email.matches(
                            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                    )) {
                        showMessage(
                                "Le format de l'email est invalide.",
                                true
                        );
                        event.consume();
                    }
                }
        );

        dialog.setResultConverter(buttonType -> {

            if (buttonType == saveButtonType) {

                return new Client(
                        nomField.getText().trim(),
                        emailField.getText().trim(),
                        telephoneField.getText().trim(),
                        adresseArea.getText().trim()
                );
            }

            return null;
        });

        return dialog;
    }

    private void updateCount() {
        lblCount.setText(
                clients.size() + " client(s)"
        );
    }

    private void showMessage(
            String message,
            boolean error
    ) {
        messageLabel.setText(message);

        messageLabel.setStyle(
                error
                        ? "-fx-text-fill: #dc2626; -fx-font-weight: bold;"
                        : "-fx-text-fill: #059669; -fx-font-weight: bold;"
        );
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank()
                ? "-"
                : value;
    }
}