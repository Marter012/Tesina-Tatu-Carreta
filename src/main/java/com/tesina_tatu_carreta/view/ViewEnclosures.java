package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalHoldingDAO;
import com.tesina_tatu_carreta.dao.EnclosureDAO;
import com.tesina_tatu_carreta.model.AnimalInventorySummary;
import com.tesina_tatu_carreta.model.Enclosure;
import com.tesina_tatu_carreta.service.AnimalInventoryService;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewEnclosures {

    private final EnclosureDAO enclosureDAO =
            new EnclosureDAO();

    private final AnimalHoldingDAO animalHoldingDAO =
            new AnimalHoldingDAO();

    private final ObservableList<Enclosure> enclosures =
            FXCollections.observableArrayList();

    private final ObservableList<AnimalInventorySummary>
            inventorySummaries =
            FXCollections.observableArrayList();

    private TableView<Enclosure> enclosureTable;

    private TableView<AnimalInventorySummary> inventoryTable;

    private TextField searchField;

    private VBox contentContainer;

    private Button allButton;
    private Button enclosuresButton;
    private Button quarantineButton;

    // =========================================================
    // VIEW
    // =========================================================

    public Parent getView() {
        return createView();
    }

    public Parent createView() {

        VBox root = new VBox(20);

        root.setPadding(
                new Insets(25)
        );

        root.setStyle(
                "-fx-background-color: #F4F1E8;"
        );

        Label title =
                new Label(
                        "Recintos"
                );

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #254D3D;"
        );

        Label subtitle =
                new Label(
                        "Administre los recintos y consulte la ubicación actual de los animales."
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

        HBox navigation =
                createNavigation();

        contentContainer =
                new VBox();

        contentContainer.setFillWidth(
                true
        );

        VBox.setVgrow(
                contentContainer,
                Priority.ALWAYS
        );

        root.getChildren().addAll(
                header,
                navigation,
                contentContainer
        );

        loadAllData();

        showAll();

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
    // NAVIGATION
    // =========================================================

    private HBox createNavigation() {

        allButton =
                createNavigationButton(
                        "Todos"
                );

        enclosuresButton =
                createNavigationButton(
                        "Recintos"
                );

        quarantineButton =
                createNavigationButton(
                        "Cuarentena"
                );

        allButton.setOnAction(
                event -> showAll()
        );

        enclosuresButton.setOnAction(
                event -> showEnclosures()
        );

        quarantineButton.setOnAction(
                event -> showQuarantine()
        );

        HBox navigation =
                new HBox(10);

        navigation.setAlignment(
                Pos.CENTER_LEFT
        );

        navigation.getChildren().addAll(
                allButton,
                enclosuresButton,
                quarantineButton
        );

        return navigation;
    }

    private Button createNavigationButton(
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
                normalNavigationStyle()
        );

        return button;
    }

    private void setActiveNavigation(
            Button activeButton) {

        allButton.setStyle(
                normalNavigationStyle()
        );

        enclosuresButton.setStyle(
                normalNavigationStyle()
        );

        quarantineButton.setStyle(
                normalNavigationStyle()
        );

        activeButton.setStyle(
                selectedNavigationStyle()
        );
    }

    private String normalNavigationStyle() {

        return """
                -fx-background-color: #E2E7E2;
                -fx-text-fill: #254D3D;
                -fx-font-weight: bold;
                -fx-background-radius: 9;
                """;
    }

    private String selectedNavigationStyle() {

        return """
                -fx-background-color: #254D3D;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 9;
                """;
    }

    // =========================================================
    // ALL
    // =========================================================

    private void showAll() {

        setActiveNavigation(
                allButton
        );

        loadAllData();

        VBox container =
                new VBox(20);

        container.getChildren().add(
                createOverviewCard()
        );

        HBox sections =
                new HBox(20);

        VBox enclosureCard =
                createEnclosureSummaryCard();

        VBox inventoryCard =
                createInventorySummaryCard();

        HBox.setHgrow(
                enclosureCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                inventoryCard,
                Priority.ALWAYS
        );

        sections.getChildren().addAll(
                enclosureCard,
                inventoryCard
        );

        container.getChildren().add(
                sections
        );

        contentContainer
                .getChildren()
                .setAll(
                        container
                );
    }

    private VBox createOverviewCard() {

        VBox card =
                createCard();

        Label title =
                createSectionTitle(
                        "Resumen general"
                );

        HBox statistics =
                new HBox(20);

        statistics.setFillHeight(
                true
        );

        VBox enclosureStat =
                createStatistic(
                        "RECINTOS",
                        String.valueOf(
                                enclosures.size()
                        )
                );

        VBox quarantineStat =
                createStatistic(
                        "EN CUARENTENA",
                        String.valueOf(
                                getTotalQuantity(
                                        AnimalInventoryService.QUARANTINE
                                )
                        )
                );

        VBox permanentStat =
                createStatistic(
                        "UBICACIÓN PERMANENTE",
                        String.valueOf(
                                getTotalQuantity(
                                        AnimalInventoryService.PERMANENT
                                )
                        )
                );

        statistics.getChildren().addAll(
                enclosureStat,
                quarantineStat,
                permanentStat
        );

        card.getChildren().addAll(
                title,
                statistics
        );

        return card;
    }

    private VBox createStatistic(
            String label,
            String value) {

        VBox box =
                new VBox(5);

        box.setPadding(
                new Insets(15)
        );

        box.setStyle(
                "-fx-background-color: #F4F1E8;" +
                "-fx-background-radius: 10;"
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #254D3D;"
        );

        Label textLabel =
                new Label(label);

        textLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #6B756F;"
        );

        box.getChildren().addAll(
                valueLabel,
                textLabel
        );

        HBox.setHgrow(
                box,
                Priority.ALWAYS
        );

        return box;
    }

    private VBox createEnclosureSummaryCard() {

        VBox card =
                createCard();

        Label title =
                createSectionTitle(
                        "Recintos físicos"
                );

        Label description =
                new Label(
                        "Espacios disponibles para alojar animales."
                );

        description.setStyle(
                "-fx-text-fill: #727A76;"
        );

        Button manageButton =
                new Button(
                        "VER RECINTOS"
                );

        applySecondaryStyle(
                manageButton
        );

        manageButton.setOnAction(
                event -> showEnclosures()
        );

        HBox actions =
                new HBox(
                        manageButton
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        card.getChildren().addAll(
                title,
                description,
                actions
        );

        return card;
    }

    private VBox createInventorySummaryCard() {

        VBox card =
                createCard();

        Label title =
                createSectionTitle(
                        "Ubicación de animales"
                );

        Label description =
                new Label(
                        "Consulte cuántos animales se encuentran en cuarentena o en ubicación permanente."
                );

        description.setWrapText(
                true
        );

        description.setStyle(
                "-fx-text-fill: #727A76;"
        );

        Button quarantine =
                new Button(
                        "VER CUARENTENA"
                );

        Button permanent =
                new Button(
                        "VER PERMANENTES"
                );

        applySecondaryStyle(
                quarantine
        );

        applySecondaryStyle(
                permanent
        );

        quarantine.setOnAction(
                event -> showQuarantine()
        );

        permanent.setOnAction(
                event -> showPermanentLocations()
        );

        HBox actions =
                new HBox(
                        10,
                        quarantine,
                        permanent
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        card.getChildren().addAll(
                title,
                description,
                actions
        );

        return card;
    }

    // =========================================================
    // ENCLOSURES
    // =========================================================

    private void showEnclosures() {

        setActiveNavigation(
                enclosuresButton
        );

        loadEnclosures();

        VBox root =
                new VBox(18);

        root.getChildren().add(
                createEnclosuresHeader()
        );

        root.getChildren().add(
                createEnclosuresTableCard()
        );

        contentContainer
                .getChildren()
                .setAll(
                        root
                );
    }

    private HBox createEnclosuresHeader() {

        Label title =
                createSectionTitle(
                        "Recintos físicos"
                );

        Label description =
                new Label(
                        "Administre los espacios disponibles para los animales."
                );

        description.setStyle(
                "-fx-text-fill: #727A76;"
        );

        VBox text =
                new VBox(
                        4,
                        title,
                        description
                );

        Button addButton =
                new Button(
                        "+ NUEVO RECINTO"
                );

        applyPrimaryStyle(
                addButton
        );

        addButton.setOnAction(
                event -> openEnclosureForm()
        );

        HBox header =
                new HBox(
                        text,
                        addButton
                );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                text,
                Priority.ALWAYS
        );

        return header;
    }

    private VBox createEnclosuresTableCard() {

        VBox card =
                createCard();

        HBox toolbar =
                new HBox(10);

        searchField =
                new TextField();

        searchField.setPromptText(
                "Buscar por nombre o sector..."
        );

        searchField.setPrefWidth(
                300
        );

        Button refreshButton =
                new Button(
                        "ACTUALIZAR"
                );

        applySecondaryStyle(
                refreshButton
        );

        refreshButton.setOnAction(
                event -> loadEnclosures()
        );

        toolbar.getChildren().addAll(
                searchField,
                refreshButton
        );

        enclosureTable =
                new TableView<>();

        configureEnclosureTable();

        searchField.textProperty()
                .addListener(
                        (observable,
                         oldValue,
                         newValue) ->
                                filterEnclosures(
                                        newValue
                                )
                );

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        Button editButton =
                new Button(
                        "EDITAR"
                );

        Button deleteButton =
                new Button(
                        "ELIMINAR"
                );

        applySecondaryStyle(
                editButton
        );

        applyDeleteStyle(
                deleteButton
        );

        editButton.setOnAction(
                event -> {

                    Enclosure selected =
                            enclosureTable
                                    .getSelectionModel()
                                    .getSelectedItem();

                    if (selected == null) {

                        showMessage(
                                Alert.AlertType.WARNING,
                                "Sin selección",
                                "Seleccione un recinto primero."
                        );

                        return;
                    }

                    openEnclosureForm(
                            selected
                    );
                }
        );

        deleteButton.setOnAction(
                event -> deleteEnclosure()
        );

        actions.getChildren().addAll(
                editButton,
                deleteButton
        );

        card.getChildren().addAll(
                toolbar,
                enclosureTable,
                actions
        );

        VBox.setVgrow(
                enclosureTable,
                Priority.ALWAYS
        );

        return card;
    }

    private void configureEnclosureTable() {

        enclosureTable.setItems(
                enclosures
        );

        enclosureTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<Enclosure, Number>
                idColumn =
                new TableColumn<>(
                        "ID"
                );

        idColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getEnclosureId()
                        )
        );

        TableColumn<Enclosure, String>
                nameColumn =
                new TableColumn<>(
                        "Nombre"
                );

        nameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getName()
                        )
        );

        TableColumn<Enclosure, String>
                sectorColumn =
                new TableColumn<>(
                        "Sector"
                );

        sectorColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getSector()
                        )
        );

        TableColumn<Enclosure, Number>
                capacityColumn =
                new TableColumn<>(
                        "Capacidad"
                );

        capacityColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getCapacity() == null
                                        ? 0
                                        : data.getValue()
                                                .getCapacity()
                        )
        );

        TableColumn<Enclosure, String>
                statusColumn =
                new TableColumn<>(
                        "Estado"
                );

        statusColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                translateStatus(
                                        data.getValue()
                                                .getStatus()
                                )
                        )
        );

        enclosureTable
                .getColumns()
                .addAll(
                        idColumn,
                        nameColumn,
                        sectorColumn,
                        capacityColumn,
                        statusColumn
                );

        enclosureTable.setPrefHeight(
                430
        );
    }

    private void filterEnclosures(
            String text) {

        String search =
                text == null
                        ? ""
                        : text
                                .trim()
                                .toLowerCase();

        if (search.isEmpty()) {

            enclosureTable.setItems(
                    enclosures
            );

            return;
        }

        ObservableList<Enclosure>
                filtered =
                FXCollections.observableArrayList();

        for (Enclosure enclosure :
                enclosures) {

            String name =
                    enclosure.getName() == null
                            ? ""
                            : enclosure.getName()
                                    .toLowerCase();

            String sector =
                    enclosure.getSector() == null
                            ? ""
                            : enclosure.getSector()
                                    .toLowerCase();

            if (name.contains(search)
                    || sector.contains(search)) {

                filtered.add(
                        enclosure
                );
            }
        }

        enclosureTable.setItems(
                filtered
        );
    }

    // =========================================================
    // QUARANTINE
    // =========================================================

    private void showQuarantine() {

        setActiveNavigation(
                quarantineButton
        );

        loadInventory();

        VBox root =
                new VBox(18);

        Label title =
                createSectionTitle(
                        "Animales en cuarentena"
                );

        Label description =
                new Label(
                        "Consulte los animales que actualmente se encuentran en cuarentena."
                );

        description.setStyle(
                "-fx-text-fill: #727A76;"
        );

        VBox header =
                new VBox(
                        4,
                        title,
                        description
                );

        root.getChildren().add(
                header
        );

        root.getChildren().add(
                createQuarantineCard()
        );

        contentContainer
                .getChildren()
                .setAll(
                        root
                );
    }

    private VBox createQuarantineCard() {

        VBox card =
                createCard();

        inventoryTable =
                new TableView<>();

        inventoryTable.setItems(
                inventorySummaries
        );

        inventoryTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<AnimalInventorySummary, String>
                animalColumn =
                new TableColumn<>(
                        "Animal"
                );

        animalColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getCommonName()
                        )
        );

        TableColumn<AnimalInventorySummary, String>
                scientificColumn =
                new TableColumn<>(
                        "Nombre científico"
                );

        scientificColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getScientificName()
                        )
        );

        TableColumn<AnimalInventorySummary, Number>
                quantityColumn =
                new TableColumn<>(
                        "Cantidad"
                );

        quantityColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getQuarantineQuantity()
                        )
        );

        TableColumn<AnimalInventorySummary, String>
                statusColumn =
                new TableColumn<>(
                        "Estado"
                );

        statusColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                translateStatus(
                                        data.getValue()
                                                .getStatus()
                                )
                        )
        );

        inventoryTable
                .getColumns()
                .addAll(
                        animalColumn,
                        scientificColumn,
                        quantityColumn,
                        statusColumn
                );

        inventoryTable.setPrefHeight(
                430
        );

        card.getChildren().add(
                inventoryTable
        );

        return card;
    }

    // =========================================================
    // ENCLOSURE FORM
    // =========================================================

    private void openEnclosureForm() {

        openEnclosureForm(
                null
        );
    }

    private void openEnclosureForm(
            Enclosure enclosure) {

        boolean editing =
                enclosure != null;

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(25)
        );

        Label title =
                createSectionTitle(
                        editing
                                ? "Editar recinto"
                                : "Nuevo recinto"
                );

        GridPane form =
                new GridPane();

        form.setHgap(15);
        form.setVgap(15);

        TextField name =
                new TextField(
                        editing
                                ? enclosure.getName()
                                : ""
                );

        TextField sector =
                new TextField(
                        editing
                                ? enclosure.getSector()
                                : ""
                );

        TextField capacity =
                new TextField(
                        editing
                                && enclosure.getCapacity() != null
                                ? String.valueOf(
                                        enclosure.getCapacity()
                                )
                                : ""
                );

        ComboBox<String> status =
                new ComboBox<>();

        status.getItems().addAll(
                "Active",
                "Inactive"
        );

        status.setValue(
                editing
                        ? enclosure.getStatus()
                        : "Active"
        );

        TextArea observations =
                new TextArea(
                        editing
                                && enclosure.getObservations() != null
                                ? enclosure.getObservations()
                                : ""
                );

        observations.setWrapText(
                true
        );

        observations.setPrefRowCount(
                4
        );

        form.add(
                createFieldLabel("Nombre"),
                0,
                0
        );

        form.add(
                name,
                1,
                0
        );

        form.add(
                createFieldLabel("Sector"),
                0,
                1
        );

        form.add(
                sector,
                1,
                1
        );

        form.add(
                createFieldLabel("Capacidad"),
                0,
                2
        );

        form.add(
                capacity,
                1,
                2
        );

        form.add(
                createFieldLabel("Estado"),
                0,
                3
        );

        form.add(
                status,
                1,
                3
        );

        form.add(
                createFieldLabel("Observaciones"),
                0,
                4
        );

        form.add(
                observations,
                1,
                4
        );

        GridPane.setHgrow(
                name,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                sector,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                capacity,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                status,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                observations,
                Priority.ALWAYS
        );

        Button cancelButton =
                new Button(
                        "CANCELAR"
                );

        Button saveButton =
                new Button(
                        editing
                                ? "GUARDAR CAMBIOS"
                                : "CREAR RECINTO"
                );

        applySecondaryStyle(
                cancelButton
        );

        applyPrimaryStyle(
                saveButton
        );

        VBox modal =
                createCard();

        modal.getChildren().addAll(
                title,
                form
        );

        HBox actions =
                new HBox(
                        10,
                        cancelButton,
                        saveButton
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        modal.getChildren().add(
                actions
        );

        Stage stage =
                createModalStage(
                        editing
                                ? "Editar recinto"
                                : "Nuevo recinto",
                        modal
                );

        cancelButton.setOnAction(
                event -> stage.close()
        );

        saveButton.setOnAction(
                event -> {

                    try {

                        saveEnclosure(
                                enclosure,
                                name,
                                sector,
                                capacity,
                                status,
                                observations
                        );

                        stage.close();

                        loadEnclosures();

                        if (contentContainer != null) {
                            showEnclosures();
                        }

                    } catch (Exception exception) {

                        showMessage(
                                Alert.AlertType.WARNING,
                                "Error de validación",
                                exception.getMessage()
                        );
                    }
                }
        );

        stage.showAndWait();
    }

    private void saveEnclosure(
            Enclosure enclosure,
            TextField nameField,
            TextField sectorField,
            TextField capacityField,
            ComboBox<String> statusComboBox,
            TextArea observationsField) {

        String name =
                nameField.getText()
                        .trim();

        String sector =
                sectorField.getText()
                        .trim();

        if (name.isEmpty()
                || sector.isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre y el sector son obligatorios."
            );
        }

        Integer capacity =
                null;

        String capacityText =
                capacityField.getText()
                        .trim();

        if (!capacityText.isEmpty()) {

            try {

                capacity =
                        Integer.parseInt(
                                capacityText
                        );

            } catch (NumberFormatException exception) {

                throw new IllegalArgumentException(
                        "La capacidad debe ser un número válido."
                );
            }

            if (capacity < 0) {

                throw new IllegalArgumentException(
                        "La capacidad no puede ser negativa."
                );
            }
        }

        if (statusComboBox.getValue() == null) {

            throw new IllegalArgumentException(
                    "Seleccione un estado."
            );
        }

        if (enclosure == null) {

            Enclosure newEnclosure =
                    new Enclosure();

            newEnclosure.setName(
                    name
            );

            newEnclosure.setSector(
                    sector
            );

            newEnclosure.setCapacity(
                    capacity
            );

            newEnclosure.setStatus(
                    statusComboBox.getValue()
            );

            newEnclosure.setObservations(
                    observationsField
                            .getText()
                            .trim()
            );

            enclosureDAO.add(
                    newEnclosure
            );

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Operación exitosa",
                    "El recinto fue creado correctamente."
            );

        } else {

            enclosure.setName(
                    name
            );

            enclosure.setSector(
                    sector
            );

            enclosure.setCapacity(
                    capacity
            );

            enclosure.setStatus(
                    statusComboBox.getValue()
            );

            enclosure.setObservations(
                    observationsField
                            .getText()
                            .trim()
            );

            enclosureDAO.update(
                    enclosure
            );

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Operación exitosa",
                    "El recinto fue actualizado correctamente."
            );
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteEnclosure() {

        Enclosure selected =
                enclosureTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un recinto primero."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Eliminar recinto"
        );

        confirmation.setHeaderText(
                null
        );

        confirmation.setContentText(
                "¿Está seguro de que desea eliminar el recinto \""
                        + selected.getName()
                        + "\"?"
        );

        confirmation.showAndWait()
                .ifPresent(
                        response -> {

                            if (response ==
                                    ButtonType.OK) {

                                enclosureDAO.delete(
                                        selected
                                                .getEnclosureId()
                                );

                                loadEnclosures();

                                showEnclosures();

                                showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Operación exitosa",
                                        "El recinto fue eliminado correctamente."
                                );
                            }
                        }
                );
    }

    // =========================================================
    // DATA
    // =========================================================

    private void loadAllData() {

        loadEnclosures();
        loadInventory();
    }

    private void loadEnclosures() {

        try {

            enclosures.setAll(
                    enclosureDAO.list()
            );

        } catch (Exception exception) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudieron cargar los recintos."
            );
        }
    }

    private void loadInventory() {

        try {

            inventorySummaries.setAll(
                    animalHoldingDAO
                            .listInventorySummary()
            );

        } catch (Exception exception) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo cargar el inventario de animales."
            );
        }
    }

    private int getTotalQuantity(
            String location) {

        return animalHoldingDAO
                .getTotalQuantity(
                        location
                );
    }

    // =========================================================
    // PERMANENT LOCATIONS
    // =========================================================

    private void showPermanentLocations() {

        ViewPermanentEnclosure view =
                new ViewPermanentEnclosure();

        Stage stage =
                createModalStage(
                        "Ubicaciones permanentes",
                        view.getView()
                );

        stage.setMinWidth(
                900
        );

        stage.setMinHeight(
                650
        );

        stage.showAndWait();
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private VBox createCard() {

        VBox card =
                new VBox(15);

        card.setPadding(
                new Insets(22)
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

    private Stage createModalStage(
            String title,
            Parent content) {

        Stage stage =
                new Stage();

        stage.setTitle(
                title
        );

        stage.initModality(
                Modality.APPLICATION_MODAL
        );

        stage.setMinWidth(
                650
        );

        stage.setMinHeight(
                500
        );

        stage.setScene(
                new Scene(
                        new ScrollPane(content),
                        700,
                        600
                )
        );

        return stage;
    }

    private String translateStatus(
            String value) {

        if (value == null) {
            return "";
        }

        return switch (value) {

            case "Active" ->
                    "Activo";

            case "Inactive" ->
                    "Inactivo";

            default ->
                    value;
        };
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