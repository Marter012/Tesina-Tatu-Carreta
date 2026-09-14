package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.AnimalHoldingDAO;
import com.tesina_tatu_carreta.dao.EnclosureDAO;
import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.AnimalHolding;
import com.tesina_tatu_carreta.model.AnimalInventorySummary;
import com.tesina_tatu_carreta.model.Enclosure;
import com.tesina_tatu_carreta.model.Species;
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
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.MovementDAO;
import com.tesina_tatu_carreta.model.Entry;
import com.tesina_tatu_carreta.model.Movement;

/**
 * Módulo principal de gestión de:
 *
 * - Animales
 * - Especies
 * - Recintos
 *
 * Se accede desde el menú principal mediante "Animales".
 */
public class ViewAnimals {

        // =========================================================
        // DAOS / SERVICES
        // =========================================================

        private final AnimalDAO animalDAO = new AnimalDAO();

        private final SpeciesDAO speciesDAO = new SpeciesDAO();

        private final EnclosureDAO enclosureDAO = new EnclosureDAO();

        private final AnimalHoldingDAO holdingDAO = new AnimalHoldingDAO();

        private final AnimalInventoryService inventoryService = new AnimalInventoryService();

        private final EntryDAO entryDAO = new EntryDAO();

        private final MovementDAO movementDAO = new MovementDAO();

        // =========================================================
        // LISTAS
        // =========================================================

        private final ObservableList<AnimalInventorySummary> inventory =
                        FXCollections.observableArrayList();

        private final ObservableList<Animal> animals =
                        FXCollections.observableArrayList();

        private final ObservableList<Species> species =
                        FXCollections.observableArrayList();

        private final ObservableList<Enclosure> enclosures =
                        FXCollections.observableArrayList();

        private final ObservableList<Animal> registeredAnimals =
                        FXCollections.observableArrayList();

        private final ObservableList<Animal> filteredRegisteredAnimals =
                        FXCollections.observableArrayList();

        // =========================================================
        // CONTENEDORES
        // =========================================================

        private VBox contentContainer;

        // =========================================================
        // BOTONES PRINCIPALES
        // =========================================================

        private Button animalsButton;
        private Button speciesButton;
        private Button enclosuresButton;

        // =========================================================
        // ANIMALES
        // =========================================================

        /*
         * IMPORTANTE:
         *
         * animalTable = tabla del INVENTARIO.
         * Trabaja con AnimalInventorySummary.
         *
         * registeredAnimalTable = tabla de ANIMALES REGISTRADOS.
         * Trabaja con Animal.
         */
        private TableView<AnimalInventorySummary> animalTable;

        private TableView<Animal> registeredAnimalTable;

        private ComboBox<Species> speciesComboBox;

        private TextField commonNameField;

        private TextField scientificNameField;

        private ComboBox<String> originComboBox;

        private ComboBox<String> statusComboBox;

        private TextField quantityField;

        private Animal selectedAnimal;

        // =========================================================
        // ESPECIES
        // =========================================================

        private TableView<Species> speciesTable;

        private TextField speciesNameField;

        private Species selectedSpecies;

        // =========================================================
        // RECINTOS
        // =========================================================

        private TableView<Enclosure> enclosureTable;

        private TextField enclosureSearchField;

        private Enclosure selectedEnclosure;

        // =========================================================
        // VIEW
        // =========================================================

        public Parent getView() {
                return createView();
        }

        public Parent createView() {

                VBox root = new VBox(22);

                root.setPadding(
                                new Insets(30, 35, 35, 35));

                root.setStyle(
                                "-fx-background-color: #F4F1E8;");

                // -----------------------------------------------------
                // HEADER
                // -----------------------------------------------------

                Label breadcrumb = new Label(
                                "Inicio / Gestión de animales");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #8A918E;");

                Label title = new Label(
                                "Gestión de animales");

                title.setStyle(
                                "-fx-font-size: 30px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                Label subtitle = new Label(
                                "Desde aquí puede registrar animales, consultar el inventario, administrar especies y administrar recintos.");

                subtitle.setWrapText(true);

                subtitle.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #405047;");

                VBox header = new VBox(
                                6,
                                breadcrumb,
                                title,
                                subtitle);

                // -----------------------------------------------------
                // NAVEGACIÓN PRINCIPAL
                // -----------------------------------------------------

                HBox navigation = createMainNavigation();

                // -----------------------------------------------------
                // CONTENEDOR
                // -----------------------------------------------------

                contentContainer = new VBox();

                contentContainer.setFillWidth(true);

                VBox.setVgrow(
                                contentContainer,
                                Priority.ALWAYS);

                root.getChildren().addAll(
                                header,
                                navigation,
                                contentContainer);

                // -----------------------------------------------------
                // CARGA INICIAL
                // -----------------------------------------------------

                loadAllData();

                showAnimals();

                // -----------------------------------------------------
                // SCROLL
                // -----------------------------------------------------

                ScrollPane scrollPane = new ScrollPane(root);

                scrollPane.setFitToWidth(true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setStyle(
                                "-fx-background: #F4F1E8;");

                return scrollPane;
        }

        // =========================================================
        // NAVEGACIÓN PRINCIPAL
        // =========================================================

        private HBox createMainNavigation() {

                animalsButton = createMainNavigationButton(
                                "🐾  ANIMALES");

                speciesButton = createMainNavigationButton(
                                "🧬  ESPECIES");

                enclosuresButton = createMainNavigationButton(
                                "🏠  RECINTOS");

                animalsButton.setOnAction(
                                event -> showAnimals());

                speciesButton.setOnAction(
                                event -> showSpecies());

                enclosuresButton.setOnAction(
                                event -> showEnclosures());

                HBox navigation = new HBox(
                                12,
                                animalsButton,
                                speciesButton,
                                enclosuresButton);

                navigation.setAlignment(
                                Pos.CENTER_LEFT);

                navigation.setPadding(
                                new Insets(5, 0, 8, 0));

                return navigation;
        }

        private Button createMainNavigationButton(
                        String text) {

                Button button = new Button(text);

                button.setPrefHeight(50);

                button.setMinWidth(150);

                button.setPadding(
                                new Insets(0, 25, 0, 25));

                button.setStyle(
                                normalMainNavigationStyle());

                return button;
        }

        private void setActiveMainNavigation(
                        Button active) {

                animalsButton.setStyle(
                                normalMainNavigationStyle());

                speciesButton.setStyle(
                                normalMainNavigationStyle());

                enclosuresButton.setStyle(
                                normalMainNavigationStyle());

                active.setStyle(
                                selectedMainNavigationStyle());
        }

        private String normalMainNavigationStyle() {

                return """
                                -fx-background-color: #E2E7E2;
                                -fx-text-fill: #254D3D;
                                -fx-font-size: 14px;
                                -fx-font-weight: bold;
                                -fx-background-radius: 10;
                                -fx-padding: 10 22;
                                """;
        }

        private String selectedMainNavigationStyle() {

                return """
                                -fx-background-color: #254D3D;
                                -fx-text-fill: white;
                                -fx-font-size: 14px;
                                -fx-font-weight: bold;
                                -fx-background-radius: 10;
                                -fx-padding: 10 22;
                                """;
        }

        // =========================================================
        // ANIMALES
        // =========================================================

        private void showAnimals() {

                setActiveMainNavigation(
                                animalsButton);

                loadSpecies();
                loadInventory();
                loadRegisteredAnimals();

                VBox root = new VBox(20);

                // -----------------------------------------------------
                // ACCIONES RÁPIDAS
                // -----------------------------------------------------

                VBox actionsCard = createCard();

                Label actionsTitle = createSectionTitle(
                                "¿Qué desea hacer?");

                Label actionsDescription = new Label(
                                "Seleccione una acción para comenzar.");

                actionsDescription.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 13px;");

                HBox actionButtons = new HBox(15);

                actionButtons.setAlignment(
                                Pos.CENTER_LEFT);

                Button registerButton = createLargeActionButton(
                                "➕",
                                "Registrar animal",
                                "Agregar un nuevo animal");

                Button viewButton = createLargeActionButton(
                                "📋",
                                "Ver animales",
                                "Consultar el inventario");

                registerButton.setOnAction(
                                event -> showAnimalForm());

                viewButton.setOnAction(
                                event -> showAnimalInventory());

                actionButtons.getChildren().addAll(
                                registerButton,
                                viewButton);

                actionsCard.getChildren().addAll(
                                actionsTitle,
                                actionsDescription,
                                actionButtons);

                // -----------------------------------------------------
                // RESUMEN
                // -----------------------------------------------------

                HBox statistics = createAnimalStatistics();

                // -----------------------------------------------------
                // TABLA
                // -----------------------------------------------------

                VBox inventoryCard = createAnimalInventoryCard();

                root.getChildren().addAll(
                                actionsCard,
                                statistics,
                                inventoryCard);

                contentContainer
                                .getChildren()
                                .setAll(root);
        }

        private HBox createAnimalStatistics() {

                int permanent = holdingDAO.getTotalQuantity(
                                AnimalInventoryService.PERMANENT);

                int quarantine = holdingDAO.getTotalQuantity(
                                AnimalInventoryService.QUARANTINE);

                int total = permanent + quarantine;

                VBox totalBox = createStatistic(
                                "ANIMALES ACTUALES",
                                String.valueOf(total));

                VBox permanentBox = createStatistic(
                                "PLANTEL PERMANENTE",
                                String.valueOf(permanent));

                VBox quarantineBox = createStatistic(
                                "CUARENTENA",
                                String.valueOf(quarantine));

                HBox statistics = new HBox(
                                15,
                                totalBox,
                                permanentBox,
                                quarantineBox);

                return statistics;
        }

        private VBox createStatistic(
                        String label,
                        String value) {

                VBox box = new VBox(5);

                box.setPadding(
                                new Insets(18));

                box.setAlignment(
                                Pos.CENTER_LEFT);

                box.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 12;");

                Label valueLabel = new Label(value);

                valueLabel.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                Label textLabel = new Label(label);

                textLabel.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #6B756F;");

                box.getChildren().addAll(
                                valueLabel,
                                textLabel);

                HBox.setHgrow(
                                box,
                                Priority.ALWAYS);

                return box;
        }

        private VBox createAnimalInventoryCard() {

                VBox card = createCard();

                HBox header = new HBox();

                VBox text = new VBox(4);

                Label title = createSectionTitle(
                                "Animales registrados");

                Label description = new Label(
                                "Aquí se muestran todos los animales registrados en la reserva. " +
                                                "Puede filtrar por estado y hacer doble clic sobre un animal para consultar su acta.");

                description.setWrapText(true);

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                text.getChildren().addAll(
                                title,
                                description);

                Button refresh = secondaryButton(
                                "ACTUALIZAR");

                refresh.setOnAction(
                                event -> {

                                        loadRegisteredAnimals();

                                        applyAnimalFilter();
                                });

                header.getChildren().addAll(
                                text,
                                refresh);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                HBox.setHgrow(
                                text,
                                Priority.ALWAYS);

                // -----------------------------------------------------
                // FILTRO
                // -----------------------------------------------------

                ComboBox<String> filter = new ComboBox<>();

                filter.getItems().addAll(
                                "Todos",
                                "Vivos",
                                "Muertos");

                filter.setValue(
                                "Todos");

                filter.setPrefWidth(
                                180);

                filter.setPrefHeight(
                                40);

                Label filterLabel = new Label(
                                "Mostrar:");

                filterLabel.setStyle(
                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #405047;");

                HBox filterBox = new HBox(
                                10,
                                filterLabel,
                                filter);

                filterBox.setAlignment(
                                Pos.CENTER_LEFT);

                // -----------------------------------------------------
                // TABLA DE ANIMALES REGISTRADOS
                // -----------------------------------------------------

                registeredAnimalTable = new TableView<>();

                createRegisteredAnimalTable();

                filter.valueProperty().addListener(
                                (observable, oldValue, newValue) -> {

                                        applyAnimalFilter(
                                                        newValue);
                                });

                // -----------------------------------------------------
                // DOBLE CLICK
                // -----------------------------------------------------

                registeredAnimalTable.setOnMouseClicked(
                                event -> {

                                        if (event.getClickCount() == 2
                                                        && !registeredAnimalTable
                                                                        .getSelectionModel()
                                                                        .isEmpty()) {

                                                Animal selected = registeredAnimalTable
                                                                .getSelectionModel()
                                                                .getSelectedItem();

                                                if (selected != null) {

                                                        openAnimalInformation(
                                                                        selected);
                                                }
                                        }
                                });

                card.getChildren().addAll(
                                header,
                                filterBox,
                                registeredAnimalTable);

                VBox.setVgrow(
                                registeredAnimalTable,
                                Priority.ALWAYS);

                return card;
        }

        private void createRegisteredAnimalTable() {

                if (registeredAnimalTable == null) {
                        return;
                }

                registeredAnimalTable.getColumns().clear();

                registeredAnimalTable.setItems(
                                filteredRegisteredAnimals);

                registeredAnimalTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<Animal, Number> idColumn =
                                new TableColumn<>("ID");

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getAnimalId()));

                TableColumn<Animal, String> animalColumn =
                                new TableColumn<>("Animal");

                animalColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                safeValue(
                                                                data.getValue()
                                                                                .getCommonName())));

                TableColumn<Animal, String> scientificColumn =
                                new TableColumn<>("Nombre científico");

                scientificColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                safeValue(
                                                                data.getValue()
                                                                                .getScientificName())));

                TableColumn<Animal, Number> quantityColumn =
                                new TableColumn<>("Cantidad actual");

                quantityColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getCurrentQuantity()));

                TableColumn<Animal, String> originColumn =
                                new TableColumn<>("Origen");

                originColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                translateOrigin(
                                                                data.getValue()
                                                                                .getOrigin())));

                TableColumn<Animal, String> statusColumn =
                                new TableColumn<>("Estado");

                statusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                isAnimalDead(
                                                                data.getValue())
                                                                                ? "Muerto"
                                                                                : "Vivo"));

                registeredAnimalTable.getColumns().addAll(
                                idColumn,
                                animalColumn,
                                scientificColumn,
                                quantityColumn,
                                originColumn,
                                statusColumn);

                registeredAnimalTable.setPrefHeight(
                                430);
        }

        private void loadRegisteredAnimals() {

                registeredAnimals.setAll(
                                animalDAO.list());

                applyAnimalFilter();
        }

        private void applyAnimalFilter() {

                applyAnimalFilter(
                                "Todos");
        }

        private void applyAnimalFilter(
                        String filter) {

                filteredRegisteredAnimals.clear();

                if (filter == null
                                || filter.equals("Todos")) {

                        filteredRegisteredAnimals.addAll(
                                        registeredAnimals);

                        return;
                }

                for (Animal animal : registeredAnimals) {

                        boolean dead = isAnimalDead(
                                        animal);

                        if ("Vivos".equals(filter)
                                        && !dead) {

                                filteredRegisteredAnimals.add(
                                                animal);
                        }

                        if ("Muertos".equals(filter)
                                        && dead) {

                                filteredRegisteredAnimals.add(
                                                animal);
                        }
                }
        }

        private void openAnimalInformation(
                        Animal animal) {

                List<Entry> entries = entryDAO.listByAnimal(
                                animal.getAnimalId());

                if (entries == null
                                || entries.isEmpty()) {

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Animal sin acta",
                                        "El animal \"" +
                                                        safeValue(
                                                                        animal.getCommonName())
                                                        +
                                                        "\" fue ingresado manualmente " +
                                                        "y no posee un acta de ingreso asociada.");

                        return;
                }

                Entry entry = entries.get(0);

                openAnimalEntryModal(
                                animal,
                                entry);
        }

        private void openAnimalEntryModal(
                        Animal animal,
                        Entry entry) {

                VBox content = createCard();

                Label title = createSectionTitle(
                                "Acta de ingreso");

                Label animalTitle = new Label(
                                safeValue(
                                                animal.getCommonName()));

                animalTitle.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                GridPane information = new GridPane();

                information.setHgap(20);
                information.setVgap(14);

                information.add(
                                label("Número de acta"),
                                0,
                                0);

                information.add(
                                new Label(
                                                safeValue(
                                                                entry.getRecordNumber())),
                                1,
                                0);

                information.add(
                                label("Fecha de ingreso"),
                                0,
                                1);

                information.add(
                                new Label(
                                                safeValue(
                                                                entry.getEntryDate())),
                                1,
                                1);

                information.add(
                                label("Organización de origen"),
                                0,
                                2);

                information.add(
                                new Label(
                                                safeValue(
                                                                entry.getSourceOrganization())),
                                1,
                                2);

                information.add(
                                label("Responsable de entrega"),
                                0,
                                3);

                information.add(
                                new Label(
                                                safeValue(
                                                                entry.getDeliveryResponsible())),
                                1,
                                3);

                information.add(
                                label("Origen"),
                                0,
                                4);

                information.add(
                                new Label(
                                                safeValue(
                                                                entry.getOrigin())),
                                1,
                                4);

                information.add(
                                label("Motivo del ingreso"),
                                0,
                                5);

                information.add(
                                new Label(
                                                safeValue(
                                                                entry.getEntryReason())),
                                1,
                                5);

                information.add(
                                label("Documentación"),
                                0,
                                6);

                TextArea documentation = new TextArea(
                                safeValue(
                                                entry.getDocumentation()));

                documentation.setEditable(
                                false);

                documentation.setWrapText(
                                true);

                documentation.setPrefRowCount(
                                3);

                information.add(
                                documentation,
                                1,
                                6);

                information.add(
                                label("Observaciones"),
                                0,
                                7);

                TextArea observations = new TextArea(
                                safeValue(
                                                entry.getObservations()));

                observations.setEditable(
                                false);

                observations.setWrapText(
                                true);

                observations.setPrefRowCount(
                                4);

                information.add(
                                observations,
                                1,
                                7);

                GridPane.setHgrow(
                                documentation,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                observations,
                                Priority.ALWAYS);

                Button close = secondaryButton(
                                "CERRAR");

                VBox box = new VBox(
                                18,
                                title,
                                animalTitle,
                                information,
                                close);

                box.setPadding(
                                new Insets(25));

                VBox.setVgrow(
                                information,
                                Priority.ALWAYS);

                Stage stage = createModalStage(
                                "Acta de ingreso",
                                box);

                close.setOnAction(
                                event -> stage.close());

                stage.showAndWait();
        }

        // =========================================================
        // TABLA ORIGINAL DE INVENTARIO
        // =========================================================

        private void createAnimalTable() {

                animalTable.getColumns().clear();

                animalTable.setItems(
                                inventory);

                animalTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<AnimalInventorySummary, String> animalColumn =
                                new TableColumn<>(
                                                "Animal");

                animalColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getCommonName()));

                TableColumn<AnimalInventorySummary, String> scientificColumn =
                                new TableColumn<>(
                                                "Nombre científico");

                scientificColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getScientificName()));

                TableColumn<AnimalInventorySummary, Number> permanentColumn =
                                new TableColumn<>(
                                                "Permanente");

                permanentColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getPermanentQuantity()));

                TableColumn<AnimalInventorySummary, Number> quarantineColumn =
                                new TableColumn<>(
                                                "Cuarentena");

                quarantineColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getQuarantineQuantity()));

                TableColumn<AnimalInventorySummary, Number> totalColumn =
                                new TableColumn<>(
                                                "TOTAL");

                totalColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getTotalQuantity()));

                TableColumn<AnimalInventorySummary, String> statusColumn =
                                new TableColumn<>(
                                                "Estado");

                statusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                translateStatus(
                                                                data.getValue()
                                                                                .getStatus())));

                animalTable.getColumns().addAll(
                                animalColumn,
                                scientificColumn,
                                permanentColumn,
                                quarantineColumn,
                                totalColumn,
                                statusColumn);

                animalTable.setPrefHeight(
                                430);

                animalTable
                                .getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable, oldValue, newValue) -> {

                                                        if (newValue != null) {

                                                                Animal animal =
                                                                                animalDAO.findById(
                                                                                                newValue.getAnimalId());

                                                                if (animal != null) {

                                                                        selectedAnimal =
                                                                                        animal;
                                                                }
                                                        }
                                                });
        }

        // =========================================================
        // FORMULARIO DE ANIMAL
        // =========================================================

        private void showAnimalForm() {

                setActiveMainNavigation(
                                animalsButton);

                VBox root = new VBox(20);

                Label title = createSectionTitle(
                                "Registrar animal");

                Label description = new Label(
                                "Complete los datos del animal. Si el animal ya existía en la reserva y no posee acta, puede incorporarlo directamente al plantel permanente.");

                description.setWrapText(true);

                description.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 13px;");

                // =====================================================
                // BOTÓN VOLVER
                // =====================================================

                Button backButton = secondaryButton(
                                "← VOLVER A ANIMALES");

                backButton.setOnAction(
                                event -> showAnimals());

                HBox header = new HBox(
                                15);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                VBox headerText = new VBox(
                                4,
                                title,
                                description);

                HBox.setHgrow(
                                headerText,
                                Priority.ALWAYS);

                header.getChildren().addAll(
                                headerText,
                                backButton);

                VBox card = createCard();

                speciesComboBox = new ComboBox<>();

                loadSpecies();

                commonNameField = new TextField();

                scientificNameField = new TextField();

                originComboBox = new ComboBox<>();

                statusComboBox = new ComboBox<>();

                quantityField = new TextField();

                speciesComboBox.setPromptText(
                                "Seleccione una especie");

                commonNameField.setPromptText(
                                "Ejemplo: Puma");

                scientificNameField.setPromptText(
                                "Ejemplo: Puma concolor");

                originComboBox.getItems().setAll(
                                "Admission",
                                "Already existing in the reserve");

                statusComboBox.getItems().setAll(
                                "Active",
                                "Inactive");

                originComboBox.setValue(
                                "Admission");

                statusComboBox.setValue(
                                "Active");

                quantityField.setPromptText(
                                "Cantidad de ejemplares");

                configureOriginConverter();

                configureStatusConverter();

                configureControl(
                                speciesComboBox);

                configureControl(
                                commonNameField);

                configureControl(
                                scientificNameField);

                configureControl(
                                originComboBox);

                configureControl(
                                statusComboBox);

                configureControl(
                                quantityField);

                GridPane form = new GridPane();

                form.setHgap(20);

                form.setVgap(15);

                form.add(
                                label("Especie"),
                                0,
                                0);

                form.add(
                                speciesComboBox,
                                0,
                                1);

                form.add(
                                label("Nombre común"),
                                1,
                                0);

                form.add(
                                commonNameField,
                                1,
                                1);

                form.add(
                                label("Nombre científico"),
                                0,
                                2);

                form.add(
                                scientificNameField,
                                0,
                                3);

                form.add(
                                label("Origen"),
                                1,
                                2);

                form.add(
                                originComboBox,
                                1,
                                3);

                form.add(
                                label("Estado"),
                                0,
                                4);

                form.add(
                                statusComboBox,
                                0,
                                5);

                Label quantityLabel = label(
                                "Cantidad");

                form.add(
                                quantityLabel,
                                1,
                                4);

                form.add(
                                quantityField,
                                1,
                                5);

                GridPane.setHgrow(
                                speciesComboBox,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                commonNameField,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                scientificNameField,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                originComboBox,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                statusComboBox,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                quantityField,
                                Priority.ALWAYS);

                Label permanentHelp = new Label(
                                "ℹ Si selecciona \"Ya existente en la reserva\", los ejemplares se registrarán directamente en Plantel Permanente y no necesitarán un acta de ingreso.");

                permanentHelp.setWrapText(true);

                permanentHelp.setStyle(
                                "-fx-background-color: #E8EEE9;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-padding: 12;" +
                                                "-fx-background-radius: 8;");

                Button clear = secondaryButton(
                                "LIMPIAR");

                Button save = primaryButton(
                                "REGISTRAR ANIMAL");

                clear.setOnAction(
                                event -> clearAnimalFields());

                save.setOnAction(
                                event -> addAnimal());

                HBox actions = new HBox(
                                10,
                                clear,
                                save);

                actions.setAlignment(
                                Pos.CENTER_RIGHT);

                card.getChildren().addAll(
                                title,
                                form,
                                permanentHelp,
                                actions);

                root.getChildren().addAll(
                                header,
                                card);

                contentContainer
                                .getChildren()
                                .setAll(root);
        }

        // =========================================================
        // AGREGAR ANIMAL
        // =========================================================

        private void addAnimal() {

                if (!validateAnimalFields()) {
                        return;
                }

                int quantity;

                try {

                        quantity = Integer.parseInt(
                                        quantityField
                                                        .getText()
                                                        .trim());

                        if (quantity <= 0) {

                                throw new NumberFormatException();
                        }

                } catch (NumberFormatException exception) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Cantidad incorrecta",
                                        "Ingrese una cantidad mayor que cero.");

                        return;
                }

                Animal animal = new Animal();

                animal.setSpeciesId(
                                speciesComboBox
                                                .getValue()
                                                .getSpeciesId());

                animal.setCommonName(
                                commonNameField
                                                .getText()
                                                .trim());

                animal.setScientificName(
                                scientificNameField
                                                .getText()
                                                .trim());

                animal.setCurrentQuantity(
                                0);

                animal.setOrigin(
                                originComboBox
                                                .getValue());

                animal.setStatus(
                                statusComboBox
                                                .getValue());

                // -----------------------------------------------------
                // ANIMAL SIN ACTA
                // -----------------------------------------------------

                if ("Already existing in the reserve"
                                .equals(
                                                originComboBox.getValue())) {

                        addAnimalWithoutEntry(
                                        animal,
                                        quantity);

                        return;
                }

                // -----------------------------------------------------
                // ANIMAL NORMAL
                // -----------------------------------------------------

                animalDAO.add(
                                animal);

                clearAnimalFields();

                loadInventory();

                showMessage(
                                Alert.AlertType.INFORMATION,
                                "Operación exitosa",
                                "El animal fue registrado correctamente.\n\n"
                                                + "El acta de ingreso se deberá registrar desde el módulo Ingresos.");

                showAnimals();
        }

        // =========================================================
        // ANIMAL SIN ACTA
        // =========================================================

        private void addAnimalWithoutEntry(
                        Animal animal,
                        int quantity) {

                try (
                                Connection connection =
                                                SQLiteConnection.connect()) {

                        connection.setAutoCommit(
                                        false);

                        try {

                                // -------------------------------------------------
                                // 1. CREAR ANIMAL
                                // -------------------------------------------------

                                animalDAO.add(
                                                animal);

                                /*
                                 * El DAO de animales ya genera el ID.
                                 * Se obtiene desde el objeto.
                                 */
                                int animalId =
                                                animal.getAnimalId();

                                // -------------------------------------------------
                                // 2. CREAR HOLDING PERMANENTE
                                // -------------------------------------------------

                                AnimalHolding holding =
                                                new AnimalHolding();

                                holding.setAnimalId(
                                                animalId);

                                // Sin acta
                                holding.setEntryId(
                                                null);

                                holding.setLocationType(
                                                AnimalInventoryService.PERMANENT);

                                holding.setQuantity(
                                                quantity);

                                holding.setStatus(
                                                "Active");

                                holdingDAO.add(
                                                connection,
                                                holding);

                                // -------------------------------------------------
                                // 3. SINCRONIZAR CANTIDAD DEL ANIMAL
                                // -------------------------------------------------

                                String sql = """
                                                UPDATE animals
                                                SET current_quantity = ?
                                                WHERE animal_id = ?
                                                """;

                                try (
                                                PreparedStatement statement =
                                                                connection.prepareStatement(
                                                                                sql)) {

                                        statement.setInt(
                                                        1,
                                                        quantity);

                                        statement.setInt(
                                                        2,
                                                        animalId);

                                        statement.executeUpdate();
                                }

                                connection.commit();

                                clearAnimalFields();

                                loadInventory();

                                showMessage(
                                                Alert.AlertType.INFORMATION,
                                                "Animal incorporado",
                                                "El animal fue registrado correctamente.\n\n"
                                                                + "Cantidad: "
                                                                + quantity
                                                                + "\n"
                                                                + "Ubicación: Plantel Permanente\n"
                                                                + "Acta de ingreso: Sin acta");

                                showAnimals();

                        } catch (Exception exception) {

                                connection.rollback();

                                throw exception;
                        }

                } catch (Exception exception) {

                        showMessage(
                                        Alert.AlertType.ERROR,
                                        "No se pudo registrar el animal",
                                        getExceptionMessage(
                                                        exception));
                }
        }

        // =========================================================
        // VALIDAR ANIMAL
        // =========================================================

        private boolean validateAnimalFields() {

                if (speciesComboBox == null
                                || speciesComboBox.getValue() == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "Debe seleccionar una especie.");

                        return false;
                }

                if (commonNameField.getText()
                                .isBlank()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "El nombre común es obligatorio.");

                        return false;
                }

                if (scientificNameField.getText()
                                .isBlank()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "El nombre científico es obligatorio.");

                        return false;
                }

                if (originComboBox.getValue() == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "Debe seleccionar un origen.");

                        return false;
                }

                if (statusComboBox.getValue() == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "Debe seleccionar un estado.");

                        return false;
                }

                if (quantityField == null
                                || quantityField.getText()
                                                .isBlank()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "Debe indicar la cantidad de ejemplares.");

                        return false;
                }

                return true;
        }

        // =========================================================
        // INVENTARIO
        // =========================================================

        private void showAnimalInventory() {

                setActiveMainNavigation(
                                animalsButton);

                loadInventory();

                Button backButton = secondaryButton(
                                "← VOLVER A ANIMALES");

                backButton.setOnAction(
                                event -> showAnimals());

                VBox root = new VBox(18);

                Label title = createSectionTitle(
                                "Inventario de animales");

                Label description = new Label(
                                "Consulte la ubicación actual de los ejemplares y realice operaciones sobre ellos.");

                HBox header = new HBox(
                                15);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                VBox headerText = new VBox(
                                4,
                                title,
                                description);

                HBox.setHgrow(
                                headerText,
                                Priority.ALWAYS);

                header.getChildren().addAll(
                                headerText,
                                backButton);

                description.setWrapText(true);

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                VBox card = createCard();

                TableView<AnimalInventorySummary> table =
                                new TableView<>();

                table.setItems(
                                inventory);

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                createInventoryColumns(
                                table);

                table.setPrefHeight(
                                400);

                HBox actions = new HBox(
                                10);

                actions.setAlignment(
                                Pos.CENTER_RIGHT);

                Button refresh = secondaryButton(
                                "ACTUALIZAR");

                Button transfer = primaryButton(
                                "TRASLADAR");

                Button death = deleteButton(
                                "REGISTRAR MUERTE");

                Button exit = secondaryButton(
                                "REGISTRAR SALIDA");

                refresh.setOnAction(
                                event -> {
                                        loadInventory();
                                        showAnimalInventory();
                                });

                transfer.setOnAction(
                                event -> openMovementDialog(
                                                table,
                                                "TRANSFER"));

                death.setOnAction(
                                event -> openMovementDialog(
                                                table,
                                                "DEATH"));

                exit.setOnAction(
                                event -> openMovementDialog(
                                                table,
                                                "EXIT"));

                actions.getChildren().addAll(
                                refresh,
                                transfer,
                                death,
                                exit);

                card.getChildren().addAll(
                                table,
                                actions);

                root.getChildren().addAll(
                                header,
                                card);

                contentContainer
                                .getChildren()
                                .setAll(root);
        }

        private void createInventoryColumns(
                        TableView<AnimalInventorySummary> table) {

                table.getColumns().clear();

                TableColumn<AnimalInventorySummary, String> animalColumn =
                                new TableColumn<>(
                                                "Animal");

                animalColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getCommonName()));

                TableColumn<AnimalInventorySummary, String> scientificColumn =
                                new TableColumn<>(
                                                "Nombre científico");

                scientificColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getScientificName()));

                TableColumn<AnimalInventorySummary, Number> permanentColumn =
                                new TableColumn<>(
                                                "Permanente");

                permanentColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getPermanentQuantity()));

                TableColumn<AnimalInventorySummary, Number> quarantineColumn =
                                new TableColumn<>(
                                                "Cuarentena");

                quarantineColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getQuarantineQuantity()));

                TableColumn<AnimalInventorySummary, Number> totalColumn =
                                new TableColumn<>(
                                                "TOTAL");

                totalColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getTotalQuantity()));

                TableColumn<AnimalInventorySummary, String> statusColumn =
                                new TableColumn<>(
                                                "Estado");

                statusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                translateStatus(
                                                                data.getValue()
                                                                                .getStatus())));

                table.getColumns().addAll(
                                animalColumn,
                                scientificColumn,
                                permanentColumn,
                                quarantineColumn,
                                totalColumn,
                                statusColumn);

                table.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable, oldValue, newValue) -> {

                                                        if (newValue != null) {

                                                                selectedAnimal =
                                                                                animalDAO.findById(
                                                                                                newValue.getAnimalId());
                                                        }
                                                });
        }

        // =========================================================
        // MOVIMIENTOS
        // =========================================================

        private void openMovementDialog(
                        TableView<AnimalInventorySummary> table,
                        String movementType) {

                AnimalInventorySummary selected =
                                table.getSelectionModel()
                                                .getSelectedItem();

                if (selected == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Seleccione un animal",
                                        "Primero debe seleccionar el animal sobre el que desea realizar la operación.");

                        return;
                }

                List<AnimalHolding> holdings =
                                holdingDAO.listByAnimal(
                                                selected.getAnimalId());

                if (holdings.isEmpty()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin animales disponibles",
                                        "Este animal no tiene ejemplares disponibles para realizar esta operación.");

                        return;
                }

                // =====================================================
                // VENTANA MODAL
                // =====================================================

                Stage stage = new Stage();

                stage.setTitle(
                                translateMovementType(
                                                movementType));

                stage.initModality(
                                Modality.APPLICATION_MODAL);

                // =====================================================
                // CONTROLES
                // =====================================================

                ComboBox<AnimalHolding> holdingComboBox =
                                new ComboBox<>(
                                                FXCollections.observableArrayList(
                                                                holdings));

                holdingComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                holdingComboBox.setPrefHeight(
                                42);

                holdingComboBox.setConverter(
                                new StringConverter<>() {

                                        @Override
                                        public String toString(
                                                        AnimalHolding holding) {

                                                if (holding == null) {
                                                        return "";
                                                }

                                                return formatHolding(
                                                                holding);
                                        }

                                        @Override
                                        public AnimalHolding fromString(
                                                        String value) {

                                                return null;
                                        }
                                });

                TextField quantity = new TextField();

                quantity.setPromptText(
                                "Ingrese la cantidad");

                quantity.setPrefHeight(
                                42);

                TextField destination = new TextField();

                destination.setPromptText(
                                "Ejemplo: otra reserva");

                destination.setPrefHeight(
                                42);

                TextArea observations = new TextArea();

                observations.setPromptText(
                                "Observaciones...");

                observations.setWrapText(
                                true);

                observations.setPrefRowCount(
                                4);

                Label available = new Label(
                                "Seleccione una ubicación");

                available.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                holdingComboBox.setOnAction(
                                event -> {

                                        AnimalHolding holding =
                                                        holdingComboBox.getValue();

                                        if (holding == null) {

                                                available.setText(
                                                                "Seleccione una ubicación");

                                                return;
                                        }

                                        available.setText(
                                                        "Hay "
                                                                        + holding.getQuantity()
                                                                        + " animales disponibles");
                                });

                // =====================================================
                // CONTENIDO
                // =====================================================

                VBox content = new VBox(
                                13);

                Label animalLabel = new Label(
                                "Animal");

                Label animalValue = new Label(
                                selected.getCommonName());

                animalValue.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                content.getChildren().addAll(
                                animalLabel,
                                animalValue,

                                new Label(
                                                "¿De dónde se retiran los animales?"),

                                holdingComboBox,

                                new Label(
                                                "Cantidad disponible"),

                                available,

                                new Label(
                                                "¿Cuántos animales desea registrar?"),

                                quantity);

                ComboBox<String> target = null;

                if ("TRANSFER".equals(
                                movementType)) {

                        target = new ComboBox<>();

                        target.getItems().addAll(
                                        AnimalInventoryService.QUARANTINE,
                                        AnimalInventoryService.PERMANENT);

                        target.setConverter(
                                        new StringConverter<>() {

                                                @Override
                                                public String toString(
                                                                String value) {

                                                        return translateLocation(
                                                                        value);
                                                }

                                                @Override
                                                public String fromString(
                                                                String value) {

                                                        return value;
                                                }
                                        });

                        target.setMaxWidth(
                                        Double.MAX_VALUE);

                        target.setPrefHeight(
                                        42);

                        content.getChildren().addAll(
                                        new Label(
                                                        "¿A dónde se trasladan?"),
                                        target);

                } else if ("EXIT".equals(
                                movementType)) {

                        content.getChildren().addAll(
                                        new Label(
                                                        "¿A qué lugar salen?"),
                                        destination);
                }

                content.getChildren().addAll(
                                new Label(
                                                "Observaciones"),
                                observations);

                // =====================================================
                // BOTONES
                // =====================================================

                Button cancel = secondaryButton(
                                "CANCELAR");

                Button save = primaryButton(
                                movementButtonText(
                                                movementType));

                HBox buttons = new HBox(
                                10,
                                cancel,
                                save);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox box = new VBox(
                                18,
                                content,
                                buttons);

                box.setPadding(
                                new Insets(25));

                // =====================================================
                // SCROLL
                // =====================================================

                ScrollPane scrollPane = new ScrollPane(
                                box);

                scrollPane.setFitToWidth(
                                true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                Scene scene = new Scene(
                                scrollPane,
                                650,
                                600);

                stage.setScene(
                                scene);

                stage.setMinWidth(
                                600);

                stage.setMinHeight(
                                500);

                // =====================================================
                // CANCELAR / CERRAR
                // =====================================================

                cancel.setOnAction(
                                event -> stage.close());

                // =====================================================
                // GUARDAR
                // =====================================================

                ComboBox<String> finalTarget = target;

                save.setOnAction(
                                event -> {
                                                                                try {

                                                AnimalHolding holding =
                                                                holdingComboBox.getValue();

                                                if (holding == null) {

                                                        throw new IllegalArgumentException(
                                                                        "Debe seleccionar de dónde se retiran los animales.");
                                                }

                                                int amount = parseQuantity(
                                                                quantity.getText()
                                                                                .trim());

                                                if (amount > holding.getQuantity()) {

                                                        throw new IllegalArgumentException(
                                                                        "La cantidad indicada supera la cantidad disponible.");
                                                }

                                                // =================================================
                                                // TRASLADO
                                                // =================================================

                                                if ("TRANSFER".equals(
                                                                movementType)) {

                                                        if (finalTarget == null
                                                                        || finalTarget.getValue() == null) {

                                                                throw new IllegalArgumentException(
                                                                                "Debe seleccionar el lugar de destino.");
                                                        }

                                                        String targetLocation =
                                                                        finalTarget.getValue();

                                                        if (holding.getLocationType()
                                                                        .equals(
                                                                                        targetLocation)) {

                                                                throw new IllegalArgumentException(
                                                                                "El lugar de destino debe ser diferente del lugar actual.");
                                                        }

                                                        if (!confirmAction(
                                                                        "Confirmar traslado",
                                                                        "¿Desea trasladar "
                                                                                        + amount
                                                                                        + " animal(es) de "
                                                                                        + translateLocation(
                                                                                                        holding.getLocationType())
                                                                                        + " a "
                                                                                        + translateLocation(
                                                                                                        targetLocation)
                                                                                        + "?")) {

                                                                return;
                                                        }

                                                        inventoryService.transfer(
                                                                        selected.getAnimalId(),
                                                                        holding.getEntryId(),
                                                                        holding.getLocationType(),
                                                                        targetLocation,
                                                                        amount,
                                                                        observations
                                                                                        .getText()
                                                                                        .trim());

                                                        // =================================================
                                                        // MUERTE
                                                        // =================================================

                                                } else if ("DEATH".equals(
                                                                movementType)) {

                                                        if (!confirmAction(
                                                                        "Confirmar muerte",
                                                                        "¿Confirma que "
                                                                                        + amount
                                                                                        + " animal(es) han muerto?")) {

                                                                return;
                                                        }

                                                        inventoryService.registerDeath(
                                                                        selected.getAnimalId(),
                                                                        holding.getEntryId(),
                                                                        holding.getLocationType(),
                                                                        amount,
                                                                        observations
                                                                                        .getText()
                                                                                        .trim());

                                                        // =================================================
                                                        // SALIDA
                                                        // =================================================

                                                } else {

                                                        String destinationText =
                                                                        destination
                                                                                        .getText()
                                                                                        .trim();

                                                        if (destinationText.isBlank()) {

                                                                throw new IllegalArgumentException(
                                                                                "Debe indicar el lugar al que salen los animales.");
                                                        }

                                                        if (!confirmAction(
                                                                        "Confirmar salida",
                                                                        "¿Confirma la salida de "
                                                                                        + amount
                                                                                        + " animal(es) hacia "
                                                                                        + destinationText
                                                                                        + "?")) {

                                                                return;
                                                        }

                                                        inventoryService.registerExit(
                                                                        selected.getAnimalId(),
                                                                        holding.getEntryId(),
                                                                        holding.getLocationType(),
                                                                        amount,
                                                                        destinationText,
                                                                        observations
                                                                                        .getText()
                                                                                        .trim());
                                                }

                                                // =================================================
                                                // OPERACIÓN EXITOSA
                                                // =================================================

                                                stage.close();

                                                loadInventory();

                                                showMessage(
                                                                Alert.AlertType.INFORMATION,
                                                                "Operación realizada",
                                                                movementSuccessMessage(
                                                                                movementType));

                                                showAnimalInventory();

                                        } catch (NumberFormatException exception) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Cantidad incorrecta",
                                                                "Ingrese una cantidad válida usando solamente números.");

                                        } catch (Exception exception) {

                                                showMessage(
                                                                Alert.AlertType.ERROR,
                                                                "No se pudo realizar la operación",
                                                                getExceptionMessage(
                                                                                exception));
                                        }
                                });

                // =====================================================
                // MOSTRAR VENTANA
                // =====================================================

                stage.showAndWait();
        }

        // =========================================================
        // ESPECIES
        // =========================================================

        private void showSpecies() {

                setActiveMainNavigation(
                                speciesButton);

                loadSpecies();

                VBox root = new VBox(18);

                Label title = createSectionTitle(
                                "Gestión de especies");

                Label description = new Label(
                                "Agregue, consulte, modifique o elimine las especies registradas.");

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                VBox formCard = createCard();

                speciesNameField = new TextField();

                speciesNameField.setPromptText(
                                "Ingrese el nombre de la especie");

                configureControl(
                                speciesNameField);

                HBox form = new HBox(
                                15);

                VBox field = new VBox(
                                6,
                                label(
                                                "Nombre de la especie"),
                                speciesNameField);

                HBox.setHgrow(
                                field,
                                Priority.ALWAYS);

                Button clear = secondaryButton(
                                "LIMPIAR");

                Button add = primaryButton(
                                "AGREGAR ESPECIE");

                Button edit = secondaryButton(
                                "GUARDAR CAMBIOS");

                Button delete = deleteButton(
                                "ELIMINAR");

                clear.setOnAction(
                                event -> clearSpeciesFields());

                add.setOnAction(
                                event -> addSpecies());

                edit.setOnAction(
                                event -> updateSpecies());

                delete.setOnAction(
                                event -> deleteSpecies());

                HBox buttons = new HBox(
                                8,
                                clear,
                                edit,
                                delete,
                                add);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                form.getChildren().addAll(
                                field,
                                buttons);

                formCard.getChildren().add(
                                form);

                // -----------------------------------------------------
                // TABLA
                // -----------------------------------------------------

                VBox tableCard = createCard();

                speciesTable = new TableView<>();

                createSpeciesTable();

                Button refresh = secondaryButton(
                                "ACTUALIZAR");

                refresh.setOnAction(
                                event -> {
                                        loadSpecies();
                                        createSpeciesTable();
                                });

                tableCard.getChildren().addAll(
                                createSectionTitle(
                                                "Especies registradas"),
                                speciesTable,
                                refresh);

                VBox.setVgrow(
                                speciesTable,
                                Priority.ALWAYS);

                root.getChildren().addAll(
                                new VBox(
                                                4,
                                                title,
                                                description),
                                formCard,
                                tableCard);

                contentContainer
                                .getChildren()
                                .setAll(root);
        }

        private void createSpeciesTable() {

                if (speciesTable == null) {
                        return;
                }

                speciesTable.getColumns().clear();

                speciesTable.setItems(
                                species);

                speciesTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<Species, Number> idColumn =
                                new TableColumn<>(
                                                "ID");

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getSpeciesId()));

                TableColumn<Species, String> nameColumn =
                                new TableColumn<>(
                                                "Nombre de la especie");

                nameColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getName()));

                speciesTable.getColumns().addAll(
                                idColumn,
                                nameColumn);

                speciesTable.setPrefHeight(
                                400);

                speciesTable
                                .getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable, oldValue, newValue) -> {

                                                        if (newValue != null) {

                                                                selectedSpecies =
                                                                                newValue;

                                                                if (speciesNameField != null) {

                                                                        speciesNameField.setText(
                                                                                        newValue.getName());
                                                                }
                                                        }
                                                });
        }

        private void addSpecies() {

                String name = speciesNameField
                                .getText()
                                .trim();

                if (name.isEmpty()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "El nombre de la especie es obligatorio.");

                        return;
                }

                Species newSpecies = new Species();

                newSpecies.setName(
                                name);

                speciesDAO.add(
                                newSpecies);

                loadSpecies();

                clearSpeciesFields();

                showMessage(
                                Alert.AlertType.INFORMATION,
                                "Operación exitosa",
                                "La especie fue agregada correctamente.");

                showSpecies();
        }

        private void updateSpecies() {

                Species selected = speciesTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selected == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin selección",
                                        "Seleccione primero una especie.");

                        return;
                }

                String name = speciesNameField
                                .getText()
                                .trim();

                if (name.isEmpty()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Validación",
                                        "El nombre de la especie es obligatorio.");

                        return;
                }

                selected.setName(
                                name);

                speciesDAO.update(
                                selected);

                loadSpecies();

                clearSpeciesFields();

                showMessage(
                                Alert.AlertType.INFORMATION,
                                "Operación exitosa",
                                "La especie fue actualizada correctamente.");

                showSpecies();
        }

        private void deleteSpecies() {

                Species selected = speciesTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selected == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin selección",
                                        "Seleccione primero una especie.");

                        return;
                }

                if (!confirmAction(
                                "Eliminar especie",
                                "¿Está seguro de que desea eliminar la especie \""
                                                + selected.getName()
                                                + "\"?")) {

                        return;
                }

                speciesDAO.delete(
                                selected.getSpeciesId());

                loadSpecies();

                clearSpeciesFields();

                showMessage(
                                Alert.AlertType.INFORMATION,
                                "Operación exitosa",
                                "La especie fue eliminada correctamente.");

                showSpecies();
        }

        private void clearSpeciesFields() {

                selectedSpecies = null;

                if (speciesNameField != null) {

                        speciesNameField.clear();
                }

                if (speciesTable != null) {

                        speciesTable
                                        .getSelectionModel()
                                        .clearSelection();
                }
        }

        // =========================================================
        // RECINTOS
        // =========================================================

        private void showEnclosures() {

                setActiveMainNavigation(
                                enclosuresButton);

                loadEnclosures();

                VBox root = new VBox(18);

                HBox header = new HBox();

                VBox headerText = new VBox(
                                4);

                Label title = createSectionTitle(
                                "Gestión de recintos");

                Label description = new Label(
                                "Administre los espacios disponibles para alojar animales.");

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                headerText.getChildren().addAll(
                                title,
                                description);

                Button add = primaryButton(
                                "+ NUEVO RECINTO");

                add.setOnAction(
                                event -> openEnclosureForm());

                header.getChildren().addAll(
                                headerText,
                                add);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                HBox.setHgrow(
                                headerText,
                                Priority.ALWAYS);

                VBox tableCard = createCard();

                HBox toolbar = new HBox(
                                10);

                enclosureSearchField = new TextField();

                enclosureSearchField.setPromptText(
                                "Buscar por nombre o sector...");

                enclosureSearchField.setPrefWidth(
                                350);

                Button refresh = secondaryButton(
                                "ACTUALIZAR");

                refresh.setOnAction(
                                event -> {
                                        loadEnclosures();
                                        filterEnclosures(
                                                        enclosureSearchField
                                                                        .getText());
                                });

                enclosureSearchField
                                .textProperty()
                                .addListener(
                                                (observable, oldValue, newValue) ->
                                                                filterEnclosures(
                                                                                newValue));

                toolbar.getChildren().addAll(
                                enclosureSearchField,
                                refresh);

                enclosureTable = new TableView<>();

                createEnclosureTable();

                HBox actions = new HBox(
                                10);

                actions.setAlignment(
                                Pos.CENTER_RIGHT);

                Button edit = secondaryButton(
                                "EDITAR");

                Button delete = deleteButton(
                                "ELIMINAR");

                edit.setOnAction(
                                event -> {

                                        Enclosure selected =
                                                        enclosureTable
                                                                        .getSelectionModel()
                                                                        .getSelectedItem();

                                        if (selected == null) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Sin selección",
                                                                "Seleccione un recinto primero.");

                                                return;
                                        }

                                        openEnclosureForm(
                                                        selected);
                                });

                delete.setOnAction(
                                event -> deleteEnclosure());

                actions.getChildren().addAll(
                                edit,
                                delete);

                tableCard.getChildren().addAll(
                                toolbar,
                                enclosureTable,
                                actions);

                VBox.setVgrow(
                                enclosureTable,
                                Priority.ALWAYS);

                root.getChildren().addAll(
                                header,
                                tableCard);

                contentContainer
                                .getChildren()
                                .setAll(root);
        }

        private void createEnclosureTable() {

                enclosureTable.getColumns().clear();

                enclosureTable.setItems(
                                enclosures);

                enclosureTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<Enclosure, Number> idColumn =
                                new TableColumn<>(
                                                "ID");

                idColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getEnclosureId()));

                TableColumn<Enclosure, String> nameColumn =
                                new TableColumn<>(
                                                "Nombre");

                nameColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getName()));

                TableColumn<Enclosure, String> sectorColumn =
                                new TableColumn<>(
                                                "Sector");

                sectorColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                data.getValue()
                                                                .getSector()));

                TableColumn<Enclosure, Number> capacityColumn =
                                new TableColumn<>(
                                                "Capacidad");

                capacityColumn.setCellValueFactory(
                                data -> new SimpleIntegerProperty(
                                                data.getValue()
                                                                .getCapacity() == null
                                                                                ? 0
                                                                                : data.getValue()
                                                                                                .getCapacity()));

                TableColumn<Enclosure, String> statusColumn =
                                new TableColumn<>(
                                                "Estado");

                statusColumn.setCellValueFactory(
                                data -> new SimpleStringProperty(
                                                translateStatus(
                                                                data.getValue()
                                                                                .getStatus())));

                enclosureTable.getColumns().addAll(
                                idColumn,
                                nameColumn,
                                sectorColumn,
                                capacityColumn,
                                statusColumn);

                enclosureTable.setPrefHeight(
                                430);
        }

        private void filterEnclosures(
                        String text) {

                if (enclosureTable == null) {
                        return;
                }

                String search = text == null
                                ? ""
                                : text.trim()
                                                .toLowerCase();

                if (search.isEmpty()) {

                        enclosureTable.setItems(
                                        enclosures);

                        return;
                }

                ObservableList<Enclosure> filtered =
                                FXCollections.observableArrayList();

                for (Enclosure enclosure : enclosures) {

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
                                                enclosure);
                        }
                }

                enclosureTable.setItems(
                                filtered);
        }

        // =========================================================
        // FORMULARIO RECINTO
        // =========================================================

        private void openEnclosureForm() {

                openEnclosureForm(
                                null);
        }

        private void openEnclosureForm(
                        Enclosure enclosure) {

                boolean editing = enclosure != null;

                VBox content = new VBox(18);

                content.setPadding(
                                new Insets(25));

                Label title = createSectionTitle(
                                editing
                                                ? "Editar recinto"
                                                : "Nuevo recinto");

                GridPane form = new GridPane();

                form.setHgap(15);

                form.setVgap(15);

                TextField name = new TextField(
                                editing
                                                ? safeValue(
                                                                enclosure.getName())
                                                : "");

                TextField sector = new TextField(
                                editing
                                                ? safeValue(
                                                                enclosure.getSector())
                                                : "");

                TextField capacity = new TextField(
                                editing
                                                && enclosure.getCapacity() != null
                                                                ? String.valueOf(
                                                                                enclosure.getCapacity())
                                                                : "");

                ComboBox<String> status = new ComboBox<>();

                status.getItems().addAll(
                                "Active",
                                "Inactive");

                status.setValue(
                                editing
                                                ? enclosure.getStatus()
                                                : "Active");

                TextArea observations = new TextArea(
                                editing
                                                ? safeValue(
                                                                enclosure.getObservations())
                                                : "");

                observations.setWrapText(
                                true);

                observations.setPrefRowCount(
                                4);

                form.add(
                                label("Nombre"),
                                0,
                                0);

                form.add(
                                name,
                                1,
                                0);

                form.add(
                                label("Sector"),
                                0,
                                1);

                form.add(
                                sector,
                                1,
                                1);

                form.add(
                                label("Capacidad"),
                                0,
                                2);

                form.add(
                                capacity,
                                1,
                                2);

                form.add(
                                label("Estado"),
                                0,
                                3);

                form.add(
                                status,
                                1,
                                3);

                form.add(
                                label("Observaciones"),
                                0,
                                4);

                form.add(
                                observations,
                                1,
                                4);

                GridPane.setHgrow(
                                name,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                sector,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                capacity,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                status,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                observations,
                                Priority.ALWAYS);

                Button cancel = secondaryButton(
                                "CANCELAR");

                Button save = primaryButton(
                                editing
                                                ? "GUARDAR CAMBIOS"
                                                : "CREAR RECINTO");

                VBox modal = createCard();

                modal.getChildren().addAll(
                                title,
                                form);

                HBox actions = new HBox(
                                10,
                                cancel,
                                save);

                actions.setAlignment(
                                Pos.CENTER_RIGHT);

                modal.getChildren().add(
                                actions);

                Stage stage = createModalStage(
                                editing
                                                ? "Editar recinto"
                                                : "Nuevo recinto",
                                modal);

                cancel.setOnAction(
                                event -> stage.close());

                save.setOnAction(
                                event -> {

                                        try {

                                                saveEnclosure(
                                                                enclosure,
                                                                name,
                                                                sector,
                                                                capacity,
                                                                status,
                                                                observations);

                                                stage.close();

                                                loadEnclosures();

                                                showEnclosures();

                                        } catch (Exception exception) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                getExceptionMessage(
                                                                                exception));
                                        }
                                });

                stage.showAndWait();
        }

        private void saveEnclosure(
                        Enclosure enclosure,
                        TextField nameField,
                        TextField sectorField,
                        TextField capacityField,
                        ComboBox<String> statusComboBox,
                        TextArea observationsField) {

                String name = nameField.getText()
                                .trim();

                String sector = sectorField.getText()
                                .trim();

                if (name.isEmpty()
                                || sector.isEmpty()) {

                        throw new IllegalArgumentException(
                                        "El nombre y el sector son obligatorios.");
                }

                Integer capacity = null;

                String capacityText = capacityField.getText()
                                .trim();

                if (!capacityText.isEmpty()) {

                        try {

                                capacity = Integer.parseInt(
                                                capacityText);

                        } catch (NumberFormatException exception) {

                                throw new IllegalArgumentException(
                                                "La capacidad debe ser un número válido.");
                        }

                        if (capacity < 0) {

                                throw new IllegalArgumentException(
                                                "La capacidad no puede ser negativa.");
                        }
                }

                if (statusComboBox.getValue() == null) {

                        throw new IllegalArgumentException(
                                        "Seleccione un estado.");
                }

                if (enclosure == null) {

                        Enclosure newEnclosure =
                                        new Enclosure();

                        newEnclosure.setName(
                                        name);

                        newEnclosure.setSector(
                                        sector);

                        newEnclosure.setCapacity(
                                        capacity);

                        newEnclosure.setStatus(
                                        statusComboBox.getValue());

                        newEnclosure.setObservations(
                                        observationsField
                                                        .getText()
                                                        .trim());

                        enclosureDAO.add(
                                        newEnclosure);

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Operación exitosa",
                                        "El recinto fue creado correctamente.");

                } else {

                        enclosure.setName(
                                        name);

                        enclosure.setSector(
                                        sector);

                        enclosure.setCapacity(
                                        capacity);

                        enclosure.setStatus(
                                        statusComboBox.getValue());

                        enclosure.setObservations(
                                        observationsField
                                                        .getText()
                                                        .trim());

                        enclosureDAO.update(
                                        enclosure);

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Operación exitosa",
                                        "El recinto fue actualizado correctamente.");
                }
        }

        // =========================================================
        // ELIMINAR RECINTO
        // =========================================================

        private void deleteEnclosure() {

                Enclosure selected = enclosureTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (selected == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin selección",
                                        "Seleccione un recinto primero.");

                        return;
                }

                if (!confirmAction(
                                "Eliminar recinto",
                                "¿Está seguro de que desea eliminar el recinto \""
                                                + selected.getName()
                                                + "\"?")) {

                        return;
                }

                enclosureDAO.delete(
                                selected.getEnclosureId());

                loadEnclosures();

                showMessage(
                                Alert.AlertType.INFORMATION,
                                "Operación exitosa",
                                "El recinto fue eliminado correctamente.");

                showEnclosures();
        }

        // =========================================================
        // DATA
        // =========================================================

        private void loadAllData() {

                loadSpecies();

                loadEnclosures();

                loadInventory();
        }

        private void loadInventory() {

                inventory.setAll(
                                holdingDAO.listInventorySummary());
        }

        private void loadSpecies() {

                species.setAll(
                                speciesDAO.list());

                if (speciesComboBox != null) {

                        speciesComboBox.setItems(
                                        species);
                }
        }

        private void loadEnclosures() {

                try {

                        enclosures.setAll(
                                        enclosureDAO.list());

                } catch (Exception exception) {

                        showMessage(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "No se pudieron cargar los recintos.");
                }
        }

        // =========================================================
        // UTILIDADES
        // =========================================================

        private Button createLargeActionButton(
                        String icon,
                        String title,
                        String description) {

                Button button = new Button();

                VBox content = new VBox(
                                5);

                content.setAlignment(
                                Pos.CENTER_LEFT);

                Label iconLabel = new Label(
                                icon);

                iconLabel.setStyle(
                                "-fx-font-size: 28px;");

                Label titleLabel = new Label(
                                title);

                titleLabel.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                Label descriptionLabel = new Label(
                                description);

                descriptionLabel.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: #727A76;");

                content.getChildren().addAll(
                                iconLabel,
                                titleLabel,
                                descriptionLabel);

                button.setGraphic(
                                content);

                button.setPrefWidth(
                                230);

                button.setPrefHeight(
                                125);

                button.setStyle(
                                """
                                                -fx-background-color: #F4F1E8;
                                                -fx-border-color: #C9D2CB;
                                                -fx-border-radius: 12;
                                                -fx-background-radius: 12;
                                                -fx-padding: 15;
                                                """);

                return button;
        }

        private VBox createCard() {

                VBox card = new VBox(
                                18);

                card.setPadding(
                                new Insets(22));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        private Label createSectionTitle(
                        String text) {

                Label label = new Label(
                                text);

                label.setStyle(
                                "-fx-font-size: 21px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #254D3D;");

                return label;
        }

        private Label label(
                        String text) {

                Label label = new Label(
                                text);

                label.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #405047;");

                return label;
        }

        private void configureControl(
                        javafx.scene.control.Control control) {

                control.setMaxWidth(
                                Double.MAX_VALUE);

                control.setPrefHeight(
                                42);
        }

        private Button primaryButton(
                        String text) {

                Button button = new Button(
                                text);

                button.setPrefHeight(
                                42);

                button.setStyle(
                                "-fx-background-color: #254D3D;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 10 20;");

                return button;
        }

        private Button secondaryButton(
                        String text) {

                Button button = new Button(
                                text);

                button.setPrefHeight(
                                42);

                button.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 10 20;");

                return button;
        }

        private Button deleteButton(
                        String text) {

                Button button = new Button(
                                text);

                button.setPrefHeight(
                                42);

                button.setStyle(
                                "-fx-background-color: #A34A4A;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 10 20;");

                return button;
        }

        // =========================================================
        // ANIMALES MUERTOS
        // =========================================================

        private boolean isAnimalDead(
                        Animal animal) {

                if (animal == null) {
                        return false;
                }

                List<Movement> movements =
                                movementDAO.list();

                for (Movement movement : movements) {

                        if (movement.getAnimalId() == animal.getAnimalId()
                                        && AnimalInventoryService.DEATH
                                                        .equals(
                                                                        movement.getMovementType())) {

                                return true;
                        }
                }

                return false;
        }

        private String safeValue(
                        String value) {

                return value == null
                                ? ""
                                : value;
        }

        // =========================================================
        // ORIGEN / ESTADO
        // =========================================================

        private void configureOriginConverter() {

                originComboBox.setConverter(
                                new StringConverter<>() {

                                        @Override
                                        public String toString(
                                                        String value) {

                                                return translateOrigin(
                                                                value);
                                        }

                                        @Override
                                        public String fromString(
                                                        String value) {

                                                return value;
                                        }
                                });
        }

        private void configureStatusConverter() {

                statusComboBox.setConverter(
                                new StringConverter<>() {

                                        @Override
                                        public String toString(
                                                        String value) {

                                                return translateStatus(
                                                                value);
                                        }

                                        @Override
                                        public String fromString(
                                                        String value) {

                                                return value;
                                        }
                                });
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

        // =========================================================
        // MOVIMIENTOS
        // =========================================================

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

        private String formatHolding(
                        AnimalHolding holding) {

                String location =
                                translateLocation(
                                                holding.getLocationType());

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
                                        "Debe indicar una cantidad.");
                }

                int quantity = Integer.parseInt(
                                value);

                if (quantity <= 0) {

                        throw new IllegalArgumentException(
                                        "La cantidad debe ser mayor que cero.");
                }

                return quantity;
        }

        // =========================================================
        // CLEAR
        // =========================================================

        private void clearAnimalFields() {

                selectedAnimal = null;

                if (speciesComboBox != null) {

                        speciesComboBox.setValue(
                                        null);
                }

                if (commonNameField != null) {

                        commonNameField.clear();
                }

                if (scientificNameField != null) {

                        scientificNameField.clear();
                }

                if (originComboBox != null) {

                        originComboBox.setValue(
                                        "Admission");
                }

                if (statusComboBox != null) {

                        statusComboBox.setValue(
                                        "Active");
                }

                if (quantityField != null) {

                        quantityField.clear();
                }
        }

        // =========================================================
        // MODAL
        // =========================================================

        private Stage createModalStage(
                        String title,
                        Parent content) {

                Stage stage = new Stage();

                stage.setTitle(
                                title);

                stage.initModality(
                                Modality.APPLICATION_MODAL);

                stage.setMinWidth(
                                650);

                stage.setMinHeight(
                                500);

                ScrollPane scrollPane = new ScrollPane(
                                content);

                scrollPane.setFitToWidth(
                                true);

                stage.setScene(
                                new Scene(
                                                scrollPane,
                                                700,
                                                600));

                return stage;
        }

        // =========================================================
        // CONFIRMACIÓN
        // =========================================================

        private boolean confirmAction(
                        String title,
                        String message) {

                Alert alert = new Alert(
                                Alert.AlertType.CONFIRMATION);

                alert.setTitle(
                                title);

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                return alert.showAndWait()
                                .filter(
                                                button -> button == ButtonType.OK)
                                .isPresent();
        }

        // =========================================================
        // HELPERS
        // =========================================================

        private String getExceptionMessage(
                        Exception exception) {

                if (exception.getMessage() == null
                                || exception.getMessage().isBlank()) {

                        return "Ocurrió un error inesperado.";
                }

                return exception.getMessage();
        }

        private void showMessage(
                        Alert.AlertType type,
                        String title,
                        String message) {

                Alert alert = new Alert(
                                type);

                alert.setTitle(
                                title);

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}