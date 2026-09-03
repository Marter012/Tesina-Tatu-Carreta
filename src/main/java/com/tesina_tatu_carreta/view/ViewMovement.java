package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.MovementDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.Entry;
import com.tesina_tatu_carreta.model.Movement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewMovement {

    private final MovementDAO movementDAO =
            new MovementDAO();

    private final AnimalDAO animalDAO =
            new AnimalDAO();

    private final EntryDAO entryDAO =
            new EntryDAO();

    private TableView<Movement> table;

    private ComboBox<Animal> animalComboBox;
    private ComboBox<Entry> entryComboBox;

    private TextField dateField;
    private ComboBox<String> movementTypeComboBox;
    private TextField quantityField;
    private TextField destinationField;
    private TextArea observationsArea;


    public void show(Stage stage) {

        // =========================
        // TITULO
        // =========================

        Label breadcrumb = new Label(
                "Home / Movement Management"
        );

        breadcrumb.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #7A827B;"
        );


        Label title = new Label(
                "Animal Movements"
        );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #30463A;"
        );


        Label subtitle = new Label(
                "Register and view animal movements within the reserve"
        );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #68746B;"
        );


        VBox header = new VBox(
                5,
                breadcrumb,
                title,
                subtitle
        );


        // =========================
        // CAMPOS
        // =========================

        animalComboBox = new ComboBox<>();

        animalComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        animalComboBox.setPromptText(
                "Select an animal"
        );

        loadAnimals();


        animalComboBox.setOnAction(e ->
                loadEntriesByAnimal()
        );


        entryComboBox = new ComboBox<>();

        entryComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        entryComboBox.setPromptText(
                "Select an animal first"
        );


        dateField = new TextField();

        dateField.setPromptText(
                "E.g.: 25/08/2026"
        );


        movementTypeComboBox = new ComboBox<>();

        movementTypeComboBox.setItems(
                FXCollections.observableArrayList(
                        "RELEASE",
                        "TRANSFER",
                        "DEATH"
                )
        );

        movementTypeComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        movementTypeComboBox.setPromptText(
                "Select the movement type"
        );


        quantityField = new TextField();

        quantityField.setPromptText(
                "Enter the quantity"
        );


        destinationField = new TextField();

        destinationField.setPromptText(
                "Only if applicable"
        );


        observationsArea = new TextArea();

        observationsArea.setPromptText(
                "Enter additional observations"
        );

        observationsArea.setPrefRowCount(2);

        observationsArea.setWrapText(true);


        // =========================
        // ESTILO CAMPOS
        // =========================

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
        destinationField.setStyle(fieldStyle);
        observationsArea.setStyle(fieldStyle);


        // =========================
        // FORMULARIO
        // =========================

        GridPane form = new GridPane();

        form.setHgap(15);
        form.setVgap(10);

        form.setAlignment(
                Pos.CENTER
        );


        ColumnConstraints labelColumn =
                new ColumnConstraints();

        labelColumn.setPercentWidth(28);


        ColumnConstraints fieldColumn =
                new ColumnConstraints();

        fieldColumn.setPercentWidth(72);


        form.getColumnConstraints().addAll(
                labelColumn,
                fieldColumn
        );


        form.add(
                createLabel("Animal *"),
                0,
                0
        );

        form.add(
                animalComboBox,
                1,
                0
        );


        form.add(
                createLabel("Record No. *"),
                0,
                1
        );

        form.add(
                entryComboBox,
                1,
                1
        );


        form.add(
                createLabel("Date *"),
                0,
                2
        );

        form.add(
                dateField,
                1,
                2
        );


        form.add(
                createLabel("Movement Type *"),
                0,
                3
        );

        form.add(
                movementTypeComboBox,
                1,
                3
        );


        form.add(
                createLabel("Quantity *"),
                0,
                4
        );

        form.add(
                quantityField,
                1,
                4
        );


        form.add(
                createLabel("Destination"),
                0,
                5
        );

        form.add(
                destinationField,
                1,
                5
        );


        form.add(
                createLabel("Observations"),
                0,
                6
        );

        form.add(
                observationsArea,
                1,
                6
        );


        // =========================
        // TARJETA FORMULARIO
        // =========================

        Label formTitle = new Label(
                "Movement Data"
        );

        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #30463A;"
        );


        VBox formCard =
                new VBox(
                        18,
                        formTitle,
                        form
                );

        formCard.setPadding(
                new Insets(22)
        );

        formCard.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #D5DBD5;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;"
        );


        // =========================
        // BOTONES
        // =========================

        Button registerButton =
                new Button(
                        "REGISTER MOVEMENT"
                );

        registerButton.setPrefHeight(38);

        registerButton.setStyle(
                "-fx-background-color: #23452C;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 0 20 0 20;"
        );


        registerButton.setOnAction(e ->
                saveMovement()
        );


        Button clearButton =
                new Button("CLEAR");

        clearButton.setPrefHeight(38);

        clearButton.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #30463A;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #C7D0C8;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 0 20 0 20;"
        );


        clearButton.setOnAction(e ->
                clearFields()
        );


        HBox buttons = new HBox(
                10,
                registerButton,
                clearButton
        );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );


        // =========================
        // TABLA
        // =========================

        table = new TableView<>();

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );


        TableColumn<Movement, Integer>
                idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "movementId"
                )
        );


        TableColumn<Movement, Integer>
                animalColumn =
                new TableColumn<>("Animal ID");

        animalColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "animalId"
                )
        );


        TableColumn<Movement, String>
                dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "movementDate"
                )
        );


        TableColumn<Movement, String>
                typeColumn =
                new TableColumn<>("Type");

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "movementType"
                )
        );


        TableColumn<Movement, Integer>
                quantityColumn =
                new TableColumn<>("Quantity");

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "quantity"
                )
        );


        TableColumn<Movement, String>
                destinationColumn =
                new TableColumn<>("Destination");

        destinationColumn.setCellValueFactory(
                new PropertyValueFactory<>(
                        "destination"
                )
        );

        table.getColumns().add(idColumn);
        table.getColumns().add(animalColumn);
        table.getColumns().add(dateColumn);
        table.getColumns().add(typeColumn);
        table.getColumns().add(quantityColumn);
        table.getColumns().add(destinationColumn);

        // TABLA MAS BAJA PARA QUE ENTRE COMPLETA
        table.setPrefHeight(170);

        loadMovements();


        // =========================
        // TARJETA TABLA
        // =========================

        Label tableTitle = new Label(
                "Movement History"
        );

        tableTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #30463A;"
        );


        VBox tableCard =
                new VBox(
                        12,
                        tableTitle,
                        table
                );

        tableCard.setPadding(
                new Insets(18)
        );

        tableCard.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #D5DBD5;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;"
        );


        // =========================
        // CONTENEDOR PRINCIPAL
        // =========================

        VBox content =
                new VBox(16);

        content.setPadding(
                new Insets(25, 35, 25, 35)
        );

        content.getChildren().addAll(
                header,
                formCard,
                buttons,
                tableCard
        );

        content.setStyle(
                "-fx-background-color: #F2F0E6;"
        );


        BorderPane root =
                new BorderPane();

        root.setCenter(content);


        // =========================
        // ESCENA
        // =========================

        Scene scene = new Scene(
                root,
                900,
                700
        );

        stage.setTitle(
                "Tatú Carreta - Movements"
        );

        stage.setScene(scene);

        stage.setMinWidth(850);
        stage.setMinHeight(650);

        stage.show();
    }


    // =========================
    // LABEL
    // =========================

    private Label createLabel(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #34463A;"
        );

        return label;
    }


    // =========================
    // CARGAR ANIMALES
    // =========================

    private void loadAnimals() {

        ObservableList<Animal> animals =
                FXCollections.observableArrayList(
                        animalDAO.list()
                );

        animalComboBox.setItems(animals);
    }


    // =========================
    // CARGAR INGRESOS
    // =========================

    private void loadEntriesByAnimal() {

        Animal selectedAnimal =
                animalComboBox.getValue();

        entryComboBox.getItems().clear();

        if (selectedAnimal == null) {
            return;
        }

        ObservableList<Entry> entries =
                FXCollections.observableArrayList(
                        entryDAO.listByAnimal(
                                selectedAnimal.getAnimalId()
                        )
                );

        entryComboBox.setItems(entries);

        entryComboBox.setPromptText(
                "Select the record number"
        );
    }


    // =========================
    // GUARDAR
    // =========================

    private void saveMovement() {

        try {

            if (animalComboBox.getValue() == null
                    || entryComboBox.getValue() == null
                    || dateField.getText().isBlank()
                    || movementTypeComboBox.getValue() == null
                    || quantityField.getText().isBlank()) {

                showMessage(
                        "Complete the required fields."
                );

                return;
            }

            Movement movement =
                    new Movement();

            movement.setAnimalId(
                    animalComboBox.getValue()
                            .getAnimalId()
            );

            movement.setEntryId(
                    entryComboBox.getValue()
                            .getEntryId()
            );

            movement.setMovementDate(
                    dateField.getText()
            );

            movement.setMovementType(
                    movementTypeComboBox.getValue()
            );

            movement.setQuantity(
                    Integer.parseInt(
                            quantityField.getText()
                    )
            );

            movement.setDestination(
                    destinationField.getText()
            );

            movement.setObservations(
                    observationsArea.getText()
            );

            movementDAO.add(movement);

            showInformation(
                    "Movement registered successfully."
            );

            clearFields();

            loadMovements();

        } catch (NumberFormatException e) {

            showMessage(
                    "Quantity must be a number."
            );
        }
    }


    // =========================
    // CARGAR TABLA
    // =========================

    private void loadMovements() {

        ObservableList<Movement> movements =
                FXCollections.observableArrayList(
                        movementDAO.list()
                );

        table.setItems(movements);
    }


    // =========================
    // LIMPIAR
    // =========================

    private void clearFields() {

        animalComboBox.setValue(null);

        entryComboBox.getItems().clear();

        entryComboBox.setValue(null);

        entryComboBox.setPromptText(
                "Select an animal first"
        );

        dateField.clear();

        movementTypeComboBox.setValue(null);

        quantityField.clear();

        destinationField.clear();

        observationsArea.clear();
    }


    // =========================
    // MENSAJES
    // =========================

    private void showMessage(String message) {

        Alert alert = new Alert(
                Alert.AlertType.WARNING
        );

        alert.setTitle("Tatú Carreta");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }


    private void showInformation(
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setTitle("Tatú Carreta");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}