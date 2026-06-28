package controllers;

import app.SceneManager;
import dao.TarifDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.Tarif;

import java.time.LocalDate;
import java.util.Optional;

public class TarifsController {

    @FXML private TextField searchField;
    @FXML private Label lblCount;
    @FXML private Label lblActiveCount;
    @FXML private Label messageLabel;

    @FXML private TableView<Tarif> tarifsTable;

    @FXML private TableColumn<Tarif, Integer> idColumn;
    @FXML private TableColumn<Tarif, String> libelleColumn;
    @FXML private TableColumn<Tarif, String> typeColumn;
    @FXML private TableColumn<Tarif, String> valeurColumn;
    @FXML private TableColumn<Tarif, String> statutColumn;
    @FXML private TableColumn<Tarif, String> dateDebutColumn;
    @FXML private TableColumn<Tarif, String> dateFinColumn;
    @FXML private TableColumn<Tarif, String> descriptionColumn;

    private final ObservableList<Tarif> tarifs =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("idTarif")
        );

        libelleColumn.setCellValueFactory(
                new PropertyValueFactory<>("libelle")
        );

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("typeReduction")
        );

        valeurColumn.setCellValueFactory(
                new PropertyValueFactory<>("valeurFormatee")
        );

        statutColumn.setCellValueFactory(
                new PropertyValueFactory<>("statutFormate")
        );

        dateDebutColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateDebutFormatee")
        );

        dateFinColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateFinFormatee")
        );

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );

        tarifsTable.setItems(tarifs);

        refreshTarifs();
    }

    @FXML
    private void refreshTarifs() {

        tarifs.setAll(TarifDAO.getAll());

        updateCounters();

        showMessage(
                tarifs.size() + " tarif(s) chargé(s).",
                false
        );
    }

    @FXML
    private void handleSearch() {

        String keyword = searchField.getText();

        if (keyword == null || keyword.isBlank()) {
            tarifs.setAll(TarifDAO.getAll());
        } else {
            tarifs.setAll(TarifDAO.search(keyword));
        }

        updateCounters();
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        refreshTarifs();
    }

    @FXML
    private void openAddForm() {

        Dialog<Tarif> dialog =
                createTarifDialog(
                        "Ajouter un tarif",
                        null
                );

        Optional<Tarif> result = dialog.showAndWait();

        result.ifPresent(tarif -> {

            boolean success = TarifDAO.create(tarif);

            if (success) {
                refreshTarifs();
                showMessage(
                        "Tarif ajouté avec succès.",
                        false
                );
            } else {
                showMessage(
                        "Erreur pendant l'ajout du tarif.",
                        true
                );
            }
        });
    }

    @FXML
    private void handleEdit() {

        Tarif selected =
                tarifsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Sélectionnez un tarif à modifier.",
                    true
            );
            return;
        }

        Dialog<Tarif> dialog =
                createTarifDialog(
                        "Modifier le tarif",
                        selected
                );

        Optional<Tarif> result = dialog.showAndWait();

        result.ifPresent(updated -> {

            updated.setIdTarif(selected.getIdTarif());

            boolean success = TarifDAO.update(updated);

            if (success) {
                refreshTarifs();
                showMessage(
                        "Tarif modifié avec succès.",
                        false
                );
            } else {
                showMessage(
                        "Erreur pendant la modification.",
                        true
                );
            }
        });
    }

    @FXML
    private void toggleActive() {

        Tarif selected =
                tarifsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Sélectionnez un tarif.",
                    true
            );
            return;
        }

        boolean newStatus = !selected.isActif();

        boolean success = TarifDAO.toggleActive(
                selected.getIdTarif(),
                newStatus
        );

        if (success) {
            refreshTarifs();

            showMessage(
                    newStatus
                            ? "Tarif activé."
                            : "Tarif désactivé.",
                    false
            );
        } else {
            showMessage(
                    "Impossible de modifier le statut.",
                    true
            );
        }
    }

    @FXML
    private void handleDelete() {

        Tarif selected =
                tarifsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(
                    "Sélectionnez un tarif à supprimer.",
                    true
            );
            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Suppression");
        confirmation.setHeaderText(
                "Supprimer le tarif « "
                        + selected.getLibelle()
                        + " » ?"
        );

        confirmation.setContentText(
                "Cette action est définitive."
        );

        Optional<ButtonType> response =
                confirmation.showAndWait();

        if (response.isPresent()
                && response.get() == ButtonType.OK) {

            boolean success =
                    TarifDAO.delete(selected.getIdTarif());

            if (success) {
                refreshTarifs();
                showMessage(
                        "Tarif supprimé.",
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

    private Dialog<Tarif> createTarifDialog(
            String title,
            Tarif existing
    ) {

        Dialog<Tarif> dialog = new Dialog<>();
        dialog.setTitle(title);

        ButtonType saveType = new ButtonType(
                "Enregistrer",
                ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(saveType, ButtonType.CANCEL);

        TextField libelleField = new TextField();

        ComboBox<String> typeCombo =
                new ComboBox<>();

        typeCombo.getItems().addAll(
                "pourcentage",
                "montant"
        );

        TextField valeurField = new TextField();

        CheckBox actifCheck = new CheckBox("Tarif actif");

        DatePicker dateDebutPicker = new DatePicker();
        DatePicker dateFinPicker = new DatePicker();

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);

        libelleField.setPromptText("Libellé du tarif");
        valeurField.setPromptText("Valeur");
        typeCombo.setValue("pourcentage");
        actifCheck.setSelected(true);

        if (existing != null) {
            libelleField.setText(existing.getLibelle());
            typeCombo.setValue(existing.getTypeReduction());
            valeurField.setText(
                    String.valueOf(existing.getValeur())
            );
            actifCheck.setSelected(existing.isActif());
            dateDebutPicker.setValue(existing.getDateDebut());
            dateFinPicker.setValue(existing.getDateFin());
            descriptionArea.setText(existing.getDescription());
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(new Label("Libellé :"), 0, 0);
        grid.add(libelleField, 1, 0);

        grid.add(new Label("Type :"), 0, 1);
        grid.add(typeCombo, 1, 1);

        grid.add(new Label("Valeur :"), 0, 2);
        grid.add(valeurField, 1, 2);

        grid.add(new Label("Statut :"), 0, 3);
        grid.add(actifCheck, 1, 3);

        grid.add(new Label("Date de début :"), 0, 4);
        grid.add(dateDebutPicker, 1, 4);

        grid.add(new Label("Date de fin :"), 0, 5);
        grid.add(dateFinPicker, 1, 5);

        grid.add(new Label("Description :"), 0, 6);
        grid.add(descriptionArea, 1, 6);

        dialog.getDialogPane().setContent(grid);

        Button saveButton =
                (Button) dialog.getDialogPane()
                        .lookupButton(saveType);

        saveButton.addEventFilter(
                ActionEvent.ACTION,
                event -> {

                    String libelle =
                            libelleField.getText().trim();

                    if (libelle.isEmpty()) {
                        showMessage(
                                "Le libellé est obligatoire.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    double valeur;

                    try {
                        valeur = Double.parseDouble(
                                valeurField.getText().trim()
                        );
                    } catch (NumberFormatException e) {
                        showMessage(
                                "La valeur doit être un nombre.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    if (valeur < 0) {
                        showMessage(
                                "La valeur ne peut pas être négative.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    if ("pourcentage".equals(typeCombo.getValue())
                            && valeur > 100) {
                        showMessage(
                                "Un pourcentage ne peut pas dépasser 100.",
                                true
                        );
                        event.consume();
                        return;
                    }

                    LocalDate debut =
                            dateDebutPicker.getValue();

                    LocalDate fin =
                            dateFinPicker.getValue();

                    if (debut != null
                            && fin != null
                            && fin.isBefore(debut)) {

                        showMessage(
                                "La date de fin doit être postérieure "
                                        + "à la date de début.",
                                true
                        );
                        event.consume();
                    }
                }
        );

        dialog.setResultConverter(buttonType -> {

            if (buttonType != saveType) {
                return null;
            }

            return new Tarif(
                    libelleField.getText().trim(),
                    typeCombo.getValue(),
                    Double.parseDouble(
                            valeurField.getText().trim()
                    ),
                    actifCheck.isSelected(),
                    dateDebutPicker.getValue(),
                    dateFinPicker.getValue(),
                    descriptionArea.getText().trim()
            );
        });

        return dialog;
    }

    private void updateCounters() {

        lblCount.setText(
                tarifs.size() + " tarif(s)"
        );

        long activeCount = tarifs.stream()
                .filter(Tarif::isActif)
                .count();

        lblActiveCount.setText(
                activeCount + " tarif(s) actif(s)"
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
}