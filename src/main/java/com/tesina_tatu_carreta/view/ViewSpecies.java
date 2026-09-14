package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.model.Species;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ViewSpecies {

    private final SpeciesDAO speciesDAO =
            new SpeciesDAO();

    private final ObservableList<Species> speciesList =
            FXCollections.observableArrayList();

    private final TableView<Species> table =
            new TableView<>();

    private TextField speciesNameField;

    private VBox sectionContainer;

    private Button informationButton;
    private Button registeredButton;

    private VBox informationSection;
    private VBox registeredSection;

    // =========================================================
    // VIEW
    // =========================================================

    public Parent getView() {
        return createView();
    }

    public Parent createView() {

        VBox root =
                new VBox(20);

        root.setPadding(
                new Insets(25)
        );

        root.setStyle(
                "-fx-background-color: #F4F1E8;"
        );

        Label title =
                new Label(
                        "Gestión de especies"
                );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #254D3D;"
        );

        Label subtitle =
                new Label(
                        "Gestiona las especies registradas en el sistema."
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #405047;"
        );

        VBox header =
                new VBox(
                        5,
                        title,
                        subtitle
                );

        informationSection =
                createInformationSection();

        registeredSection =
                createRegisteredSection();

        informationButton =
                createSectionButton(
                        "Información de especies"
                );

        registeredButton =
                createSectionButton(
                        "Especies registradas"
                );

        HBox navigation =
                new HBox(12);

        navigation.setAlignment(
                Pos.CENTER
        );

        navigation.getChildren().addAll(
                informationButton,
                registeredButton
        );

        sectionContainer =
                new VBox();

        sectionContainer.setFillWidth(
                true
        );

        VBox.setVgrow(
                sectionContainer,
                Priority.ALWAYS
        );

        informationButton.setOnAction(
                event ->
                        showSection(
                                informationSection,
                                informationButton
                        )
        );

        registeredButton.setOnAction(
                event ->
                        showSection(
                                registeredSection,
                                registeredButton
                        )
        );

        loadSpecies();

        root.getChildren().addAll(
                header,
                navigation,
                sectionContainer
        );

        showSection(
                informationSection,
                informationButton
        );

        ScrollPane scrollPane =
                new ScrollPane(root);

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setFitToHeight(
                true
        );

        scrollPane.setStyle(
                "-fx-background: #F4F1E8;"
        );

        return scrollPane;
    }

    // =========================================================
    // INFORMATION SECTION
    // =========================================================

    private VBox createInformationSection() {

        VBox section =
                new VBox(20);

        VBox card =
                createCard();

        Label title =
                createSectionTitle(
                        "Información de la especie"
                );

        GridPane form =
                new GridPane();

        form.setHgap(20);
        form.setVgap(15);

        speciesNameField =
                new TextField();

        speciesNameField.setPromptText(
                "Ingrese el nombre de la especie"
        );

        GridPane.setHgrow(
                speciesNameField,
                Priority.ALWAYS
        );

        form.add(
                createFieldLabel(
                        "Nombre de la especie"
                ),
                0,
                0
        );

        form.add(
                speciesNameField,
                1,
                0
        );

        Button clearButton =
                new Button(
                        "LIMPIAR"
                );

        Button deleteButton =
                new Button(
                        "ELIMINAR"
                );

        Button editButton =
                new Button(
                        "EDITAR"
                );

        Button addButton =
                new Button(
                        "AGREGAR"
                );

        applySecondaryStyle(
                clearButton
        );

        applyDeleteStyle(
                deleteButton
        );

        applySecondaryStyle(
                editButton
        );

        applyPrimaryStyle(
                addButton
        );

        clearButton.setOnAction(
                event ->
                        clearFields()
        );

        deleteButton.setOnAction(
                event ->
                        deleteSpecies()
        );

        editButton.setOnAction(
                event ->
                        prepareEditSpecies()
        );

        addButton.setOnAction(
                event ->
                        addSpecies()
        );

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        actions.getChildren().addAll(
                clearButton,
                deleteButton,
                editButton,
                addButton
        );

        card.getChildren().addAll(
                title,
                form,
                actions
        );

        section.getChildren().add(
                card
        );

        return section;
    }

    // =========================================================
    // REGISTERED SECTION
    // =========================================================

    private VBox createRegisteredSection() {

        VBox section =
                new VBox(20);

        VBox card =
                createCard();

        Label title =
                createSectionTitle(
                        "Especies registradas"
                );

        createTable();

        Button refreshButton =
                new Button(
                        "ACTUALIZAR"
                );

        applySecondaryStyle(
                refreshButton
        );

        refreshButton.setOnAction(
                event ->
                        loadSpecies()
        );

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        actions.getChildren().add(
                refreshButton
        );

        card.getChildren().addAll(
                title,
                table,
                actions
        );

        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );

        section.getChildren().add(
                card
        );

        return section;
    }

    // =========================================================
    // TABLE
    // =========================================================

    private void createTable() {

        table.getColumns().clear();

        TableColumn<Species, Number>
                idColumn =
                new TableColumn<>(
                        "ID"
                );

        idColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getSpeciesId()
                        )
        );

        TableColumn<Species, String>
                nameColumn =
                new TableColumn<>(
                        "Nombre de la especie"
                );

        nameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getName()
                        )
        );

        idColumn.setPrefWidth(
                100
        );

        nameColumn.setPrefWidth(
                400
        );

        table.getColumns().addAll(
                idColumn,
                nameColumn
        );

        table.setItems(
                speciesList
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(
                420
        );

        table.setMinHeight(
                420
        );

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         oldValue,
                         newValue) -> {

                            if (newValue != null
                                    && speciesNameField != null) {

                                speciesNameField.setText(
                                        newValue.getName()
                                );
                            }
                        }
                );
    }

    // =========================================================
    // ADD
    // =========================================================

    private void addSpecies() {

        String name =
                speciesNameField
                        .getText()
                        .trim();

        if (name.isEmpty()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "El nombre de la especie es obligatorio."
            );

            return;
        }

        Species species =
                new Species();

        species.setName(
                name
        );

        speciesDAO.add(
                species
        );

        loadSpecies();

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Éxito",
                "Especie agregada correctamente."
        );
    }

    // =========================================================
    // EDIT
    // =========================================================

    private void prepareEditSpecies() {

        Species selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione primero una especie."
            );

            return;
        }

        speciesNameField.setText(
                selected.getName()
        );

        speciesNameField.requestFocus();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Editar especie",
                "Modifique el nombre y luego confirme con el botón EDITAR."
        );
    }

    private void updateSpecies() {

        Species selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione primero una especie."
            );

            return;
        }

        String name =
                speciesNameField
                        .getText()
                        .trim();

        if (name.isEmpty()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "El nombre de la especie es obligatorio."
            );

            return;
        }

        selected.setName(
                name
        );

        speciesDAO.update(
                selected
        );

        loadSpecies();

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Éxito",
                "Especie actualizada correctamente."
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteSpecies() {

        Species selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione primero una especie."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Eliminar especie"
        );

        confirmation.setHeaderText(
                null
        );

        confirmation.setContentText(
                "¿Está seguro de que desea eliminar la especie \""
                        + selected.getName()
                        + "\"?"
        );

        confirmation.showAndWait()
                .ifPresent(
                        response -> {

                            if (response ==
                                    ButtonType.OK) {

                                speciesDAO.delete(
                                        selected
                                                .getSpeciesId()
                                );

                                loadSpecies();

                                clearFields();

                                showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Éxito",
                                        "Especie eliminada correctamente."
                                );
                            }
                        }
                );
    }

    // =========================================================
    // DATA
    // =========================================================

    private void loadSpecies() {

        speciesList.setAll(
                speciesDAO.list()
        );
    }

    private void clearFields() {

        if (speciesNameField != null) {

            speciesNameField.clear();
        }

        table.getSelectionModel()
                .clearSelection();
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    private void showSection(
            VBox section,
            Button activeButton) {

        sectionContainer
                .getChildren()
                .setAll(
                        section
                );

        informationButton.setStyle(
                normalSectionButtonStyle()
        );

        registeredButton.setStyle(
                normalSectionButtonStyle()
        );

        activeButton.setStyle(
                selectedSectionButtonStyle()
        );
    }

    private Button createSectionButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefHeight(
                40
        );

        button.setPadding(
                new Insets(
                        0,
                        22,
                        0,
                        22
                )
        );

        button.setStyle(
                normalSectionButtonStyle()
        );

        return button;
    }

    private String normalSectionButtonStyle() {

        return """
                -fx-background-color: #E2E7E2;
                -fx-text-fill: #254D3D;
                -fx-font-weight: bold;
                -fx-background-radius: 9;
                """;
    }

    private String selectedSectionButtonStyle() {

        return """
                -fx-background-color: #254D3D;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 9;
                """;
    }

    // =========================================================
    // UI HELPERS
    // =========================================================

    private VBox createCard() {

        VBox card =
                new VBox(18);

        card.setPadding(
                new Insets(25)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #C9D2CB;" +
                "-fx-border-radius: 14;"
        );

        return card;
    }

    private Label createSectionTitle(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #254D3D;"
        );

        return label;
    }

    private Label createFieldLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #405047;"
        );

        return label;
    }

    private void applyPrimaryStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: #254D3D;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 20;"
        );
    }

    private void applySecondaryStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #405047;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #C9D2CB;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 20;"
        );
    }

    private void applyDeleteStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: #A34A4A;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 20;"
        );
    }

    // =========================================================
    // ALERT
    // =========================================================

    private void showMessage(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}