package com.tesina_tatu_carreta.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.EntryDetailDAO;
import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.Entry;
import com.tesina_tatu_carreta.model.EntryDetail;
import com.tesina_tatu_carreta.model.Species;

public class ViewEntry {

        private final EntryDAO entryDAO = new EntryDAO();

        private final EntryDetailDAO detailDAO = new EntryDetailDAO();

        private final SpeciesDAO speciesDAO = new SpeciesDAO();

        private final AnimalDAO animalDAO = new AnimalDAO();

        private final TableView<Entry> entryTable = new TableView<>();

        private final TableView<EntryDetail> detailTable = new TableView<>();

        // =========================================
        // ENTRY DATA
        // =========================================

        private final TextField recordNumberField = new TextField();

        private final TextField entryDateField = new TextField();

        private final TextField originOrganizationField = new TextField();

        private final TextField deliveryRepresentativeField = new TextField();

        private final TextField originField = new TextField();

        private final TextField entryReasonField = new TextField();

        private final TextArea documentationField = new TextArea();

        private final TextArea observationsField = new TextArea();

        // =========================================
        // ANIMAL DATA
        // =========================================

        private final ComboBox<Species> speciesComboBox = new ComboBox<>();

        private final ComboBox<Animal> animalComboBox = new ComboBox<>();

        private final TextField quantityField = new TextField();

        private final TextField sexField = new TextField();

        private final TextField ageField = new TextField();

        private final TextField weightField = new TextField();

        private final TextField entryStatusField = new TextField();

        private final TextArea animalObservationsField = new TextArea();

        private final List<EntryDetail> pendingDetails = new ArrayList<>();

        public void show() {

                Stage stage = new Stage();

                // =========================================
                // HEADER
                // =========================================

                Label breadcrumb = new Label(
                                "Home / Entry Management");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #7A8580;");

                Label title = new Label("Entry Management");

                title.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label subtitle = new Label(
                                "Register and manage animal entries into the reserve");

                subtitle.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #6B756F;");

                VBox header = new VBox(
                                6,
                                breadcrumb,
                                title,
                                subtitle);

                // =========================================
                // ENTRY FIELDS
                // =========================================

                configureFields();

                recordNumberField.setPromptText(
                                "Ex: 001/2026");

                entryDateField.setPromptText(
                                "Ex: 25/08/2026");

                originOrganizationField.setPromptText(
                                "Ex: Environmental Police");

                deliveryRepresentativeField.setPromptText(
                                "Person responsible for delivery");

                originField.setPromptText(
                                "Place of origin");

                entryReasonField.setPromptText(
                                "Reason for entry");

                documentationField.setPromptText(
                                "Submitted documentation...");

                observationsField.setPromptText(
                                "General observations...");

                // =========================================
                // ENTRY FORM
                // =========================================

                GridPane entryForm = createForm();

                entryForm.add(
                                createFieldLabel("Record No."),
                                0,
                                0);

                entryForm.add(
                                createFieldLabel("Entry Date"),
                                1,
                                0);

                entryForm.add(
                                recordNumberField,
                                0,
                                1);

                entryForm.add(
                                entryDateField,
                                1,
                                1);

                entryForm.add(
                                createFieldLabel(
                                                "Origin Organization"),
                                0,
                                2);

                entryForm.add(
                                createFieldLabel(
                                                "Delivery Representative"),
                                1,
                                2);

                entryForm.add(
                                originOrganizationField,
                                0,
                                3);

                entryForm.add(
                                deliveryRepresentativeField,
                                1,
                                3);

                entryForm.add(
                                createFieldLabel("Origin"),
                                0,
                                4);

                entryForm.add(
                                createFieldLabel("Entry Reason"),
                                1,
                                4);

                entryForm.add(
                                originField,
                                0,
                                5);

                entryForm.add(
                                entryReasonField,
                                1,
                                5);

                VBox documentationBox = new VBox(
                                5,
                                createFieldLabel("Documentation"),
                                documentationField);

                VBox observationsBox = new VBox(
                                5,
                                createFieldLabel(
                                                "Observations"),
                                observationsField);

                GridPane.setColumnSpan(
                                documentationBox,
                                2);

                GridPane.setColumnSpan(
                                observationsBox,
                                2);

                // =========================================
                // ENTRY CARD
                // =========================================

                Label entryTitle = createCardTitle(
                                "Entry Information");

                VBox entryCard = createCard(
                                entryTitle,
                                entryForm,
                                documentationBox,
                                observationsBox);

                // =========================================
                // ANIMAL DATA
                // =========================================

                speciesComboBox.setPromptText(
                                "Select a species");

                animalComboBox.setPromptText(
                                "Select an animal");

                quantityField.setPromptText(
                                "Ex: 2");

                sexField.setPromptText(
                                "Male / Female / Mixed");

                ageField.setPromptText(
                                "Ex: Adult");

                weightField.setPromptText(
                                "Ex: 2.5");

                entryStatusField.setPromptText(
                                "Condition upon entry");

                animalObservationsField.setPromptText(
                                "Animal observations...");

                loadSpecies();

                speciesComboBox.setOnAction(e -> {

                        Species species = speciesComboBox.getValue();

                        loadAnimalsBySpecies(
                                        species);
                });

                // =========================================
                // NEW ANIMAL BUTTON
                // =========================================

                Button newAnimalButton = new Button("NEW ANIMAL");

                applySecondaryStyle(
                                newAnimalButton);

                newAnimalButton.setOnAction(
                                e -> showNewAnimalWindow());

                HBox animalRow = new HBox(
                                10,
                                animalComboBox,
                                newAnimalButton);

                HBox.setHgrow(
                                animalComboBox,
                                Priority.ALWAYS);

                // =========================================
                // ANIMAL FORM
                // =========================================

                GridPane animalForm = createForm();

                animalForm.add(
                                createFieldLabel("Species"),
                                0,
                                0);

                animalForm.add(
                                createFieldLabel("Animal"),
                                1,
                                0);

                animalForm.add(
                                speciesComboBox,
                                0,
                                1);

                animalForm.add(
                                animalRow,
                                1,
                                1);

                animalForm.add(
                                createFieldLabel("Quantity"),
                                0,
                                2);

                animalForm.add(
                                createFieldLabel("Sex"),
                                1,
                                2);

                animalForm.add(
                                quantityField,
                                0,
                                3);

                animalForm.add(
                                sexField,
                                1,
                                3);

                animalForm.add(
                                createFieldLabel("Age"),
                                0,
                                4);

                animalForm.add(
                                createFieldLabel("Weight"),
                                1,
                                4);

                animalForm.add(
                                ageField,
                                0,
                                5);

                animalForm.add(
                                weightField,
                                1,
                                5);

                animalForm.add(
                                createFieldLabel(
                                                "Condition Upon Entry"),
                                0,
                                6);

                animalForm.add(
                                entryStatusField,
                                0,
                                7);

                VBox animalObservationsBox = new VBox(
                                5,
                                createFieldLabel(
                                                "Animal Observations"),
                                animalObservationsField);

                Button addAnimalButton = new Button(
                                "ADD ANIMAL TO ENTRY");

                applyPrimaryStyle(
                                addAnimalButton);

                addAnimalButton.setOnAction(
                                e -> addPendingDetail());

                Label animalTitle = createCardTitle(
                                "Animal Information");

                VBox animalCard = createCard(
                                animalTitle,
                                animalForm,
                                animalObservationsBox,
                                addAnimalButton);

                // =========================================
                // DETAILS TABLE
                // =========================================

                configureDetailsTable();

                detailTable.setPrefHeight(220);

                Label detailsTitle = createCardTitle(
                                "Animals Added to Entry");

                VBox detailsCard = createCard(
                                detailsTitle,
                                detailTable);

                // =========================================
                // MAIN BUTTONS
                // =========================================

                Button clearButton = new Button("CLEAR");

                Button deleteButton = new Button("DELETE");

                Button editButton = new Button("EDIT");

                Button saveButton = new Button("SAVE ENTRY");

                Button backButton = new Button("BACK");

                applySecondaryStyle(
                                clearButton);

                applyDeleteStyle(
                                deleteButton);

                applySecondaryStyle(
                                editButton);

                applyPrimaryStyle(
                                saveButton);

                applySecondaryStyle(
                                backButton);

                clearButton.setOnAction(
                                e -> clearAll());

                // =========================================
                // CONFIGURE ENTRY TABLE
                // =========================================

                configureEntryTable();

                loadEntries();

                entryTable.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (
                                                                observable,
                                                                previous,
                                                                selected) -> {

                                                        if (selected != null) {

                                                                loadEntryData(
                                                                                selected);

                                                                loadDetails(
                                                                                selected
                                                                                                .getEntryId());
                                                        }
                                                });

                // =========================================
                // SAVE
                // =========================================

                saveButton.setOnAction(e -> {

                        if (!isEntryDataValid()) {

                                showMessage(
                                                Alert.AlertType.WARNING,
                                                "Please complete the required entry information.");

                                return;
                        }

                        if (pendingDetails.isEmpty()) {

                                showMessage(
                                                Alert.AlertType.WARNING,
                                                "You must add at least one animal.");

                                return;
                        }

                        Entry entry = createEntryFromForm();

                        entryDAO.add(
                                        entry);

                        List<Entry> entries = entryDAO.list();

                        if (!entries.isEmpty()) {

                                Entry lastEntry = entries.get(0);

                                for (EntryDetail detail : pendingDetails) {

                                        detail.setEntryId(
                                                        lastEntry.getEntryId());

                                        detailDAO.add(
                                                        detail);
                                }
                        }

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Entry saved successfully.");

                        clearAll();

                        loadEntries();
                });

                // =========================================
                // EDIT
                // =========================================

                editButton.setOnAction(e -> {

                        Entry selected = entryTable
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (selected == null) {

                                showMessage(
                                                Alert.AlertType.WARNING,
                                                "Select an entry to edit.");

                                return;
                        }

                        updateEntryFromForm(
                                        selected);

                        entryDAO.update(
                                        selected);

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Entry updated successfully.");

                        clearAll();

                        loadEntries();
                });

                // =========================================
                // DELETE
                // =========================================

                deleteButton.setOnAction(e -> {

                        Entry selected = entryTable
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (selected == null) {

                                showMessage(
                                                Alert.AlertType.WARNING,
                                                "Select an entry to delete.");

                                return;
                        }

                        entryDAO.delete(
                                        selected.getEntryId());

                        showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Entry deleted successfully.");

                        clearAll();

                        loadEntries();
                });

                // =========================================
                // BUTTONS
                // =========================================

                HBox buttons = new HBox(
                                10,
                                clearButton,
                                deleteButton,
                                editButton,
                                saveButton);

                buttons.setAlignment(
                                Pos.CENTER_RIGHT);

                // =========================================
                // REGISTERED ENTRIES CARD
                // =========================================

                Label registeredTitle = createCardTitle(
                                "Registered Entries");

                VBox entriesCard = createCard(
                                registeredTitle,
                                entryTable);

                entryTable.setPrefHeight(280);

                // =========================================
                // BACK
                // =========================================

                backButton.setOnAction(
                                e -> stage.close());

                HBox backContainer = new HBox(backButton);

                backContainer.setAlignment(
                                Pos.CENTER);

                // =========================================
                // CONTENT
                // =========================================

                VBox content = new VBox(
                                25,
                                header,
                                entryCard,
                                animalCard,
                                detailsCard,
                                buttons,
                                entriesCard,
                                backContainer);

                content.setAlignment(
                                Pos.TOP_CENTER);

                content.setPadding(
                                new Insets(
                                                25,
                                                35,
                                                35,
                                                35));

                content.setStyle(
                                "-fx-background-color: #F4F1E8;");

                // =========================================
                // SCROLL
                // =========================================

                ScrollPane scroll = new ScrollPane(content);

                scroll.setFitToWidth(true);

                scroll.setStyle(
                                "-fx-background: #F4F1E8;" +
                                                "-fx-background-color: #F4F1E8;");

                // =========================================
                // SCENE
                // =========================================

                Scene scene = new Scene(
                                scroll,
                                1100,
                                750);

                stage.setTitle(
                                "Tatú Carreta - Entry Management");

                stage.setMinWidth(900);

                stage.setMinHeight(650);

                stage.setScene(
                                scene);

                stage.setMaximized(true);

                stage.show();
        }

        // =========================================
        // CONFIGURE FIELDS
        // =========================================

        private void configureFields() {

                String fieldStyle = "-fx-background-radius: 8;" +
                                "-fx-border-radius: 8;" +
                                "-fx-border-color: #D1D8D2;" +
                                "-fx-padding: 8;";

                recordNumberField.setStyle(fieldStyle);
                entryDateField.setStyle(fieldStyle);
                originOrganizationField.setStyle(fieldStyle);
                deliveryRepresentativeField.setStyle(fieldStyle);
                originField.setStyle(fieldStyle);
                entryReasonField.setStyle(fieldStyle);
                documentationField.setStyle(fieldStyle);
                observationsField.setStyle(fieldStyle);
                speciesComboBox.setStyle(fieldStyle);
                animalComboBox.setStyle(fieldStyle);
                quantityField.setStyle(fieldStyle);
                sexField.setStyle(fieldStyle);
                ageField.setStyle(fieldStyle);
                weightField.setStyle(fieldStyle);
                entryStatusField.setStyle(fieldStyle);
                animalObservationsField.setStyle(fieldStyle);

                recordNumberField.setPrefHeight(38);
                entryDateField.setPrefHeight(38);
                originOrganizationField.setPrefHeight(38);
                deliveryRepresentativeField.setPrefHeight(38);
                originField.setPrefHeight(38);
                entryReasonField.setPrefHeight(38);

                speciesComboBox.setPrefHeight(38);
                animalComboBox.setPrefHeight(38);

                quantityField.setPrefHeight(38);
                sexField.setPrefHeight(38);
                ageField.setPrefHeight(38);
                weightField.setPrefHeight(38);
                entryStatusField.setPrefHeight(38);

                documentationField.setPrefRowCount(3);
                documentationField.setWrapText(true);

                observationsField.setPrefRowCount(3);
                observationsField.setWrapText(true);

                animalObservationsField.setPrefRowCount(3);
                animalObservationsField.setWrapText(true);
        }

        // =========================================
        // CREATE FORM
        // =========================================

        private GridPane createForm() {

                GridPane form = new GridPane();

                form.setHgap(20);

                form.setVgap(12);

                return form;
        }

        // =========================================
        // FIELD LABEL
        // =========================================

        private Label createFieldLabel(
                        String text) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #445149;");

                return label;
        }

        // =========================================
        // CARD TITLE
        // =========================================

        private Label createCardTitle(
                        String text) {

                Label title = new Label(text);

                title.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                return title;
        }

        // =========================================
        // CREATE CARD
        // =========================================

        private VBox createCard(
                        javafx.scene.Node... nodes) {

                VBox card = new VBox(
                                18,
                                nodes);

                card.setPadding(
                                new Insets(25));

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #D8DED9;" +
                                                "-fx-border-radius: 16;");

                return card;
        }

        // =========================================
        // PRIMARY STYLE
        // =========================================

        private void applyPrimaryStyle(
                        Button button) {

                button.setPrefHeight(38);

                button.setStyle(
                                "-fx-background-color: #254D3D;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");
        }

        // =========================================
        // SECONDARY STYLE
        // =========================================

        private void applySecondaryStyle(
                        Button button) {

                button.setPrefHeight(38);

                button.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");
        }

        // =========================================
        // DELETE STYLE
        // =========================================

        private void applyDeleteStyle(
                        Button button) {

                button.setPrefHeight(38);

                button.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #A34A4A;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #E2C5C5;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");
        }

        // =========================================
        // CONFIGURE DETAILS TABLE
        // =========================================

        private void configureDetailsTable() {

                detailTable.getColumns().clear();

                TableColumn<EntryDetail, Integer> quantityColumn = new TableColumn<>("Quantity");

                quantityColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "quantity"));

                TableColumn<EntryDetail, String> sexColumn = new TableColumn<>("Sex");

                sexColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "sex"));

                TableColumn<EntryDetail, String> ageColumn = new TableColumn<>("Age");

                ageColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "age"));

                TableColumn<EntryDetail, Double> weightColumn = new TableColumn<>("Weight");

                weightColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "weight"));

                TableColumn<EntryDetail, String> statusColumn = new TableColumn<>("Status");

                statusColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryStatus"));

                detailTable.getColumns().add(quantityColumn);
                detailTable.getColumns().add(sexColumn);
                detailTable.getColumns().add(ageColumn);
                detailTable.getColumns().add(weightColumn);
                detailTable.getColumns().add(statusColumn);

                detailTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);
        }

        // =========================================
        // CONFIGURE ENTRY TABLE
        // =========================================

        private void configureEntryTable() {

                entryTable.getColumns().clear();

                TableColumn<Entry, Integer> idColumn = new TableColumn<>("ID");

                idColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryId"));

                TableColumn<Entry, String> recordColumn = new TableColumn<>("Record No.");

                recordColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "recordNumber"));

                TableColumn<Entry, String> dateColumn = new TableColumn<>("Date");

                dateColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryDate"));

                TableColumn<Entry, String> organizationColumn = new TableColumn<>("Organization");

                organizationColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "originOrganization"));

                TableColumn<Entry, String> originColumn = new TableColumn<>("Origin");

                originColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "origin"));

                TableColumn<Entry, String> reasonColumn = new TableColumn<>("Reason");

                reasonColumn.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "entryReason"));

                entryTable.getColumns().add(idColumn);
                entryTable.getColumns().add(recordColumn);
                entryTable.getColumns().add(dateColumn);
                entryTable.getColumns().add(organizationColumn);
                entryTable.getColumns().add(originColumn);
                entryTable.getColumns().add(reasonColumn);

                entryTable.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);
        }

        // =========================================
        // NEW ANIMAL
        // =========================================

        private void showNewAnimalWindow() {

                Species species = speciesComboBox.getValue();

                if (species == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "First select a species.");

                        return;
                }

                Stage animalStage = new Stage();

                Label title = new Label("New Animal");

                title.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label nameLabel = createFieldLabel(
                                "Common Name");

                TextField nameField = new TextField();

                nameField.setPromptText(
                                "Ex: Caracara");

                Button saveButton = new Button("SAVE");

                applyPrimaryStyle(
                                saveButton);

                saveButton.setOnAction(e -> {

                        String name = nameField.getText().trim();

                        if (name.isBlank()) {

                                showMessage(
                                                Alert.AlertType.WARNING,
                                                "Enter the animal name.");

                                return;
                        }

                        Animal animal = new Animal();

                        animal.setSpeciesId(
                                        species.getSpeciesId());

                        animal.setCommonName(
                                        name);

                        animal.setScientificName(
                                        "");

                        animal.setCurrentQuantity(0);

                        animal.setOrigin("");

                        animal.setStatus("Active");

                        animalDAO.add(
                                        animal);

                        Animal created = findCreatedAnimal(
                                        species.getSpeciesId(),
                                        name);

                        loadAnimalsBySpecies(
                                        species);

                        if (created != null) {

                                animalComboBox.setValue(
                                                created);
                        }

                        animalStage.close();
                });

                VBox content = new VBox(
                                15,
                                title,
                                nameLabel,
                                nameField,
                                saveButton);

                content.setPadding(
                                new Insets(25));

                content.setAlignment(
                                Pos.CENTER);

                content.setStyle(
                                "-fx-background-color: #F4F1E8;");

                Scene scene = new Scene(
                                content,
                                400,
                                250);

                animalStage.setTitle(
                                "Tatú Carreta - New Animal");

                animalStage.setScene(
                                scene);

                animalStage.show();
        }

        // =========================================
        // FIND CREATED ANIMAL
        // =========================================

        private Animal findCreatedAnimal(
                        int speciesId,
                        String commonName) {

                for (Animal animal : animalDAO.list()) {

                        if (animal.getSpeciesId() == speciesId
                                        &&
                                        animal.getCommonName()
                                                        .equalsIgnoreCase(
                                                                        commonName)) {

                                return animal;
                        }
                }

                return null;
        }

        // =========================================
        // LOAD SPECIES
        // =========================================

        private void loadSpecies() {

                speciesComboBox.setItems(
                                FXCollections.observableArrayList(
                                                speciesDAO.list()));
        }

        // =========================================
        // LOAD ANIMALS BY SPECIES
        // =========================================

        private void loadAnimalsBySpecies(
                        Species species) {

                animalComboBox.getItems().clear();

                animalComboBox.setValue(null);

                if (species == null) {

                        return;
                }

                ObservableList<Animal> animals = FXCollections.observableArrayList();

                for (Animal animal : animalDAO.list()) {

                        if (animal.getSpeciesId() == species.getSpeciesId()) {

                                animals.add(
                                                animal);
                        }
                }

                animalComboBox.setItems(
                                animals);
        }

        // =========================================
        // ADD PENDING DETAIL
        // =========================================

        private void addPendingDetail() {

                Animal animal = animalComboBox.getValue();

                if (animal == null) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Select or create an animal.");

                        return;
                }

                if (quantityField
                                .getText()
                                .isBlank()) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Enter the quantity.");

                        return;
                }

                try {

                        int quantity = Integer.parseInt(
                                        quantityField
                                                        .getText()
                                                        .trim());

                        double weight = 0;

                        if (!weightField
                                        .getText()
                                        .isBlank()) {

                                weight = Double.parseDouble(
                                                weightField
                                                                .getText()
                                                                .trim());
                        }

                        EntryDetail detail = new EntryDetail();

                        detail.setAnimalId(
                                        animal.getAnimalId());

                        detail.setQuantity(
                                        quantity);

                        detail.setSex(
                                        sexField.getText());

                        detail.setAge(
                                        ageField.getText());

                        detail.setWeight(
                                        weight);

                        detail.setEntryStatus(
                                        entryStatusField.getText());

                        detail.setObservations(
                                        animalObservationsField
                                                        .getText());

                        pendingDetails.add(
                                        detail);

                        detailTable.setItems(
                                        FXCollections.observableArrayList(
                                                        pendingDetails));

                        clearDetail();

                } catch (NumberFormatException e) {

                        showMessage(
                                        Alert.AlertType.WARNING,
                                        "Quantity and weight must be numbers.");
                }
        }

        // =========================================
        // LOAD ENTRIES
        // =========================================

        private void loadEntries() {

                entryTable.setItems(
                                FXCollections.observableArrayList(
                                                entryDAO.list()));
        }

        // =========================================
        // LOAD DETAILS
        // =========================================

        private void loadDetails(
                        int entryId) {

                detailTable.setItems(
                                FXCollections.observableArrayList(
                                                detailDAO.listByEntry(
                                                                entryId)));
        }

        // =========================================
        // LOAD ENTRY DATA
        // =========================================

        private void loadEntryData(
                        Entry entry) {

                recordNumberField.setText(
                                entry.getRecordNumber());

                entryDateField.setText(
                                entry.getEntryDate());

                originOrganizationField.setText(
                                entry.getOrigin());

                deliveryRepresentativeField.setText(
                                entry.getDeliveryResponsible());

                originField.setText(
                                entry.getOrigin());

                entryReasonField.setText(
                                entry.getEntryReason());

                documentationField.setText(
                                entry.getDocumentation());

                observationsField.setText(
                                entry.getObservations());
        }

        // =========================================
        // CREATE ENTRY
        // =========================================

        private Entry createEntryFromForm() {

                Entry entry = new Entry();

                entry.setRecordNumber(
                                recordNumberField.getText());

                entry.setEntryDate(
                                entryDateField.getText());

                entry.setOrigin(
                                originOrganizationField.getText());

                entry.setDeliveryResponsible(
                                deliveryRepresentativeField.getText());

                entry.setOrigin(
                                originField.getText());

                entry.setEntryReason(
                                entryReasonField.getText());

                entry.setDocumentation(
                                documentationField.getText());

                entry.setObservations(
                                observationsField.getText());

                return entry;
        }

        // =========================================
        // UPDATE ENTRY
        // =========================================

        private void updateEntryFromForm(
                        Entry entry) {

                entry.setRecordNumber(
                                recordNumberField.getText());

                entry.setEntryDate(
                                entryDateField.getText());

                entry.setOrigin(
                                originOrganizationField.getText());

                entry.setDeliveryResponsible(
                                deliveryRepresentativeField.getText());

                entry.setOrigin(
                                originField.getText());

                entry.setEntryReason(
                                entryReasonField.getText());

                entry.setDocumentation(
                                documentationField.getText());

                entry.setObservations(
                                observationsField.getText());
        }

        // =========================================
        // VALIDATE DATA
        // =========================================

        private boolean isEntryDataValid() {

                return !recordNumberField
                                .getText()
                                .isBlank()

                                && !entryDateField
                                                .getText()
                                                .isBlank()

                                && !originOrganizationField
                                                .getText()
                                                .isBlank()

                                && !originField
                                                .getText()
                                                .isBlank()

                                && !entryReasonField
                                                .getText()
                                                .isBlank();
        }

        // =========================================
        // CLEAR DETAIL
        // =========================================

        private void clearDetail() {

                speciesComboBox.setValue(null);

                animalComboBox.getItems().clear();

                animalComboBox.setValue(null);

                quantityField.clear();

                sexField.clear();

                ageField.clear();

                weightField.clear();

                entryStatusField.clear();

                animalObservationsField.clear();
        }

        // =========================================
        // CLEAR EVERYTHING
        // =========================================

        private void clearAll() {

                recordNumberField.clear();

                entryDateField.clear();

                originOrganizationField.clear();

                deliveryRepresentativeField.clear();

                originField.clear();

                entryReasonField.clear();

                documentationField.clear();

                observationsField.clear();

                pendingDetails.clear();

                detailTable.getItems().clear();

                clearDetail();

                entryTable
                                .getSelectionModel()
                                .clearSelection();
        }

        // =========================================
        // MESSAGES
        // =========================================

        private void showMessage(
                        Alert.AlertType type,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(
                                "Tatú Carreta");

                alert.setHeaderText(null);

                alert.setContentText(
                                message);

                alert.showAndWait();
        }
}