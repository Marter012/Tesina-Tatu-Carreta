package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.EnclosureDAO;
import com.tesina_tatu_carreta.dao.PermanentEnclosureDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.Enclosure;
import com.tesina_tatu_carreta.model.PermanentEnclosure;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class ViewPermanentEnclosure {

    private final PermanentEnclosureDAO permanentEnclosureDAO;
    private final AnimalDAO animalDAO;
    private final EnclosureDAO enclosureDAO;

    private final BorderPane root;

    private final TableView<PermanentEnclosure> table;

    private final ComboBox<Animal> animalComboBox;
    private final TextField quantityField;
    private final ComboBox<String> locationTypeComboBox;
    private final ComboBox<Enclosure> enclosureComboBox;
    private final DatePicker entryDatePicker;
    private final ComboBox<String> statusComboBox;
    private final TextArea observationsArea;

    private PermanentEnclosure selectedPermanentEnclosure;

    private final ObservableList<PermanentEnclosure> permanentEnclosures;

    public ViewPermanentEnclosure() {

        permanentEnclosureDAO = new PermanentEnclosureDAO();
        animalDAO = new AnimalDAO();
        enclosureDAO = new EnclosureDAO();

        root = new BorderPane();

        table = new TableView<>();

        animalComboBox = new ComboBox<>();
        quantityField = new TextField();
        locationTypeComboBox = new ComboBox<>();
        enclosureComboBox = new ComboBox<>();
        entryDatePicker = new DatePicker();
        statusComboBox = new ComboBox<>();
        observationsArea = new TextArea();

        permanentEnclosures =
                FXCollections.observableArrayList();

        configureView();

        loadAnimals();
        loadEnclosures();
        loadPermanentEnclosures();

        clearForm();
    }

    public Parent getView() {
        return root;
    }

    public Parent createView() {
        return root;
    }

    private void configureView() {

        root.setPadding(
                new Insets(20)
        );

        VBox mainContainer =
                new VBox(20);

        mainContainer.setPadding(
                new Insets(10)
        );

        Label title =
                new Label(
                        "Gestión de recintos permanentes"
                );

        title.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
        );

        Label formTitle =
                new Label(
                        "Información del recinto permanente"
                );

        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        GridPane form =
                createForm();

        HBox buttons =
                createButtons();

        VBox formContainer =
                new VBox(15);

        formContainer.getChildren().addAll(
                formTitle,
                form,
                buttons
        );

        VBox tableContainer =
                createTableContainer();

        mainContainer.getChildren().addAll(
                title,
                formContainer,
                tableContainer
        );

        root.setCenter(
                mainContainer
        );

        configureLocationType();
        configureStatus();
        configureTableSelection();
    }

    private GridPane createForm() {

        GridPane form =
                new GridPane();

        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(
                new Insets(10)
        );

        Label animalLabel =
                new Label("Animal:");

        Label quantityLabel =
                new Label("Cantidad:");

        Label locationTypeLabel =
                new Label("Tipo de ubicación:");

        Label enclosureLabel =
                new Label("Recinto:");

        Label entryDateLabel =
                new Label("Fecha de ingreso:");

        Label statusLabel =
                new Label("Estado:");

        Label observationsLabel =
                new Label("Observaciones:");

        animalComboBox.setPrefWidth(250);

        quantityField.setPromptText(
                "Ingrese la cantidad"
        );

        quantityField.setPrefWidth(250);

        locationTypeComboBox.setPrefWidth(250);

        enclosureComboBox.setPrefWidth(250);

        entryDatePicker.setPrefWidth(250);

        statusComboBox.setPrefWidth(250);

        observationsArea.setPrefRowCount(4);
        observationsArea.setPrefWidth(250);
        observationsArea.setWrapText(true);

        form.add(
                animalLabel,
                0,
                0
        );

        form.add(
                animalComboBox,
                1,
                0
        );

        form.add(
                quantityLabel,
                0,
                1
        );

        form.add(
                quantityField,
                1,
                1
        );

        form.add(
                locationTypeLabel,
                0,
                2
        );

        form.add(
                locationTypeComboBox,
                1,
                2
        );

        form.add(
                enclosureLabel,
                0,
                3
        );

        form.add(
                enclosureComboBox,
                1,
                3
        );

        form.add(
                entryDateLabel,
                0,
                4
        );

        form.add(
                entryDatePicker,
                1,
                4
        );

        form.add(
                statusLabel,
                0,
                5
        );

        form.add(
                statusComboBox,
                1,
                5
        );

        form.add(
                observationsLabel,
                0,
                6
        );

        form.add(
                observationsArea,
                1,
                6
        );

        return form;
    }

    private HBox createButtons() {

        Button addButton =
                new Button("Agregar");

        Button updateButton =
                new Button("Actualizar");

        Button deleteButton =
                new Button("Eliminar");

        Button clearButton =
                new Button("Limpiar");

        Button refreshButton =
                new Button("Actualizar lista");

        addButton.setOnAction(
                event -> addPermanentEnclosure()
        );

        updateButton.setOnAction(
                event -> updatePermanentEnclosure()
        );

        deleteButton.setOnAction(
                event -> deletePermanentEnclosure()
        );

        clearButton.setOnAction(
                event -> clearForm()
        );

        refreshButton.setOnAction(
                event -> loadPermanentEnclosures()
        );

        HBox buttons =
                new HBox(10);

        buttons.setAlignment(
                Pos.CENTER_LEFT
        );

        buttons.getChildren().addAll(
                addButton,
                updateButton,
                deleteButton,
                clearButton,
                refreshButton
        );

        return buttons;
    }

    private VBox createTableContainer() {

        Label tableTitle =
                new Label(
                        "Recintos permanentes registrados"
                );

        tableTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        configureTable();

        VBox container =
                new VBox(10);

        container.getChildren().addAll(
                tableTitle,
                table
        );

        VBox.setVgrow(
                table,
                javafx.scene.layout.Priority.ALWAYS
        );

        return container;
    }

    private void configureTable() {

        table.setItems(
                permanentEnclosures
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<PermanentEnclosure, Number> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getPermanentEnclosureId()
                        )
        );

        TableColumn<PermanentEnclosure, String> animalColumn =
                new TableColumn<>("Animal");

        animalColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getAnimalName()
                        )
        );

        TableColumn<PermanentEnclosure, Number> quantityColumn =
                new TableColumn<>("Cantidad");

        quantityColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getQuantity()
                        )
        );

        TableColumn<PermanentEnclosure, String> locationColumn =
                new TableColumn<>("Tipo de ubicación");

        locationColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                translateLocationType(
                                        data.getValue()
                                                .getLocationType()
                                )
                        )
        );

        TableColumn<PermanentEnclosure, String> enclosureColumn =
                new TableColumn<>("Recinto");

        enclosureColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getEnclosureName()
                        )
        );

        TableColumn<PermanentEnclosure, String> dateColumn =
                new TableColumn<>("Fecha de ingreso");

        dateColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getEnclosureEntryDate()
                        )
        );

        TableColumn<PermanentEnclosure, String> statusColumn =
                new TableColumn<>("Estado");

        statusColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                translateStatus(
                                        data.getValue()
                                                .getStatus()
                                )
                        )
        );

        TableColumn<PermanentEnclosure, String> observationsColumn =
                new TableColumn<>("Observaciones");

        observationsColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getObservations()
                        )
        );

        table.getColumns().clear();

        table.getColumns().addAll(
                idColumn,
                animalColumn,
                quantityColumn,
                locationColumn,
                enclosureColumn,
                dateColumn,
                statusColumn,
                observationsColumn
        );
    }

    private void configureTableSelection() {

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue != null) {

                                loadSelectedPermanentEnclosure(
                                        newValue
                                );
                            }
                        }
                );
    }

    private void configureLocationType() {

        locationTypeComboBox.setItems(
                FXCollections.observableArrayList(
                        "Enclosure",
                        "Open field",
                        "Other"
                )
        );

        locationTypeComboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            String value) {

                        if (value == null) {
                            return "";
                        }

                        return translateLocationType(
                                value
                        );
                    }

                    @Override
                    public String fromString(
                            String value) {

                        if (value == null) {
                            return null;
                        }

                        return getLocationTypeInternalValue(
                                value
                        );
                    }
                }
        );

        locationTypeComboBox.valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue == null) {

                                enclosureComboBox.setDisable(
                                        true
                                );

                                return;
                            }

                            if ("Enclosure".equals(newValue)) {

                                enclosureComboBox.setDisable(
                                        false
                                );

                            } else {

                                enclosureComboBox.setDisable(
                                        true
                                );

                                enclosureComboBox
                                        .getSelectionModel()
                                        .clearSelection();
                            }
                        }
                );
    }

    private void configureStatus() {

        statusComboBox.setItems(
                FXCollections.observableArrayList(
                        "Active",
                        "Inactive"
                )
        );

        statusComboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            String value) {

                        if (value == null) {
                            return "";
                        }

                        return translateStatus(
                                value
                        );
                    }

                    @Override
                    public String fromString(
                            String value) {

                        if (value == null) {
                            return null;
                        }

                        return getStatusInternalValue(
                                value
                        );
                    }
                }
        );
    }

    private String translateLocationType(
            String value) {

        return switch (value) {

            case "Enclosure" ->
                    "Recinto";

            case "Open field" ->
                    "Campo abierto";

            case "Other" ->
                    "Otro";

            default ->
                    value;
        };
    }

    private String getLocationTypeInternalValue(
            String value) {

        return switch (value) {

            case "Recinto" ->
                    "Enclosure";

            case "Campo abierto" ->
                    "Open field";

            case "Otro" ->
                    "Other";

            default ->
                    value;
        };
    }

    private String translateStatus(
            String value) {

        return switch (value) {

            case "Active" ->
                    "Activo";

            case "Inactive" ->
                    "Inactivo";

            default ->
                    value;
        };
    }

    private String getStatusInternalValue(
            String value) {

        return switch (value) {

            case "Activo" ->
                    "Active";

            case "Inactivo" ->
                    "Inactive";

            default ->
                    value;
        };
    }

    private void loadAnimals() {

        try {

            animalComboBox.setItems(
                    FXCollections.observableArrayList(
                            animalDAO.list()
                    )
            );

        } catch (Exception exception) {

            showError(
                    "Cargar animales",
                    "No se pudieron cargar los animales.",
                    exception.getMessage()
            );
        }
    }

    private void loadEnclosures() {

        try {

            enclosureComboBox.setItems(
                    FXCollections.observableArrayList(
                            enclosureDAO.list()
                    )
            );

        } catch (Exception exception) {

            showError(
                    "Cargar recintos",
                    "No se pudieron cargar los recintos.",
                    exception.getMessage()
            );
        }
    }

    private void loadPermanentEnclosures() {

        try {

            permanentEnclosures.setAll(
                    permanentEnclosureDAO.list()
            );

        } catch (Exception exception) {

            showError(
                    "Cargar recintos permanentes",
                    "No se pudieron cargar los recintos permanentes.",
                    exception.getMessage()
            );
        }
    }

    private void loadSelectedPermanentEnclosure(
            PermanentEnclosure permanentEnclosure) {

        selectedPermanentEnclosure =
                permanentEnclosure;

        selectAnimal(
                permanentEnclosure.getAnimalId()
        );

        quantityField.setText(
                String.valueOf(
                        permanentEnclosure.getQuantity()
                )
        );

        locationTypeComboBox.setValue(
                permanentEnclosure.getLocationType()
        );

        if (permanentEnclosure.getEnclosureId() != null) {

            selectEnclosure(
                    permanentEnclosure.getEnclosureId()
            );

        } else {

            enclosureComboBox
                    .getSelectionModel()
                    .clearSelection();
        }

        String date =
                permanentEnclosure
                        .getEnclosureEntryDate();

        if (date != null && !date.isBlank()) {

            try {

                entryDatePicker.setValue(
                        LocalDate.parse(date)
                );

            } catch (Exception exception) {

                entryDatePicker.setValue(
                        null
                );
            }

        } else {

            entryDatePicker.setValue(
                    null
            );
        }

        statusComboBox.setValue(
                permanentEnclosure.getStatus()
        );

        observationsArea.setText(
                permanentEnclosure.getObservations() != null
                        ? permanentEnclosure.getObservations()
                        : ""
        );
    }

    private void selectAnimal(
            int animalId) {

        for (Animal animal :
                animalComboBox.getItems()) {

            if (animal.getAnimalId() == animalId) {

                animalComboBox
                        .getSelectionModel()
                        .select(animal);

                return;
            }
        }
    }

    private void selectEnclosure(
            int enclosureId) {

        for (Enclosure enclosure :
                enclosureComboBox.getItems()) {

            if (enclosure.getEnclosureId()
                    == enclosureId) {

                enclosureComboBox
                        .getSelectionModel()
                        .select(enclosure);

                return;
            }
        }
    }

    private void addPermanentEnclosure() {

        try {

            PermanentEnclosure permanentEnclosure =
                    buildPermanentEnclosure();

            permanentEnclosureDAO.add(
                    permanentEnclosure
            );

            showInformation(
                    "Recinto permanente",
                    "El recinto permanente se registró correctamente."
            );

            loadPermanentEnclosures();
            clearForm();

        } catch (Exception exception) {

            showError(
                    "Agregar recinto permanente",
                    "No se pudo registrar el recinto permanente.",
                    exception.getMessage()
            );
        }
    }

    private void updatePermanentEnclosure() {

        if (selectedPermanentEnclosure == null) {

            showWarning(
                    "Actualizar recinto permanente",
                    "Seleccione primero un recinto permanente."
            );

            return;
        }

        try {

            PermanentEnclosure permanentEnclosure =
                    buildPermanentEnclosure();

            permanentEnclosure.setPermanentEnclosureId(
                    selectedPermanentEnclosure
                            .getPermanentEnclosureId()
            );

            permanentEnclosureDAO.update(
                    permanentEnclosure
            );

            showInformation(
                    "Recinto permanente",
                    "El recinto permanente se actualizó correctamente."
            );

            loadPermanentEnclosures();
            clearForm();

        } catch (Exception exception) {

            showError(
                    "Actualizar recinto permanente",
                    "No se pudo actualizar el recinto permanente.",
                    exception.getMessage()
            );
        }
    }

    private void deletePermanentEnclosure() {

        if (selectedPermanentEnclosure == null) {

            showWarning(
                    "Eliminar recinto permanente",
                    "Seleccione primero un recinto permanente."
            );

            return;
        }

        boolean confirmed =
                confirmAction(
                        "Eliminar recinto permanente",
                        "¿Está seguro de que desea eliminar "
                                + "el recinto permanente seleccionado?"
                );

        if (!confirmed) {
            return;
        }

        try {

            permanentEnclosureDAO.delete(
                    selectedPermanentEnclosure
                            .getPermanentEnclosureId()
            );

            showInformation(
                    "Recinto permanente",
                    "El recinto permanente se eliminó correctamente."
            );

            loadPermanentEnclosures();
            clearForm();

        } catch (Exception exception) {

            showError(
                    "Eliminar recinto permanente",
                    "No se pudo eliminar el recinto permanente.",
                    exception.getMessage()
            );
        }
    }

    private PermanentEnclosure buildPermanentEnclosure() {

        Animal selectedAnimal =
                animalComboBox.getValue();

        if (selectedAnimal == null) {

            throw new IllegalArgumentException(
                    "Seleccione un animal."
            );
        }

        String quantityText =
                quantityField.getText().trim();

        if (quantityText.isEmpty()) {

            throw new IllegalArgumentException(
                    "Ingrese la cantidad."
            );
        }

        int quantity;

        try {

            quantity =
                    Integer.parseInt(
                            quantityText
                    );

        } catch (NumberFormatException exception) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser un número entero válido."
            );
        }

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        String locationType =
                locationTypeComboBox.getValue();

        if (locationType == null
                || locationType.isBlank()) {

            throw new IllegalArgumentException(
                    "Seleccione un tipo de ubicación."
            );
        }

        Integer enclosureId = null;

        if ("Enclosure".equals(locationType)) {

            Enclosure selectedEnclosure =
                    enclosureComboBox.getValue();

            if (selectedEnclosure == null) {

                throw new IllegalArgumentException(
                        "Seleccione un recinto."
                );
            }

            enclosureId =
                    selectedEnclosure.getEnclosureId();
        }

        if (entryDatePicker.getValue() == null) {

            throw new IllegalArgumentException(
                    "Seleccione una fecha de ingreso."
            );
        }

        String status =
                statusComboBox.getValue();

        if (status == null
                || status.isBlank()) {

            throw new IllegalArgumentException(
                    "Seleccione un estado."
            );
        }

        String observations =
                observationsArea
                        .getText()
                        .trim();

        PermanentEnclosure permanentEnclosure =
                new PermanentEnclosure();

        permanentEnclosure.setAnimalId(
                selectedAnimal.getAnimalId()
        );

        permanentEnclosure.setQuantity(
                quantity
        );

        permanentEnclosure.setLocationType(
                locationType
        );

        permanentEnclosure.setEnclosureId(
                enclosureId
        );

        permanentEnclosure.setEnclosureEntryDate(
                entryDatePicker
                        .getValue()
                        .toString()
        );

        permanentEnclosure.setStatus(
                status
        );

        permanentEnclosure.setObservations(
                observations
        );

        return permanentEnclosure;
    }

    private void clearForm() {

        selectedPermanentEnclosure =
                null;

        table.getSelectionModel()
                .clearSelection();

        animalComboBox
                .getSelectionModel()
                .clearSelection();

        quantityField.clear();

        locationTypeComboBox
                .getSelectionModel()
                .clearSelection();

        enclosureComboBox
                .getSelectionModel()
                .clearSelection();

        enclosureComboBox.setDisable(
                true
        );

        entryDatePicker.setValue(
                LocalDate.now()
        );

        statusComboBox.setValue(
                "Active"
        );

        observationsArea.clear();
    }

    private boolean confirmAction(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait()
                .filter(
                        response ->
                                response.getText()
                                        .equals("Aceptar")
                )
                .isPresent();
    }

    private void showInformation(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showError(
            String title,
            String message,
            String details) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);

        alert.setContentText(
                details == null
                        || details.isBlank()
                        ? message
                        : message
                                + "\n\n"
                                + details
        );

        alert.showAndWait();
    }
}