package controllers;

import app.SceneManager;
import app.Session;
import dao.AdministrateurDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.Administrateur;

import java.util.Optional;

public class AdministrateursController {

    @FXML private TextField searchField;
    @FXML private Label lblCount;
    @FXML private Label messageLabel;

    @FXML private TableView<Administrateur> adminsTable;
    @FXML private TableColumn<Administrateur, Integer> idColumn;
    @FXML private TableColumn<Administrateur, String> nomColumn;
    @FXML private TableColumn<Administrateur, String> emailColumn;

    private final ObservableList<Administrateur> administrateurs =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("idAdmin")
        );

        nomColumn.setCellValueFactory(
                new PropertyValueFactory<>("nom")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );

        adminsTable.setItems(administrateurs);
        refreshAdministrateurs();
    }

    @FXML
    private void refreshAdministrateurs() {
        administrateurs.setAll(AdministrateurDAO.getAll());
        updateCount();
        showMessage(
                administrateurs.size() + " administrateur(s) chargé(s).",
                false
        );
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();

        if (keyword == null || keyword.isBlank()) {
            administrateurs.setAll(AdministrateurDAO.getAll());
        } else {
            administrateurs.setAll(
                    AdministrateurDAO.search(keyword)
            );
        }

        updateCount();
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        refreshAdministrateurs();
    }

    @FXML
    private void openAddForm() {
        Dialog<AdminFormData> dialog =
                createAdminDialog("Ajouter un administrateur", null);

        Optional<AdminFormData> result = dialog.showAndWait();

        result.ifPresent(data -> {
            if (AdministrateurDAO.emailExists(data.email(), null)) {
                showMessage("Cet email est déjà utilisé.", true);
                return;
            }

            boolean success = AdministrateurDAO.create(
                    data.nom(),
                    data.email(),
                    data.password()
            );

            if (success) {
                refreshAdministrateurs();
                showMessage(
                        "Administrateur ajouté avec succès.",
                        false
                );
            } else {
                showMessage(
                        "Erreur lors de la création.",
                        true
                );
            }
        });
    }

    @FXML
    private void handleEdit() {
        Administrateur selected =
                adminsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Sélectionnez un administrateur.",
                    true
            );
            return;
        }

        Dialog<AdminFormData> dialog =
                createAdminDialog(
                        "Modifier l’administrateur",
                        selected
                );

        Optional<AdminFormData> result = dialog.showAndWait();

        result.ifPresent(data -> {
            if (AdministrateurDAO.emailExists(
                    data.email(),
                    selected.getIdAdmin()
            )) {
                showMessage("Cet email est déjà utilisé.", true);
                return;
            }

            boolean success = AdministrateurDAO.update(
                    selected.getIdAdmin(),
                    data.nom(),
                    data.email()
            );

            if (success) {
                refreshAdministrateurs();
                showMessage(
                        "Administrateur modifié avec succès.",
                        false
                );
            } else {
                showMessage(
                        "Erreur lors de la modification.",
                        true
                );
            }
        });
    }

    @FXML
    private void handlePasswordChange() {
        Administrateur selected =
                adminsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Sélectionnez un administrateur.",
                    true
            );
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Changer le mot de passe");
        dialog.setHeaderText(
                "Nouveau mot de passe pour " + selected.getEmail()
        );

        ButtonType saveType = new ButtonType(
                "Enregistrer",
                ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(saveType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        PasswordField confirmationField = new PasswordField();

        passwordField.setPromptText("Nouveau mot de passe");
        confirmationField.setPromptText("Confirmation");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(new Label("Mot de passe :"), 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(new Label("Confirmation :"), 0, 1);
        grid.add(confirmationField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Button saveButton =
                (Button) dialog.getDialogPane()
                        .lookupButton(saveType);

        saveButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    String password = passwordField.getText();

                    if (!isStrongPassword(password)) {
                        showMessage(
                                "Le mot de passe doit contenir au moins "
                                        + "8 caractères, une majuscule, "
                                        + "une minuscule et un chiffre.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    if (!password.equals(
                            confirmationField.getText()
                    )) {
                        showMessage(
                                "Les mots de passe ne correspondent pas.",
                                true
                        );
                        event.consume();
                    }
                }
        );

        dialog.setResultConverter(buttonType ->
                buttonType == saveType
                        ? passwordField.getText()
                        : null
        );

        dialog.showAndWait().ifPresent(password -> {
            boolean success =
                    AdministrateurDAO.updatePassword(
                            selected.getIdAdmin(),
                            password
                    );

            showMessage(
                    success
                            ? "Mot de passe modifié avec succès."
                            : "Erreur lors du changement de mot de passe.",
                    !success
            );
        });
    }

    @FXML
    private void handleDelete() {
        Administrateur selected =
                adminsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Sélectionnez un administrateur.",
                    true
            );
            return;
        }

        String connectedEmail = Session.getEmail();

        if (connectedEmail != null
                && connectedEmail.equalsIgnoreCase(
                selected.getEmail()
        )) {
            showMessage(
                    "Vous ne pouvez pas supprimer votre propre compte.",
                    true
            );
            return;
        }

        if (AdministrateurDAO.countAll() <= 1) {
            showMessage(
                    "Le dernier administrateur ne peut pas être supprimé.",
                    true
            );
            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Suppression");
        confirmation.setHeaderText(
                "Supprimer " + selected.getEmail() + " ?"
        );
        confirmation.setContentText(
                "Cette action est définitive."
        );

        Optional<ButtonType> response =
                confirmation.showAndWait();

        if (response.isPresent()
                && response.get() == ButtonType.OK) {

            boolean success =
                    AdministrateurDAO.delete(
                            selected.getIdAdmin()
                    );

            if (success) {
                refreshAdministrateurs();
                showMessage(
                        "Administrateur supprimé.",
                        false
                );
            } else {
                showMessage(
                        "Suppression impossible.",
                        true
                );
            }
        }
    }

    private Dialog<AdminFormData> createAdminDialog(
            String title,
            Administrateur existing
    ) {
        Dialog<AdminFormData> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType saveType = new ButtonType(
                "Enregistrer",
                ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(saveType, ButtonType.CANCEL);

        TextField nomField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmationField = new PasswordField();

        nomField.setPromptText("Nom");
        emailField.setPromptText("Email");
        passwordField.setPromptText("Mot de passe");
        confirmationField.setPromptText("Confirmation");

        if (existing != null) {
            nomField.setText(existing.getNom());
            emailField.setText(existing.getEmail());
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);

        grid.add(new Label("Email :"), 0, 1);
        grid.add(emailField, 1, 1);

        if (existing == null) {
            grid.add(new Label("Mot de passe :"), 0, 2);
            grid.add(passwordField, 1, 2);

            grid.add(new Label("Confirmation :"), 0, 3);
            grid.add(confirmationField, 1, 3);
        }

        dialog.getDialogPane().setContent(grid);

        Button saveButton =
                (Button) dialog.getDialogPane()
                        .lookupButton(saveType);

        saveButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    String nom = nomField.getText().trim();
                    String email = emailField.getText().trim();

                    if (nom.isEmpty() || email.isEmpty()) {
                        showMessage(
                                "Le nom et l’email sont obligatoires.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    if (!isValidEmail(email)) {
                        showMessage(
                                "Le format de l’email est invalide.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    if (existing == null) {
                        String password = passwordField.getText();

                        if (!isStrongPassword(password)) {
                            showMessage(
                                    "Mot de passe insuffisamment sécurisé.",
                                    true
                            );
                            event.consume();
                            return;
                        }

                        if (!password.equals(
                                confirmationField.getText()
                        )) {
                            showMessage(
                                    "Les mots de passe ne correspondent pas.",
                                    true
                            );
                            event.consume();
                        }
                    }
                }
        );

        dialog.setResultConverter(buttonType -> {
            if (buttonType != saveType) {
                return null;
            }

            return new AdminFormData(
                    nomField.getText().trim(),
                    emailField.getText().trim().toLowerCase(),
                    passwordField.getText()
            );
        });

        return dialog;
    }

    private boolean isValidEmail(String email) {
        return email != null
                && email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }

    private boolean isStrongPassword(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*");
    }

    private void updateCount() {
        lblCount.setText(
                administrateurs.size()
                        + " administrateur(s)"
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

    @FXML
    private void goBack(ActionEvent event) {
        SceneManager.goBack(event);
    }

    private record AdminFormData(
            String nom,
            String email,
            String password
    ) {
    }
}