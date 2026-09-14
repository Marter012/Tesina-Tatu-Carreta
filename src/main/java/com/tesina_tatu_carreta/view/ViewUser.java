package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.UserDAO;
import com.tesina_tatu_carreta.model.User;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ViewUser {

    private final UserDAO userDAO =
            new UserDAO();

    private final ObservableList<User> users =
            FXCollections.observableArrayList();

    private final TableView<User> table =
            new TableView<>();

    private TextField nameField;
    private TextField usernameField;
    private PasswordField passwordField;

    private ComboBox<String> roleComboBox;
    private ComboBox<String> statusComboBox;

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
                        "Gestión de usuarios"
                );

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #254D3D;"
        );

        Label subtitle =
                new Label(
                        "Gestiona los usuarios y sus roles del sistema."
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
                        "Información del usuario"
                );

        registeredButton =
                createSectionButton(
                        "Usuarios registrados"
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

        loadUsers();

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
                        "Información del usuario"
                );

        GridPane form =
                new GridPane();

        form.setHgap(20);
        form.setVgap(15);

        nameField =
                new TextField();

        nameField.setPromptText(
                "Ingrese el nombre completo"
        );

        usernameField =
                new TextField();

        usernameField.setPromptText(
                "Ingrese el nombre de usuario"
        );

        passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Ingrese la contraseña"
        );

        roleComboBox =
                new ComboBox<>();

        roleComboBox.getItems().addAll(
                "Administrator",
                "Manager",
                "Keeper",
                "Veterinarian"
        );

        roleComboBox.setPromptText(
                "Seleccione un rol"
        );

        roleComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        statusComboBox =
                new ComboBox<>();

        statusComboBox.getItems().addAll(
                "Active",
                "Inactive"
        );

        statusComboBox.setValue(
                "Active"
        );

        statusComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        form.add(
                createFieldLabel(
                        "Nombre"
                ),
                0,
                0
        );

        form.add(
                nameField,
                1,
                0
        );

        form.add(
                createFieldLabel(
                        "Usuario"
                ),
                0,
                1
        );

        form.add(
                usernameField,
                1,
                1
        );

        form.add(
                createFieldLabel(
                        "Contraseña"
                ),
                0,
                2
        );

        form.add(
                passwordField,
                1,
                2
        );

        form.add(
                createFieldLabel(
                        "Rol"
                ),
                0,
                3
        );

        form.add(
                roleComboBox,
                1,
                3
        );

        form.add(
                createFieldLabel(
                        "Estado"
                ),
                0,
                4
        );

        form.add(
                statusComboBox,
                1,
                4
        );

        GridPane.setHgrow(
                nameField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                usernameField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                passwordField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                roleComboBox,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                statusComboBox,
                Priority.ALWAYS
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
                        deleteUser()
        );

        editButton.setOnAction(
                event ->
                        updateUser()
        );

        addButton.setOnAction(
                event ->
                        addUser()
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
                        "Usuarios registrados"
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
                        loadUsers()
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

        TableColumn<User, Number>
                idColumn =
                new TableColumn<>(
                        "ID"
                );

        idColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getUserId()
                        )
        );

        TableColumn<User, String>
                nameColumn =
                new TableColumn<>(
                        "Nombre"
                );

        nameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getFullName()
                        )
        );

        TableColumn<User, String>
                usernameColumn =
                new TableColumn<>(
                        "Usuario"
                );

        usernameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getUsername()
                        )
        );

        TableColumn<User, String>
                roleColumn =
                new TableColumn<>(
                        "Rol"
                );

        roleColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                translateRole(
                                        data.getValue()
                                                .getRole()
                                )
                        )
        );

        TableColumn<User, String>
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

        idColumn.setPrefWidth(70);
        nameColumn.setPrefWidth(220);
        usernameColumn.setPrefWidth(180);
        roleColumn.setPrefWidth(160);
        statusColumn.setPrefWidth(120);

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                usernameColumn,
                roleColumn,
                statusColumn
        );

        table.setItems(
                users
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

                            if (newValue != null) {

                                loadSelectedUser(
                                        newValue
                                );
                            }
                        }
                );
    }

    // =========================================================
    // SELECTED USER
    // =========================================================

    private void loadSelectedUser(
            User user) {

        nameField.setText(
                user.getFullName()
        );

        usernameField.setText(
                user.getUsername()
        );

        passwordField.setText(
                user.getPassword()
        );

        roleComboBox.setValue(
                user.getRole()
        );

        statusComboBox.setValue(
                user.getStatus()
        );
    }

    // =========================================================
    // LOAD
    // =========================================================

    private void loadUsers() {

        users.setAll(
                userDAO.list()
        );
    }

    // =========================================================
    // ADD
    // =========================================================

    private void addUser() {

        if (!validateFields()) {
            return;
        }

        User user =
                new User();

        user.setFullName(
                nameField
                        .getText()
                        .trim()
        );

        user.setUsername(
                usernameField
                        .getText()
                        .trim()
        );

        user.setPassword(
                passwordField
                        .getText()
        );

        user.setRole(
                roleComboBox.getValue()
        );

        user.setStatus(
                statusComboBox.getValue()
        );

        userDAO.add(
                user
        );

        loadUsers();

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Éxito",
                "Usuario agregado correctamente."
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private void updateUser() {

        User selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione primero un usuario."
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        selected.setFullName(
                nameField
                        .getText()
                        .trim()
        );

        selected.setUsername(
                usernameField
                        .getText()
                        .trim()
        );

        selected.setPassword(
                passwordField
                        .getText()
        );

        selected.setRole(
                roleComboBox.getValue()
        );

        selected.setStatus(
                statusComboBox.getValue()
        );

        userDAO.update(
                selected
        );

        loadUsers();

        clearFields();

        showMessage(
                Alert.AlertType.INFORMATION,
                "Éxito",
                "Usuario actualizado correctamente."
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteUser() {

        User selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione primero un usuario."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Eliminar usuario"
        );

        confirmation.setHeaderText(
                null
        );

        confirmation.setContentText(
                "¿Está seguro de que desea eliminar al usuario \""
                        + selected.getUsername()
                        + "\"?"
        );

        confirmation.showAndWait()
                .ifPresent(
                        response -> {

                            if (response ==
                                    ButtonType.OK) {

                                userDAO.delete(
                                        selected
                                                .getUserId()
                                );

                                loadUsers();

                                clearFields();

                                showMessage(
                                        Alert.AlertType.INFORMATION,
                                        "Éxito",
                                        "Usuario eliminado correctamente."
                                );
                            }
                        }
                );
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private boolean validateFields() {

        if (nameField.getText()
                .trim()
                .isEmpty()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "El nombre es obligatorio."
            );

            return false;
        }

        if (usernameField.getText()
                .trim()
                .isEmpty()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "El nombre de usuario es obligatorio."
            );

            return false;
        }

        if (passwordField.getText()
                .isEmpty()) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "La contraseña es obligatoria."
            );

            return false;
        }

        if (roleComboBox.getValue() == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "El rol es obligatorio."
            );

            return false;
        }

        if (statusComboBox.getValue() == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Error de validación",
                    "El estado es obligatorio."
            );

            return false;
        }

        return true;
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private void clearFields() {

        nameField.clear();

        usernameField.clear();

        passwordField.clear();

        roleComboBox.setValue(
                null
        );

        statusComboBox.setValue(
                "Active"
        );

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

    // =========================================================
    // TRANSLATIONS
    // =========================================================

    private String translateRole(
            String role) {

        if (role == null) {
            return "";
        }

        return switch (role) {

            case "Administrator" ->
                    "Administrador";

            case "Manager" ->
                    "Encargado";

            case "Keeper" ->
                    "Cuidador";

            case "Veterinarian" ->
                    "Veterinario";

            default ->
                    role;
        };
    }

    private String translateStatus(
            String status) {

        if (status == null) {
            return "";
        }

        return switch (status) {

            case "Active" ->
                    "Activo";

            case "Inactive" ->
                    "Inactivo";

            default ->
                    status;
        };
    }

    // =========================================================
    // STYLES
    // =========================================================

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