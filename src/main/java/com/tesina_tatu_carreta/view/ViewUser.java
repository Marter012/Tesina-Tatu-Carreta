package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.UserDAO;
import com.tesina_tatu_carreta.model.User;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewUser {

    private final UserDAO userDAO = new UserDAO();

    private final TableView<User> table =
            new TableView<>();

    private final TextField nameField =
            new TextField();

    private final TextField usernameField =
            new TextField();

    private final PasswordField passwordField =
            new PasswordField();

    private final ComboBox<String> roleComboBox =
            new ComboBox<>();

    private final ComboBox<String> statusComboBox =
            new ComboBox<>();

    public void show() {

        Label title = new Label(
                "USER MANAGEMENT"
        );

        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        Label nameLabel = new Label("Name:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label roleLabel = new Label("Role:");
        Label statusLabel = new Label("Status:");

        nameField.setPromptText(
                "E.g.: Juan Pérez"
        );

        usernameField.setPromptText(
                "E.g.: jperez"
        );

        passwordField.setPromptText(
                "Password"
        );

        roleComboBox.setItems(
                FXCollections.observableArrayList(
                        "Administrator",
                        "Manager",
                        "Keeper",
                        "Veterinarian"
                )
        );

        statusComboBox.setItems(
                FXCollections.observableArrayList(
                        "Active",
                        "Inactive"
                )
        );

        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(
                new Insets(10)
        );

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);

        form.add(usernameLabel, 0, 1);
        form.add(usernameField, 1, 1);

        form.add(passwordLabel, 0, 2);
        form.add(passwordField, 1, 2);

        form.add(roleLabel, 0, 3);
        form.add(roleComboBox, 1, 3);

        form.add(statusLabel, 0, 4);
        form.add(statusComboBox, 1, 4);

        Button addButton =
                new Button("ADD");

        Button updateButton =
                new Button("UPDATE");

        Button deleteButton =
                new Button("DELETE");

        Button backButton =
                new Button("BACK");

        addButton.setOnAction(
                event -> addUser()
        );

        updateButton.setOnAction(
                event -> updateUser()
        );

        deleteButton.setOnAction(
                event -> deleteUser()
        );

        backButton.setOnAction(event -> {

            Stage currentStage =
                    (Stage) backButton
                            .getScene()
                            .getWindow();

            currentStage.close();
        });

        HBox buttons = new HBox(
                10,
                addButton,
                updateButton,
                deleteButton,
                backButton
        );

        buttons.setAlignment(
                Pos.CENTER
        );

        createTable();

        table.setOnMouseClicked(event -> {

            User selectedUser =
                    table.getSelectionModel()
                            .getSelectedItem();

            if (selectedUser != null) {

                loadSelectedUser(selectedUser);
            }
        });

        VBox root = new VBox(
                15,
                title,
                form,
                buttons,
                table
        );

        root.setPadding(
                new Insets(20)
        );

        loadUsers();

        Stage stage = new Stage();

        stage.setTitle(
                "Tatú Carreta - Users"
        );

        stage.setScene(
                new Scene(root, 800, 600)
        );

        stage.show();
    }

    private void createTable() {

        TableColumn<User, Number> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getUserId()
                )
        );

        TableColumn<User, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getFullName()
                )
        );

        TableColumn<User, String> usernameColumn =
                new TableColumn<>("Username");

        usernameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getUsername()
                )
        );

        TableColumn<User, String> roleColumn =
                new TableColumn<>("Role");

        roleColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getRole()
                )
        );

        TableColumn<User, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getStatus()
                )
        );
        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(usernameColumn);
        table.getColumns().add(roleColumn);
        table.getColumns().add(statusColumn);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
    }

    private void loadUsers() {

        table.getItems().clear();

        ObservableList<User> users =
                FXCollections.observableArrayList(
                        userDAO.list()
                );

        table.getItems().addAll(users);
    }

    private void loadSelectedUser(
            User selectedUser) {

        nameField.setText(
                selectedUser.getFullName()
        );

        usernameField.setText(
                selectedUser.getUsername()
        );

        passwordField.setText(
                selectedUser.getPassword()
        );

        roleComboBox.setValue(
                selectedUser.getRole()
        );

        statusComboBox.setValue(
                selectedUser.getStatus()
        );
    }

    private void addUser() {

        if (nameField.getText().isBlank() ||
                usernameField.getText().isBlank() ||
                passwordField.getText().isBlank() ||
                roleComboBox.getValue() == null ||
                statusComboBox.getValue() == null) {

            showError(
                    "Complete all required fields."
            );

            return;
        }

        User user = new User();

        user.setFullName(
                nameField.getText().trim()
        );

        user.setUsername(
                usernameField.getText().trim()
        );

        user.setPassword(
                passwordField.getText()
        );

        user.setRole(
                roleComboBox.getValue()
        );

        user.setStatus(
                statusComboBox.getValue()
        );

        userDAO.add(user);

        loadUsers();
        clearFields();
    }

    private void updateUser() {

        User selectedUser =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selectedUser == null) {

            showError(
                    "Select a record."
            );

            return;
        }

        if (nameField.getText().isBlank() ||
                usernameField.getText().isBlank() ||
                passwordField.getText().isBlank() ||
                roleComboBox.getValue() == null ||
                statusComboBox.getValue() == null) {

            showError(
                    "Complete all required fields."
            );

            return;
        }

        selectedUser.setFullName(
                nameField.getText().trim()
        );

        selectedUser.setUsername(
                usernameField.getText().trim()
        );

        selectedUser.setPassword(
                passwordField.getText()
        );

        selectedUser.setRole(
                roleComboBox.getValue()
        );

        selectedUser.setStatus(
                statusComboBox.getValue()
        );

        userDAO.update(selectedUser);

        loadUsers();
        clearFields();
    }

    private void deleteUser() {

        User selectedUser =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selectedUser == null) {

            showError(
                    "Select a record."
            );

            return;
        }

        userDAO.delete(
                selectedUser.getUserId()
        );

        loadUsers();
        clearFields();
    }

    private void clearFields() {

        nameField.clear();
        usernameField.clear();
        passwordField.clear();

        roleComboBox.setValue(null);
        statusComboBox.setValue(null);

        table.getSelectionModel()
                .clearSelection();
    }

    private void showError(
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}