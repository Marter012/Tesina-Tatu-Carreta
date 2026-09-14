package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.EntryDetailDAO;
import com.tesina_tatu_carreta.dao.EnclosureDAO;
import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.Entry;
import com.tesina_tatu_carreta.model.EntryDetail;
import com.tesina_tatu_carreta.model.Enclosure;
import com.tesina_tatu_carreta.model.Species;
import com.tesina_tatu_carreta.service.AnimalInventoryService;
import com.tesina_tatu_carreta.service.EntryService;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewEntry {

        private final EntryDAO entryDAO = new EntryDAO();

        private final EntryDetailDAO detailDAO = new EntryDetailDAO();

        private final AnimalDAO animalDAO = new AnimalDAO();

        private final SpeciesDAO speciesDAO = new SpeciesDAO();

        private final EnclosureDAO enclosureDAO = new EnclosureDAO();

        private final EntryService entryService = new EntryService();

        private final ObservableList<EntryDetail> pendingDetails = FXCollections.observableArrayList();

        private final TableView<EntryDetail> detailsTable = new TableView<>();

        private final TableView<Entry> entriesTable = new TableView<>();

        private final ObservableList<Entry> allEntries = FXCollections.observableArrayList();

        private final ObservableList<Animal> animals = FXCollections.observableArrayList();

        private TextField searchField;

        private TextField recordNumberField;
        private DatePicker entryDatePicker;
        private TextField sourceOrganizationField;
        private TextField deliveryResponsibleField;
        private TextField originField;
        private TextField entryReasonField;
        private TextArea documentationField;
        private TextArea entryObservationsField;

        private ComboBox<Species> speciesComboBox;
        private ComboBox<Enclosure> enclosureComboBox;
        private ObservableList<Enclosure> activeEnclosures = FXCollections.observableArrayList();
        private ComboBox<Animal> animalComboBox;
        private TextField quantityField;
        private ComboBox<String> sexComboBox;
        private TextField ageField;
        private TextField weightField;
        private ComboBox<String> destinationComboBox;
        private ComboBox<String> entryStatusComboBox;
        private TextArea detailObservationsField;

        private Label detailSummaryLabel;

        private Entry selectedEntry;

        /*
         * =========================================================
         * OVERLAY
         * =========================================================
         */

        private StackPane mainRoot;

        private VBox newEntryOverlay;
        private StackPane entryDetailOverlay;

        private VBox stepAnimals;
        private VBox stepEntry;

        private Label stepIndicator;
        private Label stepOneLabel;
        private Label stepTwoLabel;

        /*
         * =========================================================
         * GET VIEW
         * =========================================================
         */

        public Parent getView() {
                return createView();
        }

        // =========================================================
        // MAIN VIEW
        // =========================================================

        public Parent createView() {

                mainRoot = new StackPane();

                VBox content = new VBox(20);

                content.setPadding(
                                new Insets(30));

                content.setStyle(
                                "-fx-background-color: #F4F2EA;");

                Label breadcrumb = new Label(
                                "Inicio / Ingresos");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #777777;");

                Label title = new Label(
                                "Ingresos");

                title.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                Label description = new Label(
                                "Registre y consulte los ingresos de animales a la reserva.");

                description.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #727A76;");

                VBox header = new VBox(
                                5,
                                breadcrumb,
                                title,
                                description);

                searchField = new TextField();

                searchField.setPromptText(
                                "Buscar por registro, organización, origen o motivo...");

                searchField.setPrefHeight(
                                38);

                searchField.textProperty()
                                .addListener(
                                                (observable,
                                                                oldValue,
                                                                newValue) -> filterEntries(
                                                                                newValue));

                Button newEntryButton = primaryButton(
                                "+ NUEVO INGRESO");

                newEntryButton.setPrefHeight(
                                38);

                newEntryButton.setOnAction(
                                event -> openNewEntryModal());

                HBox actionBar = new HBox(
                                12,
                                searchField,
                                newEntryButton);

                actionBar.setAlignment(
                                Pos.CENTER_LEFT);

                HBox.setHgrow(
                                searchField,
                                Priority.ALWAYS);

                VBox historyCard = createEntriesTable();

                VBox.setVgrow(
                                historyCard,
                                Priority.ALWAYS);

                content.getChildren().addAll(
                                header,
                                actionBar,
                                historyCard);

                mainRoot.getChildren().add(
                                content);

                return mainRoot;
        }

        // =========================================================
        // ENTRIES TABLE
        // =========================================================

        private VBox createEntriesTable() {

                createEntriesColumns();

                loadEntries();

                entriesTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                entriesTable.setPrefHeight(
                                500);

                entriesTable.setMaxHeight(
                                Double.MAX_VALUE);

                entriesTable.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable,
                                                                oldValue,
                                                                newValue) -> selectedEntry = newValue);

                // Doble click: consultar el ingreso en un modal interno.
                entriesTable.setOnMouseClicked(
                                event -> {
                                        if (event.getClickCount() == 2 &&
                                                        !entriesTable.getSelectionModel().isEmpty()) {

                                                Entry entry = entriesTable
                                                                .getSelectionModel()
                                                                .getSelectedItem();

                                                if (entry != null) {
                                                        openEntryDetailModal(entry);
                                                }
                                        }
                                });

                Button editButton = secondaryButton(
                                "EDITAR INGRESO");

                Button deleteButton = deleteButton(
                                "ELIMINAR INGRESO");

                editButton.setOnAction(
                                event -> editSelectedEntry());

                deleteButton.setOnAction(
                                event -> deleteSelectedEntry());

                HBox actions = new HBox(
                                10,
                                editButton,
                                deleteButton);

                actions.setAlignment(
                                Pos.CENTER_RIGHT);

                Label tableTitle = title(
                                "Historial de ingresos");

                Label tableDescription = new Label(
                                "Seleccione un ingreso para consultar o modificar su información.");

                tableDescription.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 12px;");

                VBox header = new VBox(
                                4,
                                tableTitle,
                                tableDescription);

                HBox top = new HBox(
                                15,
                                header,
                                actions);

                top.setAlignment(
                                Pos.CENTER_LEFT);

                HBox.setHgrow(
                                header,
                                Priority.ALWAYS);

                VBox card = new VBox(
                                15,
                                top,
                                entriesTable);

                card.setPadding(
                                new Insets(25));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                VBox.setVgrow(
                                entriesTable,
                                Priority.ALWAYS);

                return card;
        }

        // =========================================================
        // NEW ENTRY OVERLAY
        // =========================================================

        private void openNewEntryModal() {

                selectedEntry = null;

                pendingDetails.clear();

                if (mainRoot == null) {
                        return;
                }

                /*
                 * =========================================================
                 * FULL SCREEN OVERLAY
                 * =========================================================
                 */

                newEntryOverlay = new VBox();

                newEntryOverlay.setMaxSize(
                                Double.MAX_VALUE,
                                Double.MAX_VALUE);

                newEntryOverlay.setPrefSize(
                                Double.MAX_VALUE,
                                Double.MAX_VALUE);

                newEntryOverlay.setAlignment(
                                Pos.CENTER);

                newEntryOverlay.setStyle(
                                "-fx-background-color: rgba(0,0,0,0.75);");

                /*
                 * =========================================================
                 * MODAL CARD
                 * =========================================================
                 */

                VBox modalCard = new VBox(0);

                modalCard.setMaxWidth(
                                1100);

                modalCard.setMaxHeight(
                                760);

                modalCard.setPrefWidth(
                                1050);

                modalCard.setStyle(
                                "-fx-background-color: #F4F2EA;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-border-radius: 18;" +
                                                "-fx-border-color: #D8D4C9;");

                /*
                 * =========================================================
                 * HEADER - FIXED
                 * =========================================================
                 */

                HBox modalHeader = new HBox();

                modalHeader.setPadding(
                                new Insets(22, 28, 22, 28));

                modalHeader.setAlignment(
                                Pos.CENTER_LEFT);

                Label modalTitle = new Label(
                                "Nuevo ingreso");

                modalTitle.setStyle(
                                "-fx-font-size: 25px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                Label subtitle = new Label(
                                "Registro de animales y aceptación de entrega");

                subtitle.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #727A76;");

                VBox titleContainer = new VBox(
                                4,
                                modalTitle,
                                subtitle);

                Button closeButton = new Button(
                                "✕");

                closeButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #555555;" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;");

                closeButton.setOnAction(
                                event -> closeNewEntryOverlay());

                HBox.setHgrow(
                                titleContainer,
                                Priority.ALWAYS);

                modalHeader.getChildren().addAll(
                                titleContainer,
                                closeButton);

                /*
                 * =========================================================
                 * STEPS - FIXED
                 * =========================================================
                 */

                HBox steps = createStepNavigation();

                /*
                 * =========================================================
                 * CONTENT CONTAINER
                 * =========================================================
                 *
                 * Only this section will scroll.
                 *
                 */

                StackPane stepsContainer = new StackPane();

                stepsContainer.setPadding(
                                new Insets(0, 28, 20, 28));

                stepAnimals = createStepAnimals();

                stepEntry = createStepEntry();

                stepEntry.setVisible(
                                false);

                stepEntry.setManaged(
                                false);

                stepsContainer.getChildren().addAll(
                                stepAnimals,
                                stepEntry);

                /*
                 * =========================================================
                 * SCROLL PANE
                 * =========================================================
                 */

                ScrollPane contentScroll = new ScrollPane(
                                stepsContainer);

                contentScroll.setFitToWidth(
                                true);

                contentScroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                /*
                 * Hide scrollbar but keep mouse wheel scrolling.
                 */

                contentScroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                contentScroll.setPannable(
                                true);

                contentScroll.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: transparent;" +
                                                "-fx-border-color: transparent;" +
                                                "-fx-padding: 0;");

                VBox.setVgrow(
                                contentScroll,
                                Priority.ALWAYS);

                /*
                 * =========================================================
                 * MODAL STRUCTURE
                 * =========================================================
                 *
                 * Header -> fixed
                 * Steps -> fixed
                 * Content -> scrollable
                 *
                 */

                modalCard.getChildren().addAll(
                                modalHeader,
                                steps,
                                contentScroll);

                /*
                 * =========================================================
                 * ADD MODAL TO FULL SCREEN OVERLAY
                 * =========================================================
                 */

                newEntryOverlay.getChildren().add(
                                modalCard);

                /*
                 * =========================================================
                 * ADD OVERLAY ABOVE ENTIRE APPLICATION
                 * =========================================================
                 */

                mainRoot.getChildren().add(
                                newEntryOverlay);

                /*
                 * Make sure the overlay is the top-most element.
                 */

                newEntryOverlay.toFront();
        }

        // =========================================================
        // STEP NAVIGATION
        // =========================================================

        private HBox createStepNavigation() {

                stepOneLabel = new Label(
                                "1. Registrar animales");

                stepTwoLabel = new Label(
                                "2. Aceptar entrega");

                stepIndicator = new Label(
                                "Paso 1 de 2");

                stepIndicator.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #727A76;");

                styleActiveStep();

                HBox steps = new HBox(
                                15,
                                stepOneLabel,
                                createStepSeparator(),
                                stepTwoLabel,
                                stepIndicator);

                steps.setPadding(
                                new Insets(0, 28, 20, 28));

                steps.setAlignment(
                                Pos.CENTER_LEFT);

                return steps;
        }

        private Label createStepSeparator() {

                Label separator = new Label(
                                "→");

                separator.setStyle(
                                "-fx-font-size: 16px;" +
                                                "-fx-text-fill: #B8A47E;" +
                                                "-fx-font-weight: bold;");

                return separator;
        }

        private void styleActiveStep() {

                stepOneLabel.setStyle(
                                "-fx-background-color: #23452C;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 9 14 9 14;");

                stepTwoLabel.setStyle(
                                "-fx-background-color: #E5E1D7;" +
                                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 9 14 9 14;");
        }

        private void styleSecondStep() {

                stepOneLabel.setStyle(
                                "-fx-background-color: #B8A47E;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 9 14 9 14;");

                stepTwoLabel.setStyle(
                                "-fx-background-color: #23452C;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 9 14 9 14;");
        }

        // =========================================================
        // STEP 1 - ANIMALS
        // =========================================================

        private VBox createStepAnimals() {

                initializeDetailControls();

                GridPane grid = createDetailGrid();

                Button clearDetail = secondaryButton(
                                "LIMPIAR");

                Button addDetail = primaryButton(
                                "+ AGREGAR ANIMAL");

                addDetail.setOnAction(
                                event -> addDetail());

                clearDetail.setOnAction(
                                event -> clearDetail());

                HBox detailButtons = new HBox(
                                10,
                                clearDetail,
                                addDetail);

                detailButtons.setAlignment(
                                Pos.CENTER_RIGHT);

                configureDetailsTable();

                detailSummaryLabel = new Label();

                updateDetailSummary();

                detailSummaryLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                Label description = new Label(
                                "Agregue los animales que forman parte de la entrega antes de aceptar el ingreso.");

                description.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 12px;");

                VBox card = new VBox(
                                18,
                                title(
                                                "Registrar animales"),
                                description,
                                grid,
                                detailButtons,
                                detailSummaryLabel,
                                title(
                                                "Animales agregados"),
                                detailsTable);

                card.setPadding(
                                new Insets(25));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                VBox.setVgrow(
                                detailsTable,
                                Priority.ALWAYS);

                Button nextButton = primaryButton(
                                "SIGUIENTE →");

                nextButton.setOnAction(
                                event -> showEntryStep());

                HBox navigation = new HBox(
                                nextButton);

                navigation.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox container = new VBox(
                                15,
                                card,
                                navigation);

                VBox.setVgrow(
                                card,
                                Priority.ALWAYS);

                return container;
        }

        // =========================================================
        // STEP 2 - ENTRY
        // =========================================================

        private VBox createStepEntry() {

                VBox entryForm = createEntryFormForOverlay();

                Label animalsTitle = title(
                                "Animales de la entrega");

                Label animalsDescription = new Label(
                                "Estos son los animales registrados en el paso anterior y serán asociados al ingreso.");

                animalsDescription.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 12px;");

                configureDetailsTable();

                VBox animalsCard = new VBox(
                                12,
                                animalsTitle,
                                animalsDescription,
                                detailsTable);

                animalsCard.setPadding(
                                new Insets(25));

                animalsCard.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                Button backButton = secondaryButton(
                                "← VOLVER");

                Button saveButton = primaryButton(
                                "GUARDAR INGRESO");

                backButton.setOnAction(
                                event -> showAnimalsStep());

                saveButton.setOnAction(
                                event -> saveEntry());

                HBox navigation = new HBox(
                                10,
                                backButton,
                                saveButton);

                navigation.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox container = new VBox(
                                15,
                                entryForm,
                                animalsCard,
                                navigation);

                return container;
        }

        // =========================================================
        // SHOW ANIMALS STEP
        // =========================================================

        private void showAnimalsStep() {

                if (stepAnimals == null ||
                                stepEntry == null) {
                        return;
                }

                stepEntry.setVisible(false);
                stepEntry.setManaged(false);

                stepAnimals.setVisible(true);
                stepAnimals.setManaged(true);

                stepIndicator.setText(
                                "Paso 1 de 2");

                styleActiveStep();
        }

        // =========================================================
        // SHOW ENTRY STEP
        // =========================================================

        private void showEntryStep() {

                if (pendingDetails.isEmpty()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Animales requeridos",
                                        "Debe agregar al menos un animal antes de continuar.");

                        return;
                }

                stepAnimals.setVisible(false);
                stepAnimals.setManaged(false);

                stepEntry.setVisible(true);
                stepEntry.setManaged(true);

                stepIndicator.setText(
                                "Paso 2 de 2");

                styleSecondStep();
        }

        // =========================================================
        // CLOSE NEW ENTRY OVERLAY
        // =========================================================

        private void closeNewEntryOverlay() {

                if (mainRoot == null ||
                                newEntryOverlay == null) {
                        return;
                }

                mainRoot.getChildren().remove(
                                newEntryOverlay);

                newEntryOverlay = null;

                pendingDetails.clear();

                selectedEntry = null;
        }

        // =========================================================
        // ENTRY FORM FOR OVERLAY
        // =========================================================

        private VBox createEntryFormForOverlay() {

                recordNumberField = new TextField();

                recordNumberField.setPromptText(
                                "Ej.: ACTA-2026-001");

                entryDatePicker = new DatePicker(
                                LocalDate.now());

                sourceOrganizationField = new TextField();

                sourceOrganizationField.setPromptText(
                                "Ej.: Secretaría de Ambiente");

                deliveryResponsibleField = new TextField();

                deliveryResponsibleField.setPromptText(
                                "Nombre del responsable");

                originField = new TextField();

                originField.setPromptText(
                                "Lugar o institución de procedencia");

                entryReasonField = new TextField();

                entryReasonField.setPromptText(
                                "Motivo por el cual ingresa el animal");

                documentationField = new TextArea();

                entryObservationsField = new TextArea();

                documentationField.setPromptText(
                                "Documentos, resoluciones, certificados, etc.");

                entryObservationsField.setPromptText(
                                "Información adicional del ingreso");

                documentationField.setPrefRowCount(
                                3);

                entryObservationsField.setPrefRowCount(
                                3);

                GridPane grid = new GridPane();

                grid.setHgap(20);
                grid.setVgap(15);

                addField(
                                grid,
                                "Número de registro",
                                recordNumberField,
                                0,
                                0);

                addField(
                                grid,
                                "Fecha de ingreso",
                                entryDatePicker,
                                1,
                                0);

                addField(
                                grid,
                                "Organización de origen",
                                sourceOrganizationField,
                                0,
                                2);

                addField(
                                grid,
                                "Responsable de la entrega",
                                deliveryResponsibleField,
                                1,
                                2);

                addField(
                                grid,
                                "Origen",
                                originField,
                                0,
                                4);

                addField(
                                grid,
                                "Motivo de ingreso",
                                entryReasonField,
                                1,
                                4);

                addField(
                                grid,
                                "Documentación",
                                documentationField,
                                0,
                                6);

                addField(
                                grid,
                                "Observaciones",
                                entryObservationsField,
                                1,
                                6);

                VBox card = new VBox(
                                18,
                                title(
                                                "Aceptar entrega"),
                                new Label(
                                                "Complete los datos generales del acta de ingreso."),
                                grid);

                ((Label) card.getChildren().get(1))
                                .setStyle(
                                                "-fx-text-fill: #727A76;" +
                                                                "-fx-font-size: 12px;");

                card.setPadding(
                                new Insets(25));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        // =========================================================
        // EDIT ENTRY MODAL
        // =========================================================

        private void openEditEntryModal(
                        Entry entry) {

                if (entry == null) {
                        return;
                }

                selectedEntry = entry;

                pendingDetails.clear();

                Stage modal = new Stage();

                modal.initModality(
                                Modality.APPLICATION_MODAL);

                modal.setTitle(
                                "Editar ingreso");

                modal.setMinWidth(
                                1000);

                modal.setMinHeight(
                                750);

                VBox content = new VBox(20);

                content.setPadding(
                                new Insets(30));

                content.setStyle(
                                "-fx-background-color: #F4F2EA;");

                Label breadcrumb = new Label(
                                "Ingresos / Editar ingreso");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #777777;");

                Label modalTitle = new Label(
                                "Editar ingreso");

                modalTitle.setStyle(
                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                Label description = new Label(
                                "Modifique los datos generales del ingreso. Los animales asociados se conservan como parte del historial.");

                description.setWrapText(
                                true);

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                content.getChildren().addAll(
                                breadcrumb,
                                modalTitle,
                                description,
                                createEntryForm(true),
                                createDetailFormForEdit());

                loadEntry(
                                entry);

                ScrollPane scrollPane = new ScrollPane(
                                content);

                scrollPane.setFitToWidth(
                                true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                BorderPane container = new BorderPane();

                container.setCenter(
                                scrollPane);

                Scene scene = new Scene(
                                container,
                                1100,
                                800);

                modal.setScene(
                                scene);

                modal.showAndWait();

                loadEntries();
        }

        // =========================================================
        // ENTRY FORM
        // =========================================================

        private VBox createEntryForm(
                        boolean editing) {

                recordNumberField = new TextField();

                recordNumberField.setPromptText(
                                "Ej.: ACTA-2026-001");

                entryDatePicker = new DatePicker(
                                LocalDate.now());

                sourceOrganizationField = new TextField();

                sourceOrganizationField.setPromptText(
                                "Ej.: Secretaría de Ambiente");

                deliveryResponsibleField = new TextField();

                deliveryResponsibleField.setPromptText(
                                "Nombre del responsable");

                originField = new TextField();

                originField.setPromptText(
                                "Lugar o institución de procedencia");

                entryReasonField = new TextField();

                entryReasonField.setPromptText(
                                "Motivo por el cual ingresa el animal");

                documentationField = new TextArea();

                entryObservationsField = new TextArea();

                documentationField.setPromptText(
                                "Documentos, resoluciones, certificados, etc.");

                entryObservationsField.setPromptText(
                                "Información adicional del ingreso");

                documentationField.setPrefRowCount(
                                3);

                entryObservationsField.setPrefRowCount(
                                3);

                GridPane grid = new GridPane();

                grid.setHgap(20);
                grid.setVgap(15);

                addField(
                                grid,
                                "Número de registro",
                                recordNumberField,
                                0,
                                0);

                addField(
                                grid,
                                "Fecha de ingreso",
                                entryDatePicker,
                                1,
                                0);

                addField(
                                grid,
                                "Organización de origen",
                                sourceOrganizationField,
                                0,
                                2);

                addField(
                                grid,
                                "Responsable de la entrega",
                                deliveryResponsibleField,
                                1,
                                2);

                addField(
                                grid,
                                "Origen",
                                originField,
                                0,
                                4);

                addField(
                                grid,
                                "Motivo de ingreso",
                                entryReasonField,
                                1,
                                4);

                addField(
                                grid,
                                "Documentación",
                                documentationField,
                                0,
                                6);

                addField(
                                grid,
                                "Observaciones",
                                entryObservationsField,
                                1,
                                6);

                Button cancel = secondaryButton(
                                "CANCELAR");

                Button action = primaryButton(
                                editing
                                                ? "ACTUALIZAR INGRESO"
                                                : "GUARDAR INGRESO");

                cancel.setOnAction(
                                event -> closeCurrentModal());

                if (editing) {

                        action.setOnAction(
                                        event -> updateEntry());

                } else {

                        action.setOnAction(
                                        event -> saveEntry());
                }

                HBox buttons = new HBox(
                                10,
                                cancel,
                                action);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox card = new VBox(
                                20,
                                title(
                                                "1. Información del ingreso"),
                                new Label(
                                                "Datos generales del acta de ingreso."),
                                grid,
                                buttons);

                ((Label) card.getChildren().get(1))
                                .setStyle(
                                                "-fx-text-fill: #727A76;" +
                                                                "-fx-font-size: 12px;");

                card.setPadding(
                                new Insets(25));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        // =========================================================
        // DETAIL FORM FOR EDIT
        // =========================================================

        private VBox createDetailFormForEdit() {

                VBox container = new VBox(15);

                Label information = new Label(
                                "Los animales asociados forman parte del historial del ingreso y no se modifican desde esta pantalla.");

                information.setWrapText(
                                true);

                information.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 12px;");

                configureDetailsTable();

                detailSummaryLabel = new Label();

                updateDetailSummary();

                detailSummaryLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                container.getChildren().addAll(
                                title(
                                                "Animales asociados"),
                                information,
                                detailSummaryLabel,
                                detailsTable);

                container.setPadding(
                                new Insets(25));

                container.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                return container;
        }

        // =========================================================
        // DETAIL GRID
        // =========================================================

        private GridPane createDetailGrid() {

                GridPane grid = new GridPane();

                grid.setHgap(20);
                grid.setVgap(15);

                Label speciesLabel = requiredLabel(
                                "Especie");

                HBox speciesContainer = new HBox(8);

                Button addSpeciesButton = secondaryButton(
                                "+ NUEVA ESPECIE");

                addSpeciesButton.setOnAction(
                                event -> openAddSpeciesModal());

                HBox.setHgrow(
                                speciesComboBox,
                                Priority.ALWAYS);

                speciesComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                speciesContainer.getChildren().addAll(
                                speciesComboBox,
                                addSpeciesButton);

                GridPane.setHgrow(
                                speciesContainer,
                                Priority.ALWAYS);

                grid.add(
                                speciesLabel,
                                0,
                                0);

                grid.add(
                                speciesContainer,
                                0,
                                1);

                Label animalLabel = requiredLabel(
                                "Animal");

                HBox animalContainer = new HBox(8);

                Button addAnimalButton = secondaryButton(
                                "+ NUEVO ANIMAL");

                addAnimalButton.setOnAction(
                                event -> openAddAnimalModal());

                HBox.setHgrow(
                                animalComboBox,
                                Priority.ALWAYS);

                animalComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                animalContainer.getChildren().addAll(
                                animalComboBox,
                                addAnimalButton);

                GridPane.setHgrow(
                                animalContainer,
                                Priority.ALWAYS);

                grid.add(
                                animalLabel,
                                1,
                                0);

                grid.add(
                                animalContainer,
                                1,
                                1);

                Label enclosureLabel = requiredLabel(
                                "Recinto / Habitáculo");

                HBox enclosureContainer = new HBox(8);

                Button addEnclosureButton = secondaryButton(
                                "+ NUEVO RECINTO");

                addEnclosureButton.setOnAction(
                                event -> openAddEnclosureModal());

                HBox.setHgrow(
                                enclosureComboBox,
                                Priority.ALWAYS);

                enclosureComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                enclosureContainer.getChildren().addAll(
                                enclosureComboBox,
                                addEnclosureButton);

                GridPane.setHgrow(
                                enclosureContainer,
                                Priority.ALWAYS);

                grid.add(
                                enclosureLabel,
                                0,
                                2);

                grid.add(
                                enclosureContainer,
                                0,
                                3);

                addField(
                                grid,
                                "Cantidad",
                                quantityField,
                                0,
                                10);

                addField(
                                grid,
                                "Sexo",
                                sexComboBox,
                                1,
                                8);

                addField(
                                grid,
                                "Edad",
                                ageField,
                                0,
                                4);

                addField(
                                grid,
                                "Peso",
                                weightField,
                                1,
                                4);

                addField(
                                grid,
                                "Destino",
                                destinationComboBox,
                                0,
                                6);

                addField(
                                grid,
                                "Estado del ingreso",
                                entryStatusComboBox,
                                1,
                                6);

                addField(
                                grid,
                                "Observaciones",
                                detailObservationsField,
                                0,
                                8);

                return grid;
        }

        // =========================================================
        // INITIALIZE DETAIL CONTROLS
        // =========================================================

        private void initializeDetailControls() {

                speciesComboBox = new ComboBox<>();

                enclosureComboBox = new ComboBox<>();

                animalComboBox = new ComboBox<>(
                                animals);

                quantityField = new TextField();

                quantityField.setPromptText(
                                "Cantidad");

                sexComboBox = new ComboBox<>();

                ageField = new TextField();

                ageField.setPromptText(
                                "Ej.: 2 años");

                weightField = new TextField();

                weightField.setPromptText(
                                "Peso en kg");

                destinationComboBox = new ComboBox<>();

                entryStatusComboBox = new ComboBox<>();

                detailObservationsField = new TextArea();

                detailObservationsField.setPromptText(
                                "Observaciones específicas del animal");

                loadSpecies();

                configureSpeciesComboBox();
                configureEnclosureComboBox();
                configureAnimalComboBox();
                configureSexComboBox();

                loadActiveEnclosures();
                configureDestinationComboBox();
                configureEntryStatusComboBox();

                speciesComboBox.setOnAction(
                                event -> loadAnimalsBySpecies());
        }

        // =========================================================
        // ENCLOSURES
        // =========================================================

        private void loadActiveEnclosures() {

                if (enclosureComboBox == null) {
                        return;
                }

                activeEnclosures.setAll(
                                enclosureDAO.listActive());

                enclosureComboBox.setItems(
                                activeEnclosures);
        }

        private void configureEnclosureComboBox() {

                enclosureComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Enclosure item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : item.toString());
                                        }
                                });

                enclosureComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Enclosure item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : item.toString());
                                        }
                                });
        }

        private Enclosure findEnclosureById(
                        Integer enclosureId) {

                if (enclosureId == null) {
                        return null;
                }

                for (Enclosure enclosure : enclosureDAO.list()) {

                        if (enclosure.getEnclosureId() == enclosureId) {
                                return enclosure;
                        }
                }

                return null;
        }

        private void openAddEnclosureModal() {

                Stage modal = new Stage();

                modal.initModality(
                                Modality.APPLICATION_MODAL);

                modal.setTitle(
                                "Nuevo recinto");

                modal.setResizable(false);

                VBox container = new VBox(16);

                container.setPadding(
                                new Insets(25));

                container.setStyle(
                                "-fx-background-color: #F4F2EA;");

                Label modalTitle = title(
                                "Nuevo recinto / habitáculo");

                Label description = new Label(
                                "Complete la información del recinto disponible para alojar al animal.");

                description.setWrapText(true);

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                TextField nameField = new TextField();
                nameField.setPromptText(
                                "Nombre del recinto");

                TextField sectorField = new TextField();
                sectorField.setPromptText(
                                "Sector");

                TextField capacityField = new TextField();
                capacityField.setPromptText(
                                "Capacidad");

                TextArea observationsField = new TextArea();
                observationsField.setPromptText(
                                "Observaciones");
                observationsField.setPrefRowCount(3);

                GridPane grid = new GridPane();
                grid.setHgap(15);
                grid.setVgap(12);

                addField(grid, "Nombre", nameField, 0, 0);
                addField(grid, "Sector", sectorField, 1, 0);
                addField(grid, "Capacidad", capacityField, 0, 2);
                addField(grid, "Observaciones", observationsField, 1, 2);

                Button cancelButton = secondaryButton(
                                "CANCELAR");

                Button saveButton = primaryButton(
                                "GUARDAR");

                cancelButton.setOnAction(
                                event -> modal.close());

                saveButton.setOnAction(
                                event -> {

                                        String name = nameField.getText().trim();

                                        if (name.isBlank()) {
                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "El nombre del recinto es obligatorio.");
                                                return;
                                        }

                                        int capacity;

                                        try {
                                                capacity = capacityField.getText().isBlank()
                                                                ? 0
                                                                : Integer.parseInt(
                                                                                capacityField.getText().trim());
                                        } catch (NumberFormatException exception) {
                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "La capacidad debe ser un número válido.");
                                                return;
                                        }

                                        if (capacity < 0) {
                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "La capacidad no puede ser negativa.");
                                                return;
                                        }

                                        Enclosure enclosure = new Enclosure();

                                        enclosure.setName(name);
                                        enclosure.setSector(
                                                        sectorField.getText().trim());
                                        enclosure.setCapacity(capacity);
                                        enclosure.setStatus("Active");
                                        enclosure.setObservations(
                                                        observationsField.getText().trim());

                                        try {
                                                enclosureDAO.add(enclosure);
                                                loadActiveEnclosures();

                                                Enclosure created = findEnclosureByName(name);

                                                if (created != null) {
                                                        enclosureComboBox.setValue(created);
                                                }

                                                modal.close();

                                        } catch (Exception exception) {
                                                showMessage(
                                                                Alert.AlertType.ERROR,
                                                                "Error al guardar",
                                                                exception.getMessage());
                                        }
                                });

                HBox buttons = new HBox(
                                10,
                                cancelButton,
                                saveButton);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                container.getChildren().addAll(
                                modalTitle,
                                description,
                                grid,
                                buttons);

                Scene scene = new Scene(
                                container,
                                650,
                                430);

                modal.setScene(scene);
                modal.showAndWait();
        }

        private Enclosure findEnclosureByName(
                        String name) {

                for (Enclosure enclosure : enclosureDAO.list()) {

                        if (enclosure.getName() != null &&
                                        enclosure.getName().equalsIgnoreCase(name)) {
                                return enclosure;
                        }
                }

                return null;
        }

        // =========================================================
        // COMBO BOX CONFIGURATION
        // =========================================================

        private void configureSpeciesComboBox() {

                speciesComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Species item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        item.getName());
                                                }
                                        }
                                });

                speciesComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Species item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        item.getName());
                                                }
                                        }
                                });
        }

        private void configureAnimalComboBox() {

                animalComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Animal item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        formatAnimal(item));
                                                }
                                        }
                                });

                animalComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Animal item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        formatAnimal(item));
                                                }
                                        }
                                });
        }

        private void configureSexComboBox() {

                sexComboBox.getItems().addAll(
                                "Male",
                                "Female",
                                "Unknown");

                sexComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateSex(item));
                                        }
                                });

                sexComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateSex(item));
                                        }
                                });
        }

        private void configureDestinationComboBox() {

                destinationComboBox.getItems().addAll(
                                AnimalInventoryService.QUARANTINE,
                                AnimalInventoryService.PERMANENT);

                destinationComboBox.setValue(
                                AnimalInventoryService.QUARANTINE);

                destinationComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateDestination(item));
                                        }
                                });

                destinationComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateDestination(item));
                                        }
                                });
        }

        private void configureEntryStatusComboBox() {

                entryStatusComboBox.getItems().addAll(
                                "Active",
                                "Pending",
                                "Under Treatment");

                entryStatusComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateEntryStatus(item));
                                        }
                                });

                entryStatusComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateEntryStatus(item));
                                        }
                                });
        }

        private String formatAnimal(
                        Animal animal) {

                if (animal == null) {
                        return "";
                }

                String commonName = animal.getCommonName();

                String scientificName = animal.getScientificName();

                if (commonName == null ||
                                commonName.isBlank()) {

                        return scientificName == null
                                        ? "Animal"
                                        : scientificName;
                }

                if (scientificName == null ||
                                scientificName.isBlank()) {

                        return commonName;
                }

                return commonName +
                                " — " +
                                scientificName;
        }

        // =========================================================
        // SPECIES
        // =========================================================

        private void loadSpecies() {

                ObservableList<Species> species = FXCollections.observableArrayList(
                                speciesDAO.list());

                speciesComboBox.setItems(
                                species);
        }

        private void openAddSpeciesModal() {

                Stage modal = new Stage();

                modal.initModality(
                                Modality.APPLICATION_MODAL);

                modal.setTitle(
                                "Nueva especie");

                modal.setResizable(
                                false);

                VBox container = new VBox(18);

                container.setPadding(
                                new Insets(25));

                container.setStyle(
                                "-fx-background-color: #F4F2EA;");

                Label modalTitle = title(
                                "Nueva especie");

                Label description = new Label(
                                "Ingrese el nombre de la nueva especie.");

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                Label nameLabel = requiredLabel(
                                "Nombre de la especie");

                TextField nameField = new TextField();

                nameField.setPromptText(
                                "Ej.: Puma");

                Button cancelButton = secondaryButton(
                                "CANCELAR");

                Button saveButton = primaryButton(
                                "GUARDAR");

                cancelButton.setOnAction(
                                event -> modal.close());

                saveButton.setOnAction(
                                event -> {

                                        String name = nameField
                                                        .getText()
                                                        .trim();

                                        if (name.isEmpty()) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "El nombre de la especie es obligatorio.");

                                                return;
                                        }

                                        Species species = new Species();

                                        species.setName(
                                                        name);

                                        try {

                                                speciesDAO.add(
                                                                species);

                                                loadSpecies();

                                                Species createdSpecies = findSpeciesByName(
                                                                name);

                                                if (createdSpecies != null) {

                                                        speciesComboBox.setValue(
                                                                        createdSpecies);

                                                        loadAnimalsBySpecies();
                                                }

                                                modal.close();

                                        } catch (Exception exception) {

                                                showMessage(
                                                                Alert.AlertType.ERROR,
                                                                "Error al guardar",
                                                                exception.getMessage());
                                        }
                                });

                HBox buttons = new HBox(
                                10,
                                cancelButton,
                                saveButton);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                container.getChildren().addAll(
                                modalTitle,
                                description,
                                nameLabel,
                                nameField,
                                buttons);

                Scene scene = new Scene(
                                container,
                                430,
                                250);

                modal.setScene(
                                scene);

                modal.showAndWait();
        }

        private Species findSpeciesByName(
                        String name) {

                for (Species species : speciesComboBox.getItems()) {

                        if (species.getName()
                                        .equalsIgnoreCase(name)) {

                                return species;
                        }
                }

                return null;
        }

        // =========================================================
        // ANIMAL
        // =========================================================

        private void openAddAnimalModal() {

                Species selectedSpecies = speciesComboBox.getValue();

                if (selectedSpecies == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Especie requerida",
                                        "Debe seleccionar una especie antes de registrar un animal.");

                        return;
                }

                Stage modal = new Stage();

                modal.initModality(
                                Modality.APPLICATION_MODAL);

                modal.setTitle(
                                "Nuevo animal");

                modal.setResizable(
                                false);

                VBox container = new VBox(16);

                container.setPadding(
                                new Insets(25));

                container.setStyle(
                                "-fx-background-color: #F4F2EA;");

                Label modalTitle = title(
                                "Nuevo animal");

                Label description = new Label(
                                "Complete la información del animal.");

                description.setStyle(
                                "-fx-text-fill: #727A76;");

                Label speciesInfo = new Label(
                                "Especie: " +
                                                selectedSpecies.getName());

                speciesInfo.setStyle(
                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                Label commonNameLabel = requiredLabel(
                                "Nombre común");

                TextField commonNameField = new TextField();

                commonNameField.setPromptText(
                                "Ingrese el nombre común");

                Label scientificNameLabel = requiredLabel(
                                "Nombre científico");

                TextField scientificNameField = new TextField();

                scientificNameField.setPromptText(
                                "Ingrese el nombre científico");

                Label originLabel = requiredLabel(
                                "Origen");

                TextField originField = new TextField();

                originField.setPromptText(
                                "Ingrese el origen");

                Label statusLabel = requiredLabel(
                                "Estado");

                ComboBox<String> statusComboBox = new ComboBox<>();

                statusComboBox.getItems().addAll(
                                "Active",
                                "Inactive");

                statusComboBox.setValue(
                                "Active");

                statusComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateAnimalStatus(item));
                                        }
                                });

                statusComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                setText(
                                                                empty || item == null
                                                                                ? null
                                                                                : translateAnimalStatus(item));
                                        }
                                });

                statusComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                Button cancelButton = secondaryButton(
                                "CANCELAR");

                Button saveButton = primaryButton(
                                "GUARDAR");

                cancelButton.setOnAction(
                                event -> modal.close());

                saveButton.setOnAction(
                                event -> {

                                        String commonName = commonNameField
                                                        .getText()
                                                        .trim();

                                        String scientificName = scientificNameField
                                                        .getText()
                                                        .trim();

                                        String origin = originField
                                                        .getText()
                                                        .trim();

                                        String status = statusComboBox.getValue();

                                        if (commonName.isEmpty()) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "El nombre común es obligatorio.");

                                                return;
                                        }

                                        if (scientificName.isEmpty()) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "El nombre científico es obligatorio.");

                                                return;
                                        }

                                        if (origin.isEmpty()) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "El origen es obligatorio.");

                                                return;
                                        }

                                        if (status == null ||
                                                        status.isBlank()) {

                                                showMessage(
                                                                Alert.AlertType.WARNING,
                                                                "Error de validación",
                                                                "El estado es obligatorio.");

                                                return;
                                        }

                                        Animal animal = new Animal();

                                        animal.setSpeciesId(
                                                        selectedSpecies.getSpeciesId());

                                        animal.setCommonName(
                                                        commonName);

                                        animal.setScientificName(
                                                        scientificName);

                                        animal.setOrigin(
                                                        origin);

                                        animal.setCurrentQuantity(
                                                        0);

                                        animal.setStatus(
                                                        status);

                                        try {

                                                animalDAO.add(
                                                                animal);

                                                loadAnimalsBySpecies();

                                                Animal createdAnimal = findAnimal(
                                                                commonName,
                                                                scientificName,
                                                                selectedSpecies.getSpeciesId());

                                                if (createdAnimal != null) {

                                                        animalComboBox.setValue(
                                                                        createdAnimal);
                                                }

                                                modal.close();

                                        } catch (Exception exception) {

                                                showMessage(
                                                                Alert.AlertType.ERROR,
                                                                "Error al guardar",
                                                                exception.getMessage());
                                        }
                                });

                HBox buttons = new HBox(
                                10,
                                cancelButton,
                                saveButton);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox commonNameContainer = new VBox(
                                6,
                                commonNameLabel,
                                commonNameField);

                VBox scientificNameContainer = new VBox(
                                6,
                                scientificNameLabel,
                                scientificNameField);

                VBox originContainer = new VBox(
                                6,
                                originLabel,
                                originField);

                VBox statusContainer = new VBox(
                                6,
                                statusLabel,
                                statusComboBox);

                container.getChildren().addAll(
                                modalTitle,
                                description,
                                speciesInfo,
                                commonNameContainer,
                                scientificNameContainer,
                                originContainer,
                                statusContainer,
                                buttons);

                Scene scene = new Scene(
                                container,
                                430,
                                500);

                modal.setScene(
                                scene);

                modal.showAndWait();
        }

        private Animal findAnimal(
                        String commonName,
                        String scientificName,
                        int speciesId) {

                for (Animal animal : animals) {

                        if (animal.getSpeciesId() == speciesId
                                        &&
                                        animal.getCommonName()
                                                        .equalsIgnoreCase(
                                                                        commonName)
                                        &&
                                        animal.getScientificName()
                                                        .equalsIgnoreCase(
                                                                        scientificName)) {

                                return animal;
                        }
                }

                return null;
        }

        // =========================================================
        // ADD DETAIL
        // =========================================================

        private void addDetail() {

                try {

                        Animal selectedAnimal = animalComboBox.getValue();

                        if (selectedAnimal == null) {

                                throw new IllegalArgumentException(
                                                "Debe seleccionar un animal.");
                        }

                        String quantityText = quantityField
                                        .getText()
                                        .trim();

                        if (quantityText.isBlank()) {

                                throw new IllegalArgumentException(
                                                "La cantidad es obligatoria.");
                        }

                        int quantity = Integer.parseInt(
                                        quantityText);

                        if (quantity <= 0) {

                                throw new IllegalArgumentException(
                                                "La cantidad debe ser mayor que cero.");
                        }

                        double weight = 0;

                        if (!weightField.getText().isBlank()) {

                                weight = Double.parseDouble(
                                                weightField
                                                                .getText()
                                                                .trim());
                        }

                        if (weight < 0) {

                                throw new IllegalArgumentException(
                                                "El peso no puede ser negativo.");
                        }

                        if (destinationComboBox.getValue() == null) {

                                throw new IllegalArgumentException(
                                                "Debe seleccionar un destino.");
                        }

                        if (entryStatusComboBox.getValue() == null) {

                                throw new IllegalArgumentException(
                                                "Debe seleccionar el estado del ingreso.");
                        }

                        if (enclosureComboBox.getValue() == null) {
                                throw new IllegalArgumentException(
                                                "Debe seleccionar un recinto / habitáculo.");
                        }

                        EntryDetail detail = new EntryDetail();

                        detail.setAnimalId(
                                        selectedAnimal.getAnimalId());

                        detail.setEnclosureId(
                                        enclosureComboBox.getValue().getEnclosureId());

                        detail.setQuantity(
                                        quantity);

                        detail.setSex(
                                        sexComboBox.getValue());

                        detail.setAge(
                                        ageField
                                                        .getText()
                                                        .trim());

                        detail.setWeight(
                                        weight);

                        detail.setDestination(
                                        destinationComboBox
                                                        .getValue());

                        detail.setEntryStatus(
                                        entryStatusComboBox
                                                        .getValue());

                        detail.setObservations(
                                        detailObservationsField
                                                        .getText()
                                                        .trim());

                        pendingDetails.add(
                                        detail);

                        updateDetailSummary();

                        clearDetail();

                } catch (NumberFormatException exception) {

                        showMessage(
                                        Alert.AlertType.ERROR,
                                        "Error de validación",
                                        "La cantidad y el peso deben contener valores numéricos válidos.");

                } catch (Exception exception) {

                        showMessage(
                                        Alert.AlertType.ERROR,
                                        "Error de validación",
                                        exception.getMessage());
                }
        }

        // =========================================================
        // DETAIL SUMMARY
        // =========================================================

        private void updateDetailSummary() {

                if (detailSummaryLabel == null) {
                        return;
                }

                int animalGroups = pendingDetails.size();

                int totalQuantity = 0;

                for (EntryDetail detail : pendingDetails) {

                        totalQuantity += detail.getQuantity();
                }

                detailSummaryLabel.setText(
                                "Registros agregados: " +
                                                animalGroups +
                                                "  |  Cantidad total de animales: " +
                                                totalQuantity);
        }

        // =========================================================
        // SAVE ENTRY
        // =========================================================

        private void saveEntry() {

                try {

                        Entry entry = buildEntry();

                        int entryId = entryService.createEntry(
                                        entry,
                                        new ArrayList<>(
                                                        pendingDetails));

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Ingreso registrado",
                                        "El ingreso " +
                                                        entryId +
                                                        " fue registrado correctamente.");

                        pendingDetails.clear();

                        closeNewEntryOverlay();

                        loadEntries();

                } catch (Exception exception) {

                        String message = exception.getMessage();

                        if (message == null ||
                                        message.isBlank()) {

                                message = "No fue posible registrar el ingreso.";
                        }

                        showMessage(
                                        Alert.AlertType.ERROR,
                                        "Error al guardar",
                                        message);
                }
        }

        // =========================================================
        // BUILD ENTRY
        // =========================================================

        private Entry buildEntry() {

                validateEntryInformation();

                if (pendingDetails.isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Debe agregar al menos un animal al ingreso.");
                }

                Entry entry = createEntryFromFields();

                return entry;
        }

        // =========================================================
        // BUILD ENTRY WITHOUT DETAILS
        // =========================================================

        private Entry buildEntryWithoutDetails() {

                validateEntryInformation();

                return createEntryFromFields();
        }

        // =========================================================
        // VALIDATE ENTRY
        // =========================================================

        private void validateEntryInformation() {

                if (recordNumberField
                                .getText()
                                .isBlank()) {

                        throw new IllegalArgumentException(
                                        "El número de registro es obligatorio.");
                }

                if (entryDatePicker.getValue() == null) {

                        throw new IllegalArgumentException(
                                        "La fecha de ingreso es obligatoria.");
                }
        }

        // =========================================================
        // CREATE ENTRY FROM FIELDS
        // =========================================================

        private Entry createEntryFromFields() {

                Entry entry = new Entry();

                entry.setRecordNumber(
                                recordNumberField
                                                .getText()
                                                .trim());

                entry.setEntryDate(
                                entryDatePicker
                                                .getValue()
                                                .toString());

                entry.setSourceOrganization(
                                sourceOrganizationField
                                                .getText()
                                                .trim());

                entry.setDeliveryResponsible(
                                deliveryResponsibleField
                                                .getText()
                                                .trim());

                entry.setOrigin(
                                originField
                                                .getText()
                                                .trim());

                entry.setEntryReason(
                                entryReasonField
                                                .getText()
                                                .trim());

                entry.setDocumentation(
                                documentationField
                                                .getText()
                                                .trim());

                entry.setObservations(
                                entryObservationsField
                                                .getText()
                                                .trim());

                return entry;
        }

        // =========================================================
        // UPDATE ENTRY
        // =========================================================

        private void updateEntry() {

                if (selectedEntry == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin selección",
                                        "Debe seleccionar una entrada primero.");

                        return;
                }

                try {

                        Entry updated = buildEntryWithoutDetails();

                        updated.setEntryId(
                                        selectedEntry.getEntryId());

                        entryService.updateEntry(
                                        updated);

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Ingreso actualizado",
                                        "La información del ingreso fue actualizada correctamente.");

                        closeCurrentModal();

                } catch (Exception exception) {

                        showMessage(
                                        Alert.AlertType.ERROR,
                                        "Error al actualizar",
                                        exception.getMessage());
                }
        }

        // =========================================================
        // EDIT SELECTED ENTRY
        // =========================================================

        private void editSelectedEntry() {

                if (selectedEntry == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin selección",
                                        "Debe seleccionar un ingreso primero.");

                        return;
                }

                openEditEntryModal(
                                selectedEntry);
        }

        // =========================================================
        // DELETE SELECTED ENTRY
        // =========================================================

        private void deleteSelectedEntry() {

                if (selectedEntry == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Sin selección",
                                        "Debe seleccionar un ingreso primero.");

                        return;
                }

                Alert confirmation = new Alert(
                                Alert.AlertType.CONFIRMATION);

                confirmation.setTitle(
                                "Eliminar ingreso");

                confirmation.setHeaderText(
                                "¿Está seguro de eliminar este ingreso?");

                confirmation.setContentText(
                                "Registro: " +
                                                selectedEntry.getRecordNumber() +
                                                "\n\n" +
                                                "Este ingreso representa el documento de origen de los animales registrados.");

                confirmation.showAndWait()
                                .ifPresent(
                                                result -> {

                                                        if (result == ButtonType.OK) {

                                                                try {

                                                                        entryService.deleteEntry(
                                                                                        selectedEntry.getEntryId());

                                                                        showMessage(
                                                                                        Alert.AlertType.INFORMATION,
                                                                                        "Ingreso eliminado",
                                                                                        "El ingreso fue eliminado correctamente.");

                                                                        selectedEntry = null;

                                                                        loadEntries();

                                                                } catch (Exception exception) {

                                                                        showMessage(
                                                                                        Alert.AlertType.ERROR,
                                                                                        "Error al eliminar",
                                                                                        exception.getMessage());
                                                                }
                                                        }
                                                });
        }

        // =========================================================
        // LOAD ENTRY
        // =========================================================

        private void loadEntry(
                        Entry entry) {

                recordNumberField.setText(
                                entry.getRecordNumber());

                entryDatePicker.setValue(
                                LocalDate.parse(
                                                entry.getEntryDate()));

                sourceOrganizationField.setText(
                                entry.getSourceOrganization());

                deliveryResponsibleField.setText(
                                entry.getDeliveryResponsible());

                originField.setText(
                                entry.getOrigin());

                entryReasonField.setText(
                                entry.getEntryReason());

                documentationField.setText(
                                entry.getDocumentation());

                entryObservationsField.setText(
                                entry.getObservations());

                pendingDetails.setAll(
                                detailDAO.listByEntry(
                                                entry.getEntryId()));

                updateDetailSummary();
        }

        // =========================================================
        // LOAD ENTRIES
        // =========================================================

        private void loadEntries() {

                List<Entry> entries = entryDAO.list();

                allEntries.setAll(
                                entries);

                filterEntries(
                                searchField == null
                                                ? ""
                                                : searchField.getText());
        }

        // =========================================================
        // FILTER ENTRIES
        // =========================================================

        private void filterEntries(
                        String text) {

                String search = text == null
                                ? ""
                                : text.trim()
                                                .toLowerCase();

                if (search.isEmpty()) {

                        entriesTable.setItems(
                                        FXCollections.observableArrayList(
                                                        allEntries));

                        return;
                }

                ObservableList<Entry> filtered = FXCollections.observableArrayList();

                for (Entry entry : allEntries) {

                        boolean matchesRecord = contains(
                                        entry.getRecordNumber(),
                                        search);

                        boolean matchesOrganization = contains(
                                        entry.getSourceOrganization(),
                                        search);

                        boolean matchesReason = contains(
                                        entry.getEntryReason(),
                                        search);

                        boolean matchesOrigin = contains(
                                        entry.getOrigin(),
                                        search);

                        if (matchesRecord ||
                                        matchesOrganization ||
                                        matchesReason ||
                                        matchesOrigin) {

                                filtered.add(
                                                entry);
                        }
                }

                entriesTable.setItems(
                                filtered);
        }

        private boolean contains(
                        String value,
                        String search) {

                return value != null &&
                                value.toLowerCase()
                                                .contains(search);
        }

        // =========================================================
        // LOAD ANIMALS BY SPECIES
        // =========================================================

        private void loadAnimalsBySpecies() {

                animals.clear();

                Animal previousSelection = animalComboBox.getValue();

                Species species = speciesComboBox.getValue();

                if (species == null) {

                        animalComboBox.setItems(
                                        animals);

                        return;
                }

                for (Animal animal : animalDAO.list()) {

                        if (animal.getSpeciesId() == species.getSpeciesId()) {

                                animals.add(
                                                animal);
                        }
                }

                animalComboBox.setItems(
                                animals);

                if (previousSelection != null) {

                        for (Animal animal : animals) {

                                if (animal.getAnimalId() == previousSelection.getAnimalId()) {

                                        animalComboBox.setValue(
                                                        animal);

                                        break;
                                }
                        }
                }
        }

        // =========================================================
        // DETAILS TABLE
        // =========================================================

        private void configureDetailsTable() {

                detailsTable.getColumns().clear();

                TableColumn<EntryDetail, String> animalColumn = new TableColumn<>(
                                "Animal");

                animalColumn.setCellValueFactory(
                                cellData -> {

                                        EntryDetail detail = cellData.getValue();

                                        Animal animal = findAnimalById(
                                                        detail.getAnimalId());

                                        String animalName = animal != null
                                                        ? formatAnimal(animal)
                                                        : "Desconocido";

                                        return new SimpleStringProperty(
                                                        animalName);
                                });

                TableColumn<EntryDetail, String> enclosureColumn = new TableColumn<>(
                                "Recinto");

                enclosureColumn.setCellValueFactory(
                                cellData -> {

                                        EntryDetail detail = cellData.getValue();

                                        Enclosure enclosure = findEnclosureById(
                                                        detail.getEnclosureId());

                                        String enclosureName = enclosure != null
                                                        ? enclosure.toString()
                                                        : "Sin recinto";

                                        return new SimpleStringProperty(
                                                        enclosureName);
                                });

                TableColumn<EntryDetail, Integer> quantityColumn = new TableColumn<>(
                                "Cantidad");

                quantityColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "quantity"));

                TableColumn<EntryDetail, String> sexColumn = new TableColumn<>(
                                "Sexo");

                sexColumn.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                translateSex(
                                                                cellData
                                                                                .getValue()
                                                                                .getSex())));

                TableColumn<EntryDetail, String> ageColumn = new TableColumn<>(
                                "Edad");

                ageColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "age"));

                TableColumn<EntryDetail, Double> weightColumn = new TableColumn<>(
                                "Peso (kg)");

                weightColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "weight"));

                TableColumn<EntryDetail, String> destinationColumn = new TableColumn<>(
                                "Destino");

                destinationColumn.setCellValueFactory(
                                cellData -> {

                                        String value = cellData
                                                        .getValue()
                                                        .getDestination();

                                        return new SimpleStringProperty(
                                                        translateDestination(
                                                                        value));
                                });

                TableColumn<EntryDetail, String> statusColumn = new TableColumn<>(
                                "Estado");

                statusColumn.setCellValueFactory(
                                cellData -> {

                                        String value = cellData
                                                        .getValue()
                                                        .getEntryStatus();

                                        return new SimpleStringProperty(
                                                        translateEntryStatus(
                                                                        value));
                                });

                detailsTable.getColumns().addAll(
                                animalColumn,
                                enclosureColumn,
                                quantityColumn,
                                sexColumn,
                                ageColumn,
                                weightColumn,
                                destinationColumn,
                                statusColumn);

                detailsTable.setItems(
                                pendingDetails);

                detailsTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                detailsTable.setPrefHeight(
                                220);
        }

        // =========================================================
        // ENTRY DETAIL MODAL
        // =========================================================

        private void openEntryDetailModal(Entry entry) {

                if (entry == null || mainRoot == null) {
                        return;
                }

                closeEntryDetailModal();

                entryDetailOverlay = new StackPane();

                entryDetailOverlay.setStyle(
                                "-fx-background-color: rgba(0,0,0,0.38);");

                VBox modal = new VBox(18);

                modal.setPadding(
                                new Insets(28));

                modal.setMaxWidth(1050);
                modal.setMaxHeight(720);

                modal.setStyle(
                                "-fx-background-color: #F4F2EA;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 16;");

                Label modalTitle = title(
                                "Detalle del ingreso");

                Label description = new Label(
                                "Consulta completa de la información registrada y de los animales asociados.");

                description.setStyle(
                                "-fx-text-fill: #727A76;" +
                                                "-fx-font-size: 12px;");

                Button closeButton = secondaryButton(
                                "CERRAR");

                closeButton.setOnAction(
                                event -> closeEntryDetailModal());

                HBox header = new HBox(
                                12,
                                new VBox(5, modalTitle, description),
                                closeButton);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                HBox.setHgrow(
                                header.getChildren().get(0),
                                Priority.ALWAYS);

                GridPane information = new GridPane();
                information.setHgap(20);
                information.setVgap(12);
                information.setPadding(
                                new Insets(20));
                information.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 12;");

                addReadOnlyInfo(information, "Número de registro",
                                entry.getRecordNumber(), 0, 0);
                addReadOnlyInfo(information, "Fecha de ingreso",
                                entry.getEntryDate(), 1, 0);
                addReadOnlyInfo(information, "Organización de origen",
                                entry.getSourceOrganization(), 0, 2);
                addReadOnlyInfo(information, "Responsable de la entrega",
                                entry.getDeliveryResponsible(), 1, 2);
                addReadOnlyInfo(information, "Origen",
                                entry.getOrigin(), 0, 4);
                addReadOnlyInfo(information, "Motivo de ingreso",
                                entry.getEntryReason(), 1, 4);
                addReadOnlyInfo(information, "Documentación",
                                entry.getDocumentation(), 0, 6);
                addReadOnlyInfo(information, "Observaciones",
                                entry.getObservations(), 1, 6);

                TableView<EntryDetail> detailView =
                                createEntryDetailReadOnlyTable(entry);

                Label animalsTitle = title(
                                "Animales asociados");

                Label modificationsTitle = title(
                                "Observaciones / modificaciones");

                VBox modificationBox = new VBox(8);

                List<EntryDetail> details =
                                detailDAO.listByEntry(entry.getEntryId());

                boolean foundObservation = false;

                if (entry.getObservations() != null &&
                                !entry.getObservations().isBlank()) {

                        Label entryObservation = new Label(
                                        "Ingreso: " + entry.getObservations());

                        entryObservation.setWrapText(true);
                        entryObservation.setStyle(
                                        "-fx-background-color: white;" +
                                                        "-fx-padding: 10;" +
                                                        "-fx-background-radius: 8;");

                        modificationBox.getChildren().add(
                                        entryObservation);

                        foundObservation = true;
                }

                for (EntryDetail detail : details) {

                        if (detail.getObservations() != null &&
                                        !detail.getObservations().isBlank()) {

                                Label observation = new Label(
                                                "Animal: " +
                                                                getAnimalDisplayName(detail.getAnimalId()) +
                                                                " — " +
                                                                detail.getObservations());

                                observation.setWrapText(true);
                                observation.setStyle(
                                                "-fx-background-color: white;" +
                                                                "-fx-padding: 10;" +
                                                                "-fx-background-radius: 8;");

                                modificationBox.getChildren().add(
                                                observation);

                                foundObservation = true;
                        }
                }

                if (!foundObservation) {

                        Label noObservations = new Label(
                                        "No hay observaciones o modificaciones registradas.");

                        noObservations.setStyle(
                                        "-fx-text-fill: #727A76;");

                        modificationBox.getChildren().add(
                                        noObservations);
                }

                VBox content = new VBox(
                                15,
                                information,
                                animalsTitle,
                                detailView,
                                modificationsTitle,
                                modificationBox);

                ScrollPane scrollPane = new ScrollPane(
                                content);

                scrollPane.setFitToWidth(true);
                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setStyle(
                                "-fx-background-color: transparent;");

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                Button bottomClose = secondaryButton(
                                "CERRAR");

                bottomClose.setOnAction(
                                event -> closeEntryDetailModal());

                HBox bottom = new HBox(
                                bottomClose);

                bottom.setAlignment(
                                Pos.CENTER_RIGHT);

                modal.getChildren().addAll(
                                header,
                                scrollPane,
                                bottom);

                StackPane.setAlignment(
                                modal,
                                Pos.CENTER);

                StackPane.setMargin(
                                modal,
                                new Insets(25));

                entryDetailOverlay.getChildren().add(
                                modal);

                mainRoot.getChildren().add(
                                entryDetailOverlay);

                entryDetailOverlay.toFront();
        }

        private TableView<EntryDetail> createEntryDetailReadOnlyTable(
                        Entry entry) {

                TableView<EntryDetail> table = new TableView<>();

                TableColumn<EntryDetail, String> animalColumn = new TableColumn<>(
                                "Animal");
                animalColumn.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                getAnimalDisplayName(
                                                                cellData.getValue().getAnimalId())));

                TableColumn<EntryDetail, String> enclosureColumn = new TableColumn<>(
                                "Recinto");
                enclosureColumn.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                getEnclosureDisplayName(
                                                                cellData.getValue().getEnclosureId())));

                TableColumn<EntryDetail, Integer> quantityColumn = new TableColumn<>(
                                "Cantidad");
                quantityColumn.setCellValueFactory(
                                new PropertyValueFactory<>("quantity"));

                TableColumn<EntryDetail, String> sexColumn = new TableColumn<>(
                                "Sexo");
                sexColumn.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                translateSex(
                                                                cellData.getValue().getSex())));

                TableColumn<EntryDetail, String> ageColumn = new TableColumn<>(
                                "Edad");
                ageColumn.setCellValueFactory(
                                new PropertyValueFactory<>("age"));

                TableColumn<EntryDetail, Double> weightColumn = new TableColumn<>(
                                "Peso (kg)");
                weightColumn.setCellValueFactory(
                                new PropertyValueFactory<>("weight"));

                TableColumn<EntryDetail, String> destinationColumn = new TableColumn<>(
                                "Destino");
                destinationColumn.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                translateDestination(
                                                                cellData.getValue().getDestination())));

                TableColumn<EntryDetail, String> statusColumn = new TableColumn<>(
                                "Estado");
                statusColumn.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                translateEntryStatus(
                                                                cellData.getValue().getEntryStatus())));

                TableColumn<EntryDetail, String> observationsColumn = new TableColumn<>(
                                "Observaciones");
                observationsColumn.setCellValueFactory(
                                new PropertyValueFactory<>("observations"));

                table.getColumns().addAll(
                                animalColumn,
                                enclosureColumn,
                                quantityColumn,
                                sexColumn,
                                ageColumn,
                                weightColumn,
                                destinationColumn,
                                statusColumn,
                                observationsColumn);

                table.setItems(
                                FXCollections.observableArrayList(
                                                detailDAO.listByEntry(
                                                                entry.getEntryId())));

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);
                table.setPrefHeight(230);

                return table;
        }

        private void addReadOnlyInfo(
                        GridPane grid,
                        String labelText,
                        String value,
                        int column,
                        int row) {

                VBox box = new VBox(4);

                Label label = new Label(labelText);
                label.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #727A76;");

                Label valueLabel = new Label(
                                value == null || value.isBlank()
                                                ? "Sin información"
                                                : value);

                valueLabel.setWrapText(true);
                valueLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #23452C;");

                box.getChildren().addAll(
                                label,
                                valueLabel);

                grid.add(
                                box,
                                column,
                                row);

                GridPane.setHgrow(
                                box,
                                Priority.ALWAYS);
        }

        private String getAnimalDisplayName(
                        int animalId) {

                Animal animal = findAnimalById(animalId);

                return animal == null
                                ? "Animal #" + animalId
                                : formatAnimal(animal);
        }

        private String getEnclosureDisplayName(
                        Integer enclosureId) {

                Enclosure enclosure = findEnclosureById(enclosureId);

                return enclosure == null
                                ? "Sin recinto"
                                : enclosure.toString();
        }

        private void closeEntryDetailModal() {

                if (entryDetailOverlay != null &&
                                mainRoot != null) {

                        mainRoot.getChildren().remove(
                                        entryDetailOverlay);

                        entryDetailOverlay = null;
                }
        }

        // =========================================================
        // ENTRIES TABLE COLUMNS
        // =========================================================

        private void createEntriesColumns() {

                entriesTable.getColumns().clear();

                TableColumn<Entry, Integer> idColumn = new TableColumn<>(
                                "ID");

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryId"));

                TableColumn<Entry, String> recordColumn = new TableColumn<>(
                                "Registro");

                recordColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "recordNumber"));

                TableColumn<Entry, String> dateColumn = new TableColumn<>(
                                "Fecha");

                dateColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryDate"));

                TableColumn<Entry, String> sourceColumn = new TableColumn<>(
                                "Organización de origen");

                sourceColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "sourceOrganization"));

                TableColumn<Entry, String> reasonColumn = new TableColumn<>(
                                "Motivo");

                reasonColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryReason"));

                entriesTable.getColumns().addAll(
                                idColumn,
                                recordColumn,
                                dateColumn,
                                sourceColumn,
                                reasonColumn);
        }

        // =========================================================
        // CLEAR DETAIL
        // =========================================================

        private void clearDetail() {

                if (speciesComboBox == null) {
                        return;
                }

                speciesComboBox.setValue(
                                null);

                if (enclosureComboBox != null) {
                        loadActiveEnclosures();
                        enclosureComboBox.setValue(null);
                }

                animals.clear();

                animalComboBox.setItems(
                                animals);

                animalComboBox.setValue(
                                null);

                quantityField.clear();

                sexComboBox.setValue(
                                null);

                ageField.clear();

                weightField.clear();

                destinationComboBox.setValue(
                                AnimalInventoryService.QUARANTINE);

                entryStatusComboBox.setValue(
                                null);

                detailObservationsField.clear();
        }

        // =========================================================
        // CLOSE CURRENT MODAL
        // =========================================================

        private void closeCurrentModal() {

                if (recordNumberField == null ||
                                recordNumberField.getScene() == null) {

                        return;
                }

                Stage stage = (Stage) recordNumberField
                                .getScene()
                                .getWindow();

                stage.close();
        }

        // =========================================================
        // FIND ANIMAL
        // =========================================================

        private Animal findAnimalById(
                        int animalId) {

                for (Animal animal : animalDAO.list()) {

                        if (animal.getAnimalId() == animalId) {

                                return animal;
                        }
                }

                return null;
        }

        // =========================================================
        // TRANSLATIONS
        // =========================================================

        private String translateSex(
                        String value) {

                if (value == null) {
                        return "";
                }

                return switch (value) {

                        case "Male" ->
                                "Macho";

                        case "Female" ->
                                "Hembra";

                        case "Unknown" ->
                                "Desconocido";

                        default ->
                                value;
                };
        }

        private String translateDestination(
                        String value) {

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

        private String translateEntryStatus(
                        String value) {

                if (value == null) {
                        return "";
                }

                return switch (value) {

                        case "Active" ->
                                "Activo";

                        case "Pending" ->
                                "Pendiente";

                        case "Under Treatment" ->
                                "En tratamiento";

                        default ->
                                value;
                };
        }

        private String translateAnimalStatus(
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

        // =========================================================
        // REQUIRED LABEL
        // =========================================================

        private Label requiredLabel(
                        String text) {

                Label label = new Label(
                                text + " *");

                label.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #4B5752;");

                return label;
        }

        // =========================================================
        // FIELD
        // =========================================================

        private void addField(
                        GridPane grid,
                        String labelText,
                        javafx.scene.control.Control control,
                        int column,
                        int row) {

                Label label = title(
                                labelText);

                label.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #4B5752;");

                grid.add(
                                label,
                                column,
                                row);

                grid.add(
                                control,
                                column,
                                row + 1);

                GridPane.setHgrow(
                                control,
                                Priority.ALWAYS);

                control.setMaxWidth(
                                Double.MAX_VALUE);
        }

        // =========================================================
        // TITLE
        // =========================================================

        private Label title(
                        String text) {

                Label label = new Label(
                                text);

                label.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                return label;
        }

        // =========================================================
        // PRIMARY BUTTON
        // =========================================================

        private Button primaryButton(
                        String text) {

                Button button = new Button(
                                text);

                button.setStyle(
                                "-fx-background-color: #23452C;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                return button;
        }

        // =========================================================
        // SECONDARY BUTTON
        // =========================================================

        private Button secondaryButton(
                        String text) {

                Button button = new Button(
                                text);

                button.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #23452C;" +
                                                "-fx-border-color: #B8C7B8;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                return button;
        }

        // =========================================================
        // DELETE BUTTON
        // =========================================================

        private Button deleteButton(
                        String text) {

                Button button = new Button(
                                text);

                button.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #A94442;" +
                                                "-fx-border-color: #D8B3B3;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                return button;
        }

        // =========================================================
        // MESSAGE
        // =========================================================

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