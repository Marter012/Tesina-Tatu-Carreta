package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.AnimalHoldingDAO;
import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.AnimalHolding;
import com.tesina_tatu_carreta.model.AnimalInventorySummary;
import com.tesina_tatu_carreta.model.Species;
import com.tesina_tatu_carreta.service.AnimalInventoryService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.List;

public class ViewAnimals {

    private final AnimalDAO animalDAO =
            new AnimalDAO();

    private final SpeciesDAO speciesDAO =
            new SpeciesDAO();

    private final AnimalHoldingDAO holdingDAO =
            new AnimalHoldingDAO();

    private final AnimalInventoryService inventoryService =
            new AnimalInventoryService();

    private final TableView<AnimalInventorySummary>
            table =
            new TableView<>();

    private final ObservableList<
            AnimalInventorySummary>
            inventory =
            FXCollections.observableArrayList();

    private final ObservableList<Animal>
            animals =
            FXCollections.observableArrayList();

    private final ObservableList<Species>
            species =
            FXCollections.observableArrayList();

    private VBox contentContainer;

    private Button informationButton;
    private Button registeredButton;

    private ComboBox<Species> speciesComboBox;
    private TextField commonNameField;
    private TextField scientificNameField;
    private ComboBox<String> originComboBox;
    private ComboBox<String> statusComboBox;

    private Animal selectedAnimal;

    public Parent getView() {
        return createView();
    }

    public Parent createView() {

        Label breadcrumb =
                new Label(
                        "Inicio / Gestión de animales"
                );

        breadcrumb.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #8A918E;"
        );

        Label title =
                new Label(
                        "Gestión de animales"
                );

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );

        Label description =
                new Label(
                        "Consulte y administre los animales que se encuentran actualmente en la reserva."
                );

        description.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #727A76;"
        );

        VBox header =
                new VBox(
                        7,
                        breadcrumb,
                        title,
                        description
                );

        informationButton =
                createSectionButton(
                        "Información del animal",
                        true
                );

        registeredButton =
                createSectionButton(
                        "Inventario actual",
                        false
                );

        HBox navigation =
                new HBox(
                        10,
                        informationButton,
                        registeredButton
                );

        navigation.setAlignment(
                Pos.CENTER
        );

        navigation.setPadding(
                new Insets(8)
        );

        informationButton.setOnAction(
                event -> {
                    selectSection(
                            informationButton
                    );
                    showInformationSection();
                }
        );

        registeredButton.setOnAction(
                event -> {
                    selectSection(
                            registeredButton
                    );
                    showInventorySection();
                }
        );

        contentContainer =
                new VBox();

        contentContainer.setFillWidth(
                true
        );

        contentContainer.getChildren().add(
                createInformationSection()
        );

        VBox content =
                new VBox(22);

        content.setPadding(
                new Insets(
                        35,
                        40,
                        35,
                        40
                )
        );

        content.setStyle(
                "-fx-background-color: #F4F2EA;"
        );

        content.getChildren().addAll(
                header,
                navigation,
                contentContainer
        );

        VBox.setVgrow(
                contentContainer,
                Priority.ALWAYS
        );

        ScrollPane scrollPane =
                new ScrollPane(content);

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setStyle(
                "-fx-background-color: #F4F2EA;"
        );

        return scrollPane;
    }

    private VBox createInformationSection() {

        speciesComboBox =
                new ComboBox<>();

        commonNameField =
                new TextField();

        scientificNameField =
                new TextField();

        originComboBox =
                new ComboBox<>();

        statusComboBox =
                new ComboBox<>();

        loadSpecies();

        speciesComboBox.setPromptText(
                "Seleccione una especie"
        );

        commonNameField.setPromptText(
                "Nombre común"
        );

        scientificNameField.setPromptText(
                "Nombre científico"
        );

        originComboBox.getItems().setAll(
                "Admission",
                "Already existing in the reserve"
        );

        statusComboBox.getItems().setAll(
                "Active",
                "Inactive"
        );

        originComboBox.setValue(
                "Admission"
        );

        statusComboBox.setValue(
                "Active"
        );

        configureOriginConverter();
        configureStatusConverter();

        configureControl(
                speciesComboBox
        );

        configureControl(
                commonNameField
        );

        configureControl(
                scientificNameField
        );

        configureControl(
                originComboBox
        );

        configureControl(
                statusComboBox
        );

        GridPane form =
                new GridPane();

        form.setHgap(20);
        form.setVgap(15);

        form.add(
                label("Especie"),
                0,
                0
        );

        form.add(
                speciesComboBox,
                0,
                1
        );

        form.add(
                label("Nombre común"),
                1,
                0
        );

        form.add(
                commonNameField,
                1,
                1
        );

        form.add(
                label("Nombre científico"),
                0,
                2
        );

        form.add(
                scientificNameField,
                0,
                3
        );

        form.add(
                label("Origen"),
                1,
                2
        );

        form.add(
                originComboBox,
                1,
                3
        );

        form.add(
                label("Estado"),
                0,
                4
        );

        form.add(
                statusComboBox,
                0,
                5
        );

        GridPane.setHgrow(
                speciesComboBox,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                commonNameField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                scientificNameField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                originComboBox,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                statusComboBox,
                Priority.ALWAYS
        );

        Button clearButton =
                secondaryButton(
                        "LIMPIAR"
                );

        Button deleteButton =
                deleteButton(
                        "ELIMINAR"
                );

        Button editButton =
                secondaryButton(
                        "EDITAR"
                );

        Button addButton =
                primaryButton(
                        "AGREGAR"
                );

        clearButton.setOnAction(
                event -> clearFields()
        );

        deleteButton.setOnAction(
                event -> deleteAnimal()
        );

        editButton.setOnAction(
                event -> updateAnimal()
        );

        addButton.setOnAction(
                event -> addAnimal()
        );

        HBox actions =
                new HBox(
                        10,
                        clearButton,
                        deleteButton,
                        editButton,
                        addButton
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        Label help =
                new Label(
                        "Seleccione un animal de la tabla de inventario para consultar sus datos."
                );

        help.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #777E7A;"
        );

        VBox card =
                new VBox(
                        20,
                        title(
                                "Información del animal"
                        ),
                        help,
                        form,
                        actions
                );

        card.setPadding(
                new Insets(25)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #DDDAD1;" +
                "-fx-border-radius: 14;"
        );

        return new VBox(card);
    }

    private VBox createInventorySection() {

        Label title =
                title(
                        "Inventario actual"
                );

        Label description =
                new Label(
                        "Aquí puede consultar cuántos animales hay actualmente en cada lugar de la reserva."
                );

        description.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #777E7A;"
        );

        createInventoryColumns();

        loadInventory();

        table.setItems(
                inventory
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(
                400
        );

        table.setFixedCellSize(
                42
        );

        table.setPlaceholder(
                new Label(
                        "No hay animales registrados en el inventario actual."
                )
        );

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                handleInventorySelection(
                                        newValue
                                )
                );

        HBox summary =
                createInventorySummary();

        Button refresh =
                secondaryButton(
                        "ACTUALIZAR"
                );

        Button transfer =
                primaryButton(
                        "TRASLADAR"
                );

        Button death =
                deleteButton(
                        "REGISTRAR MUERTE"
                );

        Button exit =
                secondaryButton(
                        "REGISTRAR SALIDA"
                );

        refresh.setOnAction(
                event -> loadInventory()
        );

        transfer.setOnAction(
                event -> openMovementDialog(
                        "TRANSFER"
                )
        );

        death.setOnAction(
                event -> openMovementDialog(
                        "DEATH"
                )
        );

        exit.setOnAction(
                event -> openMovementDialog(
                        "EXIT"
                )
        );

        HBox actions =
                new HBox(
                        10,
                        refresh,
                        transfer,
                        death,
                        exit
                );

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        Label selectionHelp =
                new Label(
                        "Seleccione un animal para registrar un traslado, una muerte o una salida."
                );

        selectionHelp.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #777E7A;"
        );

        VBox card =
                new VBox(
                        18,
                        title,
                        description,
                        summary,
                        table,
                        selectionHelp,
                        actions
                );

        card.setPadding(
                new Insets(25)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #DDDAD1;" +
                "-fx-border-radius: 14;"
        );

        return card;
    }

    private HBox createInventorySummary() {

        int permanent =
                holdingDAO.getTotalQuantity(
                        AnimalInventoryService.PERMANENT
                );

        int quarantine =
                holdingDAO.getTotalQuantity(
                        AnimalInventoryService.QUARANTINE
                );

        int total =
                permanent + quarantine;

        Label totalTitle =
                new Label(
                        "TOTAL ACTUAL"
                );

        Label totalValue =
                new Label(
                        String.valueOf(total)
                );

        Label quarantineTitle =
                new Label(
                        "CUARENTENA"
                );

        Label quarantineValue =
                new Label(
                        String.valueOf(quarantine)
                );

        Label permanentTitle =
                new Label(
                        "PERMANENTE"
                );

        Label permanentValue =
                new Label(
                        String.valueOf(permanent)
                );

        styleSummaryTitle(
                totalTitle
        );

        styleSummaryValue(
                totalValue
        );

        styleSummaryTitle(
                quarantineTitle
        );

        styleSummaryValue(
                quarantineValue
        );

        styleSummaryTitle(
                permanentTitle
        );

        styleSummaryValue(
                permanentValue
        );

        VBox totalBox =
                createSummaryBox(
                        totalTitle,
                        totalValue
                );

        VBox quarantineBox =
                createSummaryBox(
                        quarantineTitle,
                        quarantineValue
                );

        VBox permanentBox =
                createSummaryBox(
                        permanentTitle,
                        permanentValue
                );

        HBox box =
                new HBox(
                        25,
                        totalBox,
                        quarantineBox,
                        permanentBox
                );

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        box.setPadding(
                new Insets(12)
        );

        box.setStyle(
                "-fx-background-color: #E8EEE9;" +
                "-fx-background-radius: 10;"
        );

        return box;
    }

    private VBox createSummaryBox(
            Label title,
            Label value) {

        VBox box =
                new VBox(
                        3,
                        title,
                        value
                );

        box.setAlignment(
                Pos.CENTER
        );

        box.setMinWidth(
                140
        );

        return box;
    }

    private void styleSummaryTitle(
            Label label) {

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #607066;"
        );
    }

    private void styleSummaryValue(
            Label label) {

        label.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );
    }

    private void createInventoryColumns() {

        table.getColumns().clear();

        TableColumn<
                AnimalInventorySummary,
                String> animalColumn =
                new TableColumn<>(
                        "Animal"
                );

        animalColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "commonName"
                )
        );

        TableColumn<
                AnimalInventorySummary,
                String> scientificColumn =
                new TableColumn<>(
                        "Nombre científico"
                );

        scientificColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "scientificName"
                )
        );

        TableColumn<
                AnimalInventorySummary,
                Integer> permanentColumn =
                new TableColumn<>(
                        "Permanente"
                );

        permanentColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "permanentQuantity"
                )
        );

        TableColumn<
                AnimalInventorySummary,
                Integer> quarantineColumn =
                new TableColumn<>(
                        "Cuarentena"
                );

        quarantineColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "quarantineQuantity"
                )
        );

        TableColumn<
                AnimalInventorySummary,
                Integer> totalColumn =
                new TableColumn<>(
                        "TOTAL"
                );

        totalColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "totalQuantity"
                )
        );

        TableColumn<
                AnimalInventorySummary,
                String> statusColumn =
                new TableColumn<>(
                        "Estado"
                );

        statusColumn.setCellValueFactory(
                cellData -> {

                    String value =
                            cellData
                                    .getValue()
                                    .getStatus();

                    return new SimpleStringProperty(
                            translateStatus(
                                    value
                            )
                    );
                }
        );

        table.getColumns().addAll(
                animalColumn,
                scientificColumn,
                permanentColumn,
                quarantineColumn,
                totalColumn,
                statusColumn
        );
    }

    private void handleInventorySelection(
            AnimalInventorySummary selected) {

        if (selected == null) {
            return;
        }

        Animal animal =
                animalDAO.findById(
                        selected.getAnimalId()
                );

        if (animal == null) {
            return;
        }

        selectedAnimal = animal;

        loadAnimalIntoFields(
                animal
        );
    }

    private void loadAnimalIntoFields(
            Animal animal) {

        Species selectedSpecies = null;

        for (Species item : species) {

            if (item.getSpeciesId()
                    == animal.getSpeciesId()) {

                selectedSpecies = item;
                break;
            }
        }

        speciesComboBox.setValue(
                selectedSpecies
        );

        commonNameField.setText(
                safeValue(
                        animal.getCommonName()
                )
        );

        scientificNameField.setText(
                safeValue(
                        animal.getScientificName()
                )
        );

        originComboBox.setValue(
                safeValue(
                        animal.getOrigin()
                )
        );

        statusComboBox.setValue(
                safeValue(
                        animal.getStatus()
                )
        );
    }

    private void openMovementDialog(
            String movementType) {

        AnimalInventorySummary selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Seleccione un animal",
                    "Primero debe seleccionar el animal sobre el que desea realizar la operación."
            );

            return;
        }

        List<AnimalHolding> holdings =
                holdingDAO.listByAnimal(
                        selected.getAnimalId()
                );

        if (holdings.isEmpty()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin animales disponibles",
                    "Este animal no tiene ejemplares disponibles para realizar esta operación."
            );

            return;
        }

        ComboBox<AnimalHolding> holdingComboBox =
                new ComboBox<>(
                        FXCollections.observableArrayList(
                                holdings
                        )
                );

        holdingComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        holdingComboBox.setPrefHeight(
                42
        );

        holdingComboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            AnimalHolding holding) {

                        if (holding == null) {
                            return "";
                        }

                        return formatHolding(
                                holding
                        );
                    }

                    @Override
                    public AnimalHolding fromString(
                            String value) {

                        return null;
                    }
                }
        );

        TextField quantityField =
                new TextField();

        quantityField.setPromptText(
                "Ingrese la cantidad"
        );

        quantityField.setPrefHeight(
                42
        );

        TextField destinationField =
                new TextField();

        destinationField.setPromptText(
                "Ejemplo: Reserva Provincial..."
        );

        destinationField.setPrefHeight(
                42
        );

        TextArea observationsField =
                new TextArea();

        observationsField.setPromptText(
                "Escriba una observación si es necesario..."
        );

        observationsField.setWrapText(
                true
        );

        observationsField.setPrefRowCount(
                4
        );

        Label availableValue =
                new Label(
                        "Seleccione una ubicación"
                );

        availableValue.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );

        holdingComboBox.setOnAction(
                event -> {

                    AnimalHolding holding =
                            holdingComboBox.getValue();

                    if (holding == null) {

                        availableValue.setText(
                                "Seleccione una ubicación"
                        );

                        return;
                    }

                    availableValue.setText(
                            "Hay "
                                    + holding.getQuantity()
                                    + " animales disponibles"
                    );
                }
        );

        VBox content =
                new VBox(
                        13
                );

        Label animalLabel =
                new Label(
                        "Animal"
                );

        Label animalValue =
                new Label(
                        selected.getCommonName()
                );

        animalValue.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );

        content.getChildren().addAll(
                animalLabel,
                animalValue,
                new Label(
                        "¿De dónde se retiran los animales?"
                ),
                holdingComboBox,
                new Label(
                        "Cantidad disponible"
                ),
                availableValue,
                new Label(
                        "¿Cuántos animales desea registrar?"
                ),
                quantityField
        );

        ComboBox<String> target =
                null;

        if ("TRANSFER".equals(movementType)) {

            target =
                    new ComboBox<>();

            target.getItems().addAll(
                    AnimalInventoryService.QUARANTINE,
                    AnimalInventoryService.PERMANENT
            );

            target.setConverter(
                    new StringConverter<>() {

                        @Override
                        public String toString(
                                String value) {

                            return translateLocation(
                                    value
                            );
                        }

                        @Override
                        public String fromString(
                                String value) {

                            return value;
                        }
                    }
            );

            target.setMaxWidth(
                    Double.MAX_VALUE
            );

            target.setPrefHeight(
                    42
            );

            content.getChildren().addAll(
                    new Label(
                            "¿A dónde se trasladan?"
                    ),
                    target
            );

        } else if ("EXIT".equals(movementType)) {

            content.getChildren().addAll(
                    new Label(
                            "¿A qué lugar salen?"
                    ),
                    destinationField
            );
        }

        content.getChildren().addAll(
                new Label(
                        "Observaciones"
                ),
                observationsField
        );

        Button save =
                primaryButton(
                        movementButtonText(
                                movementType
                        )
                );

        Button cancel =
                secondaryButton(
                        "CANCELAR"
                );

        HBox buttons =
                new HBox(
                        10,
                        cancel,
                        save
                );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        VBox box =
                new VBox(
                        18,
                        content,
                        buttons
                );

        box.setPadding(
                new Insets(10)
        );

        Alert alert =
                new Alert(
                        Alert.AlertType.NONE
                );

        alert.setTitle(
                translateMovementType(
                        movementType
                )
        );

        alert.setHeaderText(
                selected.getCommonName()
        );

        alert.getDialogPane()
                .setContent(box);

        cancel.setOnAction(
                event -> alert.close()
        );

        ComboBox<String> finalTarget =
                target;

        save.setOnAction(
                event -> {

                    try {

                        AnimalHolding holding =
                                holdingComboBox.getValue();

                        if (holding == null) {

                            throw new IllegalArgumentException(
                                    "Debe seleccionar de dónde se retiran los animales."
                            );
                        }

                        int quantity =
                                parseQuantity(
                                        quantityField
                                                .getText()
                                                .trim()
                                );

                        if (quantity
                                > holding.getQuantity()) {

                            throw new IllegalArgumentException(
                                    "La cantidad indicada supera la cantidad disponible."
                            );
                        }

                        if ("TRANSFER".equals(
                                movementType)) {

                            if (finalTarget == null
                                    || finalTarget.getValue()
                                    == null) {

                                throw new IllegalArgumentException(
                                        "Debe seleccionar el lugar de destino."
                                );
                            }

                            String targetLocation =
                                    finalTarget.getValue();

                            if (holding.getLocationType()
                                    .equals(
                                            targetLocation
                                    )) {

                                throw new IllegalArgumentException(
                                        "El lugar de destino debe ser diferente al lugar actual."
                                );
                            }

                            if (!confirmAction(
                                    "Confirmar traslado",
                                    "¿Desea trasladar "
                                            + quantity
                                            + " animal(es) de "
                                            + translateLocation(
                                            holding.getLocationType()
                                    )
                                            + " a "
                                            + translateLocation(
                                            targetLocation
                                    )
                                            + "?"
                            )) {
                                return;
                            }

                            inventoryService.transfer(
                                    selected.getAnimalId(),
                                    holding.getEntryId(),
                                    holding.getLocationType(),
                                    targetLocation,
                                    quantity,
                                    observationsField
                                            .getText()
                                            .trim()
                            );

                        } else if ("DEATH".equals(
                                movementType)) {

                            if (!confirmAction(
                                    "Confirmar muerte",
                                    "¿Confirma que "
                                            + quantity
                                            + " animal(es) han muerto?"
                            )) {
                                return;
                            }

                            inventoryService.registerDeath(
                                    selected.getAnimalId(),
                                    holding.getEntryId(),
                                    holding.getLocationType(),
                                    quantity,
                                    observationsField
                                            .getText()
                                            .trim()
                            );

                        } else {

                            String destination =
                                    destinationField
                                            .getText()
                                            .trim();

                            if (destination.isBlank()) {

                                throw new IllegalArgumentException(
                                        "Debe indicar el lugar al que salen los animales."
                                );
                            }

                            if (!confirmAction(
                                    "Confirmar salida",
                                    "¿Confirma la salida de "
                                            + quantity
                                            + " animal(es) hacia "
                                            + destination
                                            + "?"
                            )) {
                                return;
                            }

                            inventoryService.registerExit(
                                    selected.getAnimalId(),
                                    holding.getEntryId(),
                                    holding.getLocationType(),
                                    quantity,
                                    destination,
                                    observationsField
                                            .getText()
                                            .trim()
                            );
                        }

                        alert.close();

                        loadInventory();

                        showMessage(
                                Alert.AlertType.INFORMATION,
                                "Operación realizada",
                                movementSuccessMessage(
                                        movementType
                                )
                        );

                    } catch (NumberFormatException exception) {

                        showMessage(
                                Alert.AlertType.WARNING,
                                "Cantidad incorrecta",
                                "Ingrese una cantidad válida usando solamente números."
                        );

                    } catch (Exception exception) {

                        showMessage(
                                Alert.AlertType.ERROR,
                                "No se pudo realizar la operación",
                                getExceptionMessage(
                                        exception
                                )
                        );
                    }
                });

        alert.showAndWait();
    }

    private String formatHolding(
            AnimalHolding holding) {

        String location =
                translateLocation(
                        holding.getLocationType()
                );

        String entry =
                holding.getEntryId() == null
                        ? "Sin acta de ingreso"
                        : "Acta de ingreso N.º "
                                + holding.getEntryId();

        return location
                + " — "
                + holding.getQuantity()
                + " animales — "
                + entry;
    }

    private int parseQuantity(
            String value) {

        if (value.isBlank()) {

            throw new IllegalArgumentException(
                    "Debe indicar una cantidad."
            );
        }

        int quantity =
                Integer.parseInt(
                        value
                );

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        return quantity;
    }

    private boolean confirmAction(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        return alert.showAndWait()
                .filter(
                        button ->
                                button ==
                                        javafx.scene.control.ButtonType.OK
                )
                .isPresent();
    }

    private String movementButtonText(
            String movementType) {

        return switch (movementType) {

            case "TRANSFER" ->
                    "TRASLADAR";

            case "DEATH" ->
                    "REGISTRAR MUERTE";

            case "EXIT" ->
                    "REGISTRAR SALIDA";

            default ->
                    "GUARDAR";
        };
    }

    private String movementSuccessMessage(
            String movementType) {

        return switch (movementType) {

            case "TRANSFER" ->
                    "El traslado fue registrado correctamente.";

            case "DEATH" ->
                    "La muerte fue registrada correctamente.";

            case "EXIT" ->
                    "La salida fue registrada correctamente.";

            default ->
                    "La operación fue registrada correctamente.";
        };
    }

    private String getExceptionMessage(
            Exception exception) {

        if (exception.getMessage() == null
                || exception.getMessage().isBlank()) {

            return "Ocurrió un error inesperado.";
        }

        return exception.getMessage();
    }

    private void addAnimal() {

        if (!validateAnimalFields()) {
            return;
        }

        Animal animal =
                new Animal();

        animal.setSpeciesId(
                speciesComboBox
                        .getValue()
                        .getSpeciesId()
        );

        animal.setCommonName(
                commonNameField
                        .getText()
                        .trim()
        );

        animal.setScientificName(
                scientificNameField
                        .getText()
                        .trim()
        );

        animal.setCurrentQuantity(
                0
        );

        animal.setOrigin(
                originComboBox.getValue()
        );

        animal.setStatus(
                statusComboBox.getValue()
        );

        animalDAO.add(
                animal
        );

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Operación exitosa",
                "El animal fue registrado correctamente."
        );
    }

    private void updateAnimal() {

        Animal animal =
                selectedAnimal;

        if (animal == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Debe seleccionar un animal primero."
            );

            return;
        }

        if (!validateAnimalFields()) {
            return;
        }

        animal.setSpeciesId(
                speciesComboBox
                        .getValue()
                        .getSpeciesId()
        );

        animal.setCommonName(
                commonNameField
                        .getText()
                        .trim()
        );

        animal.setScientificName(
                scientificNameField
                        .getText()
                        .trim()
        );

        animal.setOrigin(
                originComboBox.getValue()
        );

        animal.setStatus(
                statusComboBox.getValue()
        );

        animalDAO.update(
                animal
        );

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Operación exitosa",
                "La información del animal fue actualizada correctamente."
        );
    }

    private void deleteAnimal() {

        if (selectedAnimal == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Debe seleccionar un animal primero."
            );

            return;
        }

        if (selectedAnimal.getCurrentQuantity() > 0) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "No se puede eliminar",
                    "No se puede eliminar un animal que todavía tiene ejemplares en la reserva."
            );

            return;
        }

        boolean confirmed =
                confirmAction(
                        "Confirmar eliminación",
                        "¿Está seguro de que desea eliminar este animal?"
                );

        if (!confirmed) {
            return;
        }

        animalDAO.delete(
                selectedAnimal.getAnimalId()
        );

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Operación exitosa",
                "El animal fue eliminado correctamente."
        );
    }

    private boolean validateAnimalFields() {

        if (speciesComboBox.getValue() == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "Debe seleccionar una especie."
            );

            return false;
        }

        if (commonNameField
                .getText()
                .isBlank()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El nombre común es obligatorio."
            );

            return false;
        }

        if (scientificNameField
                .getText()
                .isBlank()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El nombre científico es obligatorio."
            );

            return false;
        }

        if (originComboBox.getValue() == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "Debe seleccionar un origen."
            );

            return false;
        }

        if (statusComboBox.getValue() == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "Debe seleccionar un estado."
            );

            return false;
        }

        return true;
    }

    private void loadInventory() {

        inventory.setAll(
                holdingDAO.listInventorySummary()
        );

        table.setItems(
                inventory
        );
    }

    private void loadSpecies() {

        species.setAll(
                speciesDAO.list()
        );

        speciesComboBox.setItems(
                species
        );
    }

    private void showInformationSection() {

        contentContainer
                .getChildren()
                .setAll(
                        createInformationSection()
                );
    }

    private void showInventorySection() {

        contentContainer
                .getChildren()
                .setAll(
                        createInventorySection()
                );
    }

    private void selectSection(
            Button selected) {

        informationButton.setStyle(
                normalSectionStyle()
        );

        registeredButton.setStyle(
                normalSectionStyle()
        );

        selected.setStyle(
                selectedSectionStyle()
        );
    }

    private void clearFields() {

        selectedAnimal = null;

        if (speciesComboBox != null) {

            speciesComboBox.setValue(
                    null
            );
        }

        if (commonNameField != null) {

            commonNameField.clear();
        }

        if (scientificNameField != null) {

            scientificNameField.clear();
        }

        if (originComboBox != null) {

            originComboBox.setValue(
                    "Admission"
            );
        }

        if (statusComboBox != null) {

            statusComboBox.setValue(
                    "Active"
            );
        }

        table.getSelectionModel()
                .clearSelection();
    }

    private void configureOriginConverter() {

        originComboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            String value) {

                        return translateOrigin(
                                value
                        );
                    }

                    @Override
                    public String fromString(
                            String value) {

                        return value;
                    }
                }
        );
    }

    private void configureStatusConverter() {

        statusComboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            String value) {

                        return translateStatus(
                                value
                        );
                    }

                    @Override
                    public String fromString(
                            String value) {

                        return value;
                    }
                }
        );
    }

    private String translateOrigin(
            String value) {

        if (value == null) {
            return "";
        }

        return switch (value) {

            case "Admission" ->
                    "Ingreso";

            case "Already existing in the reserve" ->
                    "Ya existente en la reserva";

            default ->
                    value;
        };
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

    private String translateLocation(
            String value) {

        if (value == null) {
            return "";
        }

        if (AnimalInventoryService.QUARANTINE
                .equals(value)) {

            return "Cuarentena";
        }

        if (AnimalInventoryService.PERMANENT
                .equals(value)) {

            return "Permanente";
        }

        return value;
    }

    private String translateMovementType(
            String value) {

        return switch (value) {

            case "TRANSFER" ->
                    "Trasladar animales";

            case "DEATH" ->
                    "Registrar muerte";

            case "EXIT" ->
                    "Registrar salida";

            default ->
                    value;
        };
    }

    private String safeValue(
            String value) {

        return value == null
                ? ""
                : value;
    }

    private Button createSectionButton(
            String text,
            boolean selected) {

        Button button =
                new Button(text);

        button.setPrefHeight(
                42
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
                selected
                        ? selectedSectionStyle()
                        : normalSectionStyle()
        );

        return button;
    }

    private String selectedSectionStyle() {

        return """
                -fx-background-color: #23452C;
                -fx-text-fill: white;
                -fx-background-radius: 7;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                """;
    }

    private String normalSectionStyle() {

        return """
                -fx-background-color: transparent;
                -fx-text-fill: #56635C;
                -fx-background-radius: 7;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                """;
    }

    private Label title(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );

        return label;
    }

    private Label label(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4B5752;"
        );

        return label;
    }

    private void configureControl(
            javafx.scene.control.Control control) {

        control.setMaxWidth(
                Double.MAX_VALUE
        );

        control.setPrefHeight(
                40
        );
    }

    private Button primaryButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefHeight(
                40
        );

        button.setStyle(
                "-fx-background-color: #23452C;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 12px;"
        );

        return button;
    }

    private Button secondaryButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefHeight(
                40
        );

        button.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #23452C;" +
                "-fx-border-color: #B8C7B8;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;"
        );

        return button;
    }

    private Button deleteButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefHeight(
                40
        );

        button.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #A94442;" +
                "-fx-border-color: #D8B3B3;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;"
        );

        return button;
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