package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.AnimalHoldingDAO;
import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.MovementDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.Entry;
import com.tesina_tatu_carreta.model.Movement;
import com.tesina_tatu_carreta.service.AnimalInventoryService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewMovement {

        private final MovementDAO movementDAO =
                        new MovementDAO();

        private final AnimalDAO animalDAO =
                        new AnimalDAO();

        private final EntryDAO entryDAO =
                        new EntryDAO();

        private final AnimalHoldingDAO holdingDAO =
                        new AnimalHoldingDAO();

        private final AnimalInventoryService inventoryService =
                        new AnimalInventoryService();

        private TableView<Movement> table;

        private ComboBox<Animal> animalComboBox;

        private ComboBox<Entry> entryComboBox;

        private TextField dateField;

        private ComboBox<String> movementTypeComboBox;

        private TextField quantityField;

        private ComboBox<String> originComboBox;

        private TextField destinationField;

        private TextArea observationsArea;

        private Label availableQuantityLabel;

        private Label originAvailableQuantityLabel;

        // =========================================================
        // SECTION NAVIGATION
        // =========================================================

        private VBox sectionContainer;

        private Button registerMovementButton;

        private Button movementHistoryButton;

        private VBox registerSection;

        private VBox historySection;

        // =========================================================
        // MAIN VIEW
        // =========================================================

        public Parent getView() {
                return createView();
        }

        public Parent createView() {

                Label breadcrumb = new Label(
                                "Inicio / Gestión de movimientos");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #7A827B;");

                Label title = new Label(
                                "Movimientos de animales");

                title.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label subtitle = new Label(
                                "Registre y consulte los movimientos de animales dentro de la reserva.");

                subtitle.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #68746B;");

                VBox header = new VBox(
                                6,
                                breadcrumb,
                                title,
                                subtitle);

                // =====================================================
                // CREATE FIELDS
                // =====================================================

                animalComboBox = new ComboBox<>();

                animalComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                animalComboBox.setPromptText(
                                "Seleccione un animal");

                loadAnimals();

                animalComboBox.setOnAction(
                                event -> loadEntriesByAnimal());

                entryComboBox = new ComboBox<>();

                entryComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                entryComboBox.setPromptText(
                                "Seleccione primero un animal");

                entryComboBox.setOnAction(
                                event -> updateAvailableQuantity());

                availableQuantityLabel = new Label(
                                "Seleccione un acta para ver la cantidad disponible.");

                availableQuantityLabel.setWrapText(true);

                availableQuantityLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #55705F;");

                dateField = new TextField();

                dateField.setPromptText(
                                "Ej.: 25/08/2026");

                movementTypeComboBox = new ComboBox<>(
                                FXCollections.observableArrayList(
                                                "RELEASE",
                                                "TRANSFER",
                                                "DEATH"));

                movementTypeComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                movementTypeComboBox.setPromptText(
                                "Seleccione el tipo de movimiento");

                movementTypeComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty || item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        translateMovementType(item));
                                                }
                                        }
                                });

                movementTypeComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty || item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        translateMovementType(item));
                                                }
                                        }
                                });

                quantityField = new TextField();

                quantityField.setPromptText(
                                "Ingrese la cantidad disponible");

                // =====================================================
                // ORIGIN
                // =====================================================

                originComboBox = new ComboBox<>(
                                FXCollections.observableArrayList(
                                                AnimalInventoryService.PERMANENT,
                                                AnimalInventoryService.QUARANTINE));

                originComboBox.setMaxWidth(
                                Double.MAX_VALUE);

                originComboBox.setPromptText(
                                "Seleccione el lugar de origen");

                originComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty || item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        translateLocation(item));
                                                }
                                        }
                                });

                originComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty || item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        translateLocation(item));
                                                }
                                        }
                                });

                originComboBox.setOnAction(
                                event -> updateOriginAvailableQuantity());

                originAvailableQuantityLabel = new Label(
                                "Seleccione el origen para ver la cantidad disponible.");

                originAvailableQuantityLabel.setWrapText(true);

                originAvailableQuantityLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #55705F;");

                destinationField = new TextField();

                destinationField.setPromptText(
                                "Lugar de destino");

                observationsArea = new TextArea();

                observationsArea.setPromptText(
                                "Ingrese observaciones adicionales");

                observationsArea.setPrefRowCount(3);

                observationsArea.setWrapText(true);

                String fieldStyle =
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-color: #C7D0C8;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-font-size: 13px;";

                animalComboBox.setStyle(fieldStyle);
                entryComboBox.setStyle(fieldStyle);
                dateField.setStyle(fieldStyle);
                movementTypeComboBox.setStyle(fieldStyle);
                quantityField.setStyle(fieldStyle);
                originComboBox.setStyle(fieldStyle);
                destinationField.setStyle(fieldStyle);
                observationsArea.setStyle(fieldStyle);

                // =====================================================
                // CREATE SECTIONS
                // =====================================================

                registerSection =
                                createRegisterSection();

                historySection =
                                createHistorySection();

                // =====================================================
                // SECTION NAVIGATION
                // =====================================================

                registerMovementButton =
                                new Button(
                                                "REGISTRAR MOVIMIENTO");

                movementHistoryButton =
                                new Button(
                                                "HISTORIAL DE MOVIMIENTOS");

                registerMovementButton.setPrefHeight(38);
                movementHistoryButton.setPrefHeight(38);

                registerMovementButton.setOnAction(
                                event -> showSection(
                                                registerSection,
                                                registerMovementButton));

                movementHistoryButton.setOnAction(
                                event -> showSection(
                                                historySection,
                                                movementHistoryButton));

                HBox sectionNavigation =
                                new HBox(
                                                10,
                                                registerMovementButton,
                                                movementHistoryButton);

                sectionNavigation.setAlignment(
                                Pos.CENTER);

                // =====================================================
                // SECTION CONTAINER
                // =====================================================

                sectionContainer =
                                new VBox();

                sectionContainer.setMaxWidth(
                                Double.MAX_VALUE);

                showSection(
                                registerSection,
                                registerMovementButton);

                // =====================================================
                // CONTENT
                // =====================================================

                VBox content =
                                new VBox(
                                                22,
                                                header,
                                                sectionNavigation,
                                                sectionContainer);

                content.setAlignment(
                                Pos.TOP_CENTER);

                content.setPadding(
                                new Insets(
                                                25,
                                                35,
                                                35,
                                                35));

                content.setStyle(
                                "-fx-background-color: #F2F0E6;");

                ScrollPane scrollPane =
                                new ScrollPane(
                                                content);

                scrollPane.setFitToWidth(true);

                scrollPane.setStyle(
                                "-fx-background: #F2F0E6;" +
                                                "-fx-background-color: #F2F0E6;");

                return scrollPane;
        }

        // =========================================================
        // REGISTER MOVEMENT SECTION
        // =========================================================

        private VBox createRegisterSection() {

                GridPane form =
                                new GridPane();

                form.setHgap(15);
                form.setVgap(12);

                form.setMaxWidth(
                                Double.MAX_VALUE);

                ColumnConstraints labelColumn =
                                new ColumnConstraints();

                labelColumn.setPercentWidth(28);

                ColumnConstraints fieldColumn =
                                new ColumnConstraints();

                fieldColumn.setPercentWidth(72);

                form.getColumnConstraints().addAll(
                                labelColumn,
                                fieldColumn);

                form.add(
                                createLabel("Animal *"),
                                0,
                                0);

                form.add(
                                animalComboBox,
                                1,
                                0);

                form.add(
                                createLabel("N.º de registro *"),
                                0,
                                1);

                VBox entryBox =
                                new VBox(
                                                5,
                                                entryComboBox,
                                                availableQuantityLabel);

                form.add(
                                entryBox,
                                1,
                                1);

                form.add(
                                createLabel("Fecha *"),
                                0,
                                2);

                form.add(
                                dateField,
                                1,
                                2);

                form.add(
                                createLabel("Tipo de movimiento *"),
                                0,
                                3);

                form.add(
                                movementTypeComboBox,
                                1,
                                3);

                form.add(
                                createLabel("Cantidad *"),
                                0,
                                4);

                form.add(
                                quantityField,
                                1,
                                4);

                form.add(
                                createLabel("Origen *"),
                                0,
                                5);

                VBox originBox =
                                new VBox(
                                                5,
                                                originComboBox,
                                                originAvailableQuantityLabel);

                form.add(
                                originBox,
                                1,
                                5);

                form.add(
                                createLabel("Destino"),
                                0,
                                6);

                form.add(
                                destinationField,
                                1,
                                6);

                form.add(
                                createLabel("Observaciones"),
                                0,
                                7);

                form.add(
                                observationsArea,
                                1,
                                7);

                Label formTitle =
                                new Label(
                                                "Datos del movimiento");

                formTitle.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label helpLabel =
                                new Label(
                                                "Seleccione el animal y su acta de ingreso. La cantidad disponible se muestra antes de registrar el movimiento.");

                helpLabel.setWrapText(true);

                helpLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #68746B;");

                VBox formCard =
                                createCard(
                                                formTitle,
                                                helpLabel,
                                                form);

                // =====================================================
                // ACTION BUTTONS
                // =====================================================

                Button registerButton =
                                new Button(
                                                "REGISTRAR MOVIMIENTO");

                applyPrimaryStyle(
                                registerButton);

                registerButton.setOnAction(
                                event -> saveMovement());

                Button clearButton =
                                new Button(
                                                "LIMPIAR");

                applySecondaryStyle(
                                clearButton);

                clearButton.setOnAction(
                                event -> clearFields());

                HBox buttons =
                                new HBox(
                                                10,
                                                clearButton,
                                                registerButton);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                return new VBox(
                                18,
                                formCard,
                                buttons);
        }

        // =========================================================
        // MOVEMENT HISTORY SECTION
        // =========================================================

        private VBox createHistorySection() {

                table =
                                new TableView<>();

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                TableColumn<Movement, Integer> idColumn =
                                new TableColumn<>("ID");

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "movementId"));

                TableColumn<Movement, Integer> animalColumn =
                                new TableColumn<>("Animal");

                animalColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "animalId"));

                animalColumn.setCellFactory(
                                column -> new TableCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Integer animalId,
                                                        boolean empty) {

                                                super.updateItem(
                                                                animalId,
                                                                empty);

                                                if (empty || animalId == null) {

                                                        setText(null);

                                                        return;
                                                }

                                                Animal animal =
                                                                findAnimalById(
                                                                                animalId);

                                                setText(
                                                                getAnimalDisplayName(
                                                                                animal));
                                        }
                                });

                TableColumn<Movement, Integer> entryColumn =
                                new TableColumn<>(
                                                "N.º de registro");

                entryColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryId"));

                entryColumn.setCellFactory(
                                column -> new TableCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Integer entryId,
                                                        boolean empty) {

                                                super.updateItem(
                                                                entryId,
                                                                empty);

                                                if (empty || entryId == null) {

                                                        setText(
                                                                        "Sin registro");

                                                        return;
                                                }

                                                Entry entry =
                                                                findEntryById(
                                                                                entryId);

                                                if (entry == null) {

                                                        setText(
                                                                        "Sin registro");

                                                        return;
                                                }

                                                setText(
                                                                getEntryDisplayName(
                                                                                entry));
                                        }
                                });

                TableColumn<Movement, String> dateColumn =
                                new TableColumn<>("Fecha");

                dateColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "movementDate"));

                TableColumn<Movement, String> typeColumn =
                                new TableColumn<>("Movimiento");

                typeColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "movementType"));

                typeColumn.setCellFactory(
                                column -> new TableCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        String type,
                                                        boolean empty) {

                                                super.updateItem(
                                                                type,
                                                                empty);

                                                if (empty || type == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        translateMovementType(
                                                                                        type));
                                                }
                                        }
                                });

                TableColumn<Movement, Integer> quantityColumn =
                                new TableColumn<>("Cantidad");

                quantityColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "quantity"));

                TableColumn<Movement, String> originColumn =
                                new TableColumn<>("Origen");

                originColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "originLocation"));

                TableColumn<Movement, String> destinationColumn =
                                new TableColumn<>("Destino");

                destinationColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "destination"));

                table.getColumns().addAll(
                                idColumn,
                                animalColumn,
                                entryColumn,
                                dateColumn,
                                typeColumn,
                                quantityColumn,
                                originColumn,
                                destinationColumn);

                table.setPrefHeight(420);
                table.setMinHeight(420);
                table.setMaxHeight(420);

                loadMovements();

                Label tableTitle =
                                new Label(
                                                "Historial de movimientos");

                tableTitle.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label tableDescription =
                                new Label(
                                                "Consulte los movimientos registrados y el origen de cada animal.");

                tableDescription.setWrapText(true);

                tableDescription.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #68746B;");

                Button refreshButton =
                                new Button(
                                                "ACTUALIZAR");

                applySecondaryStyle(
                                refreshButton);

                refreshButton.setOnAction(
                                event -> loadMovements());

                HBox tableActions =
                                new HBox(
                                                refreshButton);

                tableActions.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox tableCard =
                                createCard(
                                                tableTitle,
                                                tableDescription,
                                                tableActions,
                                                table);

                return new VBox(
                                18,
                                tableCard);
        }

        // =========================================================
        // SECTION NAVIGATION
        // =========================================================

        private void showSection(
                        VBox section,
                        Button activeButton) {

                sectionContainer
                                .getChildren()
                                .setAll(section);

                applySecondaryStyle(
                                registerMovementButton);

                applySecondaryStyle(
                                movementHistoryButton);

                applyPrimaryStyle(
                                activeButton);
        }

        // =========================================================
        // LABEL
        // =========================================================

        private Label createLabel(
                        String text) {

                Label label =
                                new Label(text);

                label.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #34463A;");

                return label;
        }

        // =========================================================
        // CARD
        // =========================================================

        private VBox createCard(
                        javafx.scene.Node... nodes) {

                VBox card =
                                new VBox(
                                                18,
                                                nodes);

                card.setPadding(
                                new Insets(22));

                card.setMaxWidth(
                                Double.MAX_VALUE);

                card.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #D5DBD5;" +
                                                "-fx-border-radius: 16;" +
                                                "-fx-border-width: 1;");

                return card;
        }

        // =========================================================
        // PRIMARY BUTTON
        // =========================================================

        private void applyPrimaryStyle(
                        Button button) {

                button.setPrefHeight(38);

                button.setStyle(
                                "-fx-background-color: #254D3D;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 0 20 0 20;");
        }

        // =========================================================
        // SECONDARY BUTTON
        // =========================================================

        private void applySecondaryStyle(
                        Button button) {

                button.setPrefHeight(38);

                button.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 0 20 0 20;");
        }

        // =========================================================
        // LOAD ANIMALS
        // =========================================================

        private void loadAnimals() {

                ObservableList<Animal> animals =
                                FXCollections.observableArrayList(
                                                animalDAO.list());

                animalComboBox.setItems(
                                animals);

                animalComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Animal animal,
                                                        boolean empty) {

                                                super.updateItem(
                                                                animal,
                                                                empty);

                                                if (empty || animal == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        getAnimalDisplayNameWithQuantity(
                                                                                        animal));
                                                }
                                        }
                                });

                animalComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Animal animal,
                                                        boolean empty) {

                                                super.updateItem(
                                                                animal,
                                                                empty);

                                                if (empty || animal == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        getAnimalDisplayNameWithQuantity(
                                                                                        animal));
                                                }
                                        }
                                });
        }

        // =========================================================
        // LOAD ENTRIES BY ANIMAL
        // =========================================================

        private void loadEntriesByAnimal() {

                Animal selectedAnimal =
                                animalComboBox.getValue();

                entryComboBox
                                .getItems()
                                .clear();

                entryComboBox.setValue(
                                null);

                availableQuantityLabel.setText(
                                "Seleccione un acta para ver la cantidad disponible.");

                originAvailableQuantityLabel.setText(
                                "Seleccione el origen para ver la cantidad disponible.");

                if (selectedAnimal == null) {

                        entryComboBox.setPromptText(
                                        "Seleccione primero un animal");

                        return;
                }

                ObservableList<Entry> allEntries =
                                FXCollections.observableArrayList(
                                                entryDAO.listByAnimal(
                                                                selectedAnimal.getAnimalId()));

                ObservableList<Entry> availableEntries =
                                FXCollections.observableArrayList();

                for (Entry entry : allEntries) {

                        int available =
                                        holdingDAO.getAvailableQuantityByAnimalAndEntry(
                                                        selectedAnimal.getAnimalId(),
                                                        entry.getEntryId());

                        if (available > 0) {

                                availableEntries.add(
                                                entry);
                        }
                }

                entryComboBox.setItems(
                                availableEntries);

                entryComboBox.setPromptText(
                                availableEntries.isEmpty()
                                                ? "Sin cantidades disponibles"
                                                : "Seleccione el número de registro");

                entryComboBox.setCellFactory(
                                listView -> new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Entry entry,
                                                        boolean empty) {

                                                super.updateItem(
                                                                entry,
                                                                empty);

                                                if (empty || entry == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        getEntryDisplayNameWithAvailability(
                                                                                        entry));
                                                }
                                        }
                                });

                entryComboBox.setButtonCell(
                                new ListCell<>() {

                                        @Override
                                        protected void updateItem(
                                                        Entry entry,
                                                        boolean empty) {

                                                super.updateItem(
                                                                entry,
                                                                empty);

                                                if (empty || entry == null) {

                                                        setText(null);

                                                } else {

                                                        setText(
                                                                        getEntryDisplayNameWithAvailability(
                                                                                        entry));
                                                }
                                        }
                                });
        }

        // =========================================================
        // UPDATE AVAILABLE QUANTITY
        // =========================================================

        private void updateAvailableQuantity() {

                Animal animal =
                                animalComboBox.getValue();

                Entry entry =
                                entryComboBox.getValue();

                originAvailableQuantityLabel.setText(
                                "Seleccione el origen para ver la cantidad disponible.");

                if (animal == null || entry == null) {

                        availableQuantityLabel.setText(
                                        "Seleccione un acta para ver la cantidad disponible.");

                        return;
                }

                int available =
                                holdingDAO.getAvailableQuantityByAnimalAndEntry(
                                                animal.getAnimalId(),
                                                entry.getEntryId());

                availableQuantityLabel.setText(
                                "Disponible en este acta: "
                                                + available
                                                + " animal(es).");

                updateOriginAvailableQuantity();
        }

        // =========================================================
        // UPDATE ORIGIN AVAILABLE QUANTITY
        // =========================================================

        private void updateOriginAvailableQuantity() {

                Animal animal =
                                animalComboBox.getValue();

                Entry entry =
                                entryComboBox.getValue();

                String origin =
                                originComboBox.getValue();

                if (animal == null
                                || entry == null
                                || origin == null) {

                        originAvailableQuantityLabel.setText(
                                        "Seleccione el origen para ver la cantidad disponible.");

                        return;
                }

                int available =
                                holdingDAO.getAvailableQuantityByAnimalAndEntryAndLocation(
                                                animal.getAnimalId(),
                                                entry.getEntryId(),
                                                origin);

                originAvailableQuantityLabel.setText(
                                "Disponible en "
                                                + translateLocation(origin)
                                                + ": "
                                                + available
                                                + " animal(es).");
        }

        // =========================================================
        // SAVE MOVEMENT
        // =========================================================

        private void saveMovement() {

                try {

                        if (animalComboBox.getValue() == null
                                        ||
                                        entryComboBox.getValue() == null
                                        ||
                                        dateField.getText().isBlank()
                                        ||
                                        movementTypeComboBox.getValue() == null
                                        ||
                                        quantityField.getText().isBlank()) {

                                showMessage(
                                                "Complete todos los campos obligatorios.");

                                return;
                        }

                        int quantity =
                                        Integer.parseInt(
                                                        quantityField
                                                                        .getText()
                                                                        .trim());

                        if (quantity <= 0) {

                                showMessage(
                                                "La cantidad debe ser mayor que cero.");

                                return;
                        }

                        Animal selectedAnimal =
                                        animalComboBox.getValue();

                        Entry selectedEntry =
                                        entryComboBox.getValue();

                        int animalId =
                                        selectedAnimal.getAnimalId();

                        int entryId =
                                        selectedEntry.getEntryId();

                        String movementType =
                                        movementTypeComboBox.getValue();

                        String origin =
                                        originComboBox.getValue();

                        String destination =
                                        destinationField
                                                        .getText()
                                                        .trim();

                        String movementDate =
                                        dateField
                                                        .getText()
                                                        .trim();

                        String observations =
                                        observationsArea
                                                        .getText()
                                                        .trim();

                        // =================================================
                        // AVAILABLE BY ACT
                        // =================================================

                        int availableByEntry =
                                        holdingDAO.getAvailableQuantityByAnimalAndEntry(
                                                        animalId,
                                                        entryId);

                        if (availableByEntry <= 0) {

                                showMessage(
                                                "El animal seleccionado no tiene cantidad disponible en este acta.");

                                loadEntriesByAnimal();

                                return;
                        }

                        if (quantity > availableByEntry) {

                                showMessage(
                                                "La cantidad solicitada ("
                                                                + quantity
                                                                + ") supera la cantidad disponible en el acta ("
                                                                + availableByEntry
                                                                + ").");

                                return;
                        }

                        // =================================================
                        // ORIGIN
                        // =================================================

                        if (origin == null
                                        || origin.isBlank()) {

                                showMessage(
                                                "Seleccione el lugar de origen.");

                                return;
                        }

                        // =================================================
                        // AVAILABLE BY ORIGIN
                        // =================================================

                        int availableAtOrigin =
                                        holdingDAO.getAvailableQuantityByAnimalAndEntryAndLocation(
                                                        animalId,
                                                        entryId,
                                                        origin);

                        if (availableAtOrigin <= 0) {

                                showMessage(
                                                "No hay cantidad disponible de este animal en "
                                                                + translateLocation(origin)
                                                                + " para el acta seleccionada.");

                                return;
                        }

                        if (quantity > availableAtOrigin) {

                                showMessage(
                                                "La cantidad solicitada ("
                                                                + quantity
                                                                + ") supera la cantidad disponible en "
                                                                + translateLocation(origin)
                                                                + " ("
                                                                + availableAtOrigin
                                                                + ").");

                                return;
                        }

                        // =================================================
                        // TRANSFER
                        // =================================================

                        if ("TRANSFER".equals(movementType)) {

                                if (destination.isBlank()) {

                                        showMessage(
                                                        "Para un traslado debe indicar el lugar de destino.");

                                        return;
                                }

                                String normalizedDestination =
                                                normalizeLocation(
                                                                destination);

                                if (normalizedDestination == null) {

                                        showMessage(
                                                        "Para un traslado, el destino debe ser Permanente o Cuarentena.");

                                        return;
                                }

                                if (origin.equals(
                                                normalizedDestination)) {

                                        showMessage(
                                                        "El lugar de origen y el destino no pueden ser iguales.");

                                        return;
                                }

                                destination =
                                                normalizedDestination;
                        }

                        // =================================================
                        // EXIT
                        // =================================================

                        if ("RELEASE".equals(movementType)
                                        &&
                                        destination.isBlank()) {

                                showMessage(
                                                "Para un egreso debe indicar el destino.");

                                return;
                        }

                        // =================================================
                        // DEATH
                        // =================================================

                        if ("DEATH".equals(movementType)) {

                                destination = null;
                        }

                        // =================================================
                        // CONFIRM
                        // =================================================

                        String confirmationMessage =
                                        buildConfirmationMessage(
                                                        movementType,
                                                        quantity,
                                                        origin,
                                                        destination,
                                                        availableByEntry,
                                                        availableAtOrigin);

                        if (!confirmAction(
                                        confirmationMessage)) {

                                return;
                        }

                        // =================================================
                        // REGISTER THROUGH INVENTORY SERVICE
                        // =================================================

                        switch (movementType) {

                                case "TRANSFER" ->

                                        inventoryService.transfer(
                                                        animalId,
                                                        entryId,
                                                        origin,
                                                        destination,
                                                        quantity,
                                                        movementDate,
                                                        observations);

                                case "DEATH" ->

                                        inventoryService.registerDeath(
                                                        animalId,
                                                        entryId,
                                                        origin,
                                                        quantity,
                                                        movementDate,
                                                        observations);

                                case "RELEASE" ->

                                        inventoryService.registerExit(
                                                        animalId,
                                                        entryId,
                                                        origin,
                                                        quantity,
                                                        destination,
                                                        movementDate,
                                                        observations);

                                default ->

                                        throw new IllegalArgumentException(
                                                        "Tipo de movimiento no válido.");
                        }

                        showInformation(
                                        "Movimiento registrado correctamente.\n\n"
                                                        + "La cantidad disponible, el inventario del animal y el historial del acta fueron actualizados.");

                        clearFields();

                        loadAnimals();

                        loadMovements();

                } catch (NumberFormatException exception) {

                        showMessage(
                                        "La cantidad debe ser un número entero.");

                } catch (Exception exception) {

                        showMessage(
                                        exception.getMessage() == null
                                                        ? "No se pudo registrar el movimiento."
                                                        : exception.getMessage());

                        exception.printStackTrace();
                }
        }

        // =========================================================
        // LOAD MOVEMENTS
        // =========================================================

        private void loadMovements() {

                if (table == null) {

                        return;
                }

                ObservableList<Movement> movements =
                                FXCollections.observableArrayList(
                                                movementDAO.list());

                table.setItems(
                                movements);
        }

        // =========================================================
        // CLEAR FIELDS
        // =========================================================

        private void clearFields() {

                animalComboBox.setValue(
                                null);

                entryComboBox
                                .getItems()
                                .clear();

                entryComboBox.setValue(
                                null);

                entryComboBox.setPromptText(
                                "Seleccione primero un animal");

                availableQuantityLabel.setText(
                                "Seleccione un acta para ver la cantidad disponible.");

                dateField.clear();

                movementTypeComboBox.setValue(
                                null);

                quantityField.clear();

                originComboBox.setValue(
                                null);

                originAvailableQuantityLabel.setText(
                                "Seleccione el origen para ver la cantidad disponible.");

                destinationField.clear();

                observationsArea.clear();
        }

        // =========================================================
        // FIND ANIMAL
        // =========================================================

        private Animal findAnimalById(
                        int animalId) {

                for (Animal animal :
                                animalDAO.list()) {

                        if (animal.getAnimalId()
                                        == animalId) {

                                return animal;
                        }
                }

                return null;
        }

        // =========================================================
        // FIND ENTRY
        // =========================================================

        private Entry findEntryById(
                        int entryId) {

                for (Entry entry :
                                entryDAO.list()) {

                        if (entry.getEntryId()
                                        == entryId) {

                                return entry;
                        }
                }

                return null;
        }

        // =========================================================
        // ANIMAL DISPLAY NAME
        // =========================================================

        private String getAnimalDisplayName(
                        Animal animal) {

                if (animal == null) {

                        return "Animal desconocido";
                }

                String commonName =
                                animal.getCommonName();

                String scientificName =
                                animal.getScientificName();

                if (commonName != null
                                &&
                                !commonName.isBlank()) {

                        if (scientificName != null
                                        &&
                                        !scientificName.isBlank()) {

                                return commonName
                                                + " — "
                                                + scientificName;
                        }

                        return commonName;
                }

                return "Animal N.º "
                                + animal.getAnimalId();
        }

        // =========================================================
        // ANIMAL DISPLAY NAME + QUANTITY
        // =========================================================

        private String getAnimalDisplayNameWithQuantity(
                        Animal animal) {

                return getAnimalDisplayName(animal)
                                + " — Disponible total: "
                                + animal.getCurrentQuantity();
        }

        // =========================================================
        // ENTRY DISPLAY NAME
        // =========================================================

        private String getEntryDisplayName(
                        Entry entry) {

                if (entry == null) {

                        return "Sin registro";
                }

                String recordNumber =
                                entry.getRecordNumber();

                if (recordNumber != null
                                &&
                                !recordNumber.isBlank()) {

                        return "N.º de registro "
                                        + recordNumber;
                }

                return "Ingreso N.º "
                                + entry.getEntryId();
        }

        // =========================================================
        // ENTRY DISPLAY NAME + AVAILABLE QUANTITY
        // =========================================================

        private String getEntryDisplayNameWithAvailability(
                        Entry entry) {

                if (entry == null) {

                        return "Sin registro";
                }

                Animal animal =
                                animalComboBox.getValue();

                if (animal == null) {

                        return getEntryDisplayName(entry);
                }

                int available =
                                holdingDAO.getAvailableQuantityByAnimalAndEntry(
                                                animal.getAnimalId(),
                                                entry.getEntryId());

                return getEntryDisplayName(entry)
                                + " — Disponible: "
                                + available;
        }

        // =========================================================
        // TRANSLATE MOVEMENT TYPE
        // =========================================================

        private String translateMovementType(
                        String movementType) {

                if (movementType == null) {

                        return "";
                }

                return switch (movementType) {

                        case "RELEASE", "EXIT" ->
                                "Egreso / Liberación";

                        case "TRANSFER" ->
                                "Traslado";

                        case "DEATH" ->
                                "Fallecimiento";

                        case "ENTRY" ->
                                "Ingreso";

                        default ->
                                movementType;
                };
        }

        // =========================================================
        // TRANSLATE LOCATION
        // =========================================================

        private String translateLocation(
                        String location) {

                if (location == null) {

                        return "";
                }

                return switch (location) {

                        case AnimalInventoryService.PERMANENT ->
                                "Permanente";

                        case AnimalInventoryService.QUARANTINE ->
                                "Cuarentena";

                        default ->
                                location;
                };
        }

        // =========================================================
        // NORMALIZE LOCATION
        // =========================================================

        private String normalizeLocation(
                        String location) {

                if (location == null) {

                        return null;
                }

                String value =
                                location
                                                .trim()
                                                .toUpperCase();

                return switch (value) {

                        case "PERMANENT",
                                "PERMANENTE" ->

                                AnimalInventoryService.PERMANENT;

                        case "QUARANTINE",
                                "CUARENTENA" ->

                                AnimalInventoryService.QUARANTINE;

                        default ->

                                null;
                };
        }

        // =========================================================
        // CONFIRMATION MESSAGE
        // =========================================================

        private String buildConfirmationMessage(
                        String movementType,
                        int quantity,
                        String origin,
                        String destination,
                        int availableByEntry,
                        int availableAtOrigin) {

                StringBuilder message =
                                new StringBuilder();

                message.append(
                                "Está por registrar el siguiente movimiento:\n\n");

                message.append(
                                "Tipo: ");

                message.append(
                                translateMovementType(
                                                movementType));

                message.append(
                                "\nCantidad: ");

                message.append(
                                quantity);

                message.append(
                                "\nDisponible en el acta: ");

                message.append(
                                availableByEntry);

                message.append(
                                "\nDisponible en el origen: ");

                message.append(
                                availableAtOrigin);

                if (origin != null
                                && !origin.isBlank()) {

                        message.append(
                                        "\nOrigen: ");

                        message.append(
                                        translateLocation(
                                                        origin));
                }

                if (destination != null
                                && !destination.isBlank()) {

                        message.append(
                                        "\nDestino: ");

                        if (AnimalInventoryService.PERMANENT.equals(
                                        destination)
                                        ||
                                        AnimalInventoryService.QUARANTINE.equals(
                                                        destination)) {

                                message.append(
                                                translateLocation(
                                                                destination));

                        } else {

                                message.append(
                                                destination);
                        }
                }

                message.append(
                                "\n\n¿Desea continuar?");

                return message.toString();
        }

        // =========================================================
        // CONFIRM ACTION
        // =========================================================

        private boolean confirmAction(
                        String message) {

                Alert alert =
                                new Alert(
                                                Alert.AlertType.CONFIRMATION);

                alert.setTitle(
                                "Confirmar movimiento");

                alert.setHeaderText(
                                "Confirmación");

                alert.setContentText(
                                message);

                return alert.showAndWait()
                                .filter(
                                                response ->
                                                                response == ButtonType.OK)
                                .isPresent();
        }

        // =========================================================
        // WARNING MESSAGE
        // =========================================================

        private void showMessage(
                        String message) {

                Alert alert =
                                new Alert(
                                                Alert.AlertType.WARNING);

                alert.setTitle(
                                "Tatú Carreta");

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }

        // =========================================================
        // INFORMATION MESSAGE
        // =========================================================

        private void showInformation(
                        String message) {

                Alert alert =
                                new Alert(
                                                Alert.AlertType.INFORMATION);

                alert.setTitle(
                                "Tatú Carreta");

                alert.setHeaderText(
                                null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}