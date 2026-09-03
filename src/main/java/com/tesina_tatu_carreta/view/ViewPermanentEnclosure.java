package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.UserDAO;
import com.tesina_tatu_carreta.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewPermanentEnclosure {

        private final UserDAO userDAO = new UserDAO();

        private final TableView<User> table = new TableView<>();

        private final TextField txtName = new TextField();
        private final TextField txtUser = new TextField();
        private final PasswordField txtPassword = new PasswordField();

        private final ComboBox<String> comboRole = new ComboBox<>();
        private final ComboBox<String> comboStatus = new ComboBox<>();

        public void show() {

                Stage ventana = new Stage();

                Label titulo = new Label("ADMINISTRACIÓN DE USUARIOS");

                titulo.setStyle(
                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #23452C;");

                Label lblNombre = new Label("Nombre:");
                Label lblUsuario = new Label("Usuario:");
                Label lblPassword = new Label("Contraseña:");
                Label lblRol = new Label("Rol:");
                Label lblEstado = new Label("Estado:");

                txtName.setPromptText("Ej: Juan Pérez");
                txtUser.setPromptText("Ej: jperez");
                txtPassword.setPromptText("Contraseña");

                comboRole.getItems().addAll(
                                "Administrador",
                                "Encargado",
                                "Cuidador",
                                "Veterinario");

                comboStatus.getItems().addAll(
                                "Activo",
                                "Inactivo");

                comboStatus.setValue("Activo");

                HBox filaNombre = new HBox(
                                10,
                                lblNombre,
                                txtName);

                HBox filaUsuario = new HBox(
                                10,
                                lblUsuario,
                                txtUser);

                HBox filaPassword = new HBox(
                                10,
                                lblPassword,
                                txtPassword);

                HBox filaRol = new HBox(
                                10,
                                lblRol,
                                comboRole);

                HBox filaEstado = new HBox(
                                10,
                                lblEstado,
                                comboStatus);

                Button btnAgregar = new Button("AGREGAR");
                Button btnModificar = new Button("MODIFICAR");
                Button btnEliminar = new Button("ELIMINAR");
                Button btnVolver = new Button("VOLVER");

                HBox botones = new HBox(
                                10,
                                btnAgregar,
                                btnModificar,
                                btnEliminar);

                botones.setAlignment(Pos.CENTER);

                // COLUMNAS DE LA TABLA

                TableColumn<User, Integer> columnaId = new TableColumn<>("ID");

                columnaId.setCellValueFactory(
                                new PropertyValueFactory<>("idUsuario"));

                TableColumn<User, String> columnaNombre = new TableColumn<>("Nombre");

                columnaNombre.setCellValueFactory(
                                new PropertyValueFactory<>("nombreUsuario"));

                TableColumn<User, String> columnaUsuario = new TableColumn<>("Usuario");

                columnaUsuario.setCellValueFactory(
                                new PropertyValueFactory<>("usuario"));

                TableColumn<User, String> columnaRol = new TableColumn<>("Rol");

                columnaRol.setCellValueFactory(
                                new PropertyValueFactory<>("rol"));

                TableColumn<User, String> columnaEstado = new TableColumn<>("Estado");

                columnaEstado.setCellValueFactory(
                                new PropertyValueFactory<>("estado"));

                table.getColumns().add(columnaId);
                table.getColumns().add(columnaNombre);
                table.getColumns().add(columnaUsuario);
                table.getColumns().add(columnaRol);
                table.getColumns().add(columnaEstado);

                table.setPrefHeight(300);

                cargarUsuarios();

                // SELECCIONAR USUARIO

                table.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable, anterior, userSelected) -> {

                                                        if (userSelected != null) {

                                                                txtName.setText(
                                                                                userSelected.getFullName());

                                                                txtUser.setText(
                                                                                userSelected.getUsername());

                                                                txtPassword.setText(
                                                                                userSelected.getPassword());

                                                                comboRole.setValue(
                                                                                userSelected.getRole());

                                                                comboStatus.setValue(
                                                                                userSelected.getStatus());
                                                        }
                                                });

                // AGREGAR

                btnAgregar.setOnAction(e -> {

                        if (txtName.getText().isBlank()
                                        || txtUser.getText().isBlank()
                                        || txtPassword.getText().isBlank()
                                        || comboRole.getValue() == null
                                        || comboStatus.getValue() == null) {

                                return;
                        }

                        User user = new User();

                        user.setFullName(
                                        txtName.getText());

                        user.setUsername(
                                        txtUser.getText());

                        user.setPassword(
                                        txtPassword.getText());

                        user.setRole(
                                        comboRole.getValue());

                        user.setStatus(
                                        comboStatus.getValue());

                        userDAO.add(user);

                        limpiarCampos();
                        cargarUsuarios();
                });

                // MODIFICAR

                btnModificar.setOnAction(e -> {

                        User userSelected = table.getSelectionModel()
                                        .getSelectedItem();

                        if (userSelected == null) {
                                return;
                        }

                        userSelected.setFullName(
                                        txtName.getText());

                        userSelected.setUsername(
                                        txtUser.getText());

                        userSelected.setPassword(
                                        txtPassword.getText());

                        userSelected.setRole(
                                        comboRole.getValue());

                        userSelected.setStatus(
                                        comboStatus.getValue());

                        userDAO.update(userSelected);

                        limpiarCampos();
                        cargarUsuarios();
                });

                // ELIMINAR

                btnEliminar.setOnAction(e -> {

                        User userSelected = table.getSelectionModel()
                                        .getSelectedItem();

                        if (userSelected == null) {
                                return;
                        }

                        userDAO.delete(
                                        userSelected.getUserId());

                        limpiarCampos();
                        cargarUsuarios();
                });

                // VOLVER

                btnVolver.setOnAction(e -> ventana.close());

                VBox contenedor = new VBox(15);

                contenedor.setAlignment(Pos.TOP_CENTER);
                contenedor.setPadding(new Insets(30));

                contenedor.getChildren().addAll(
                                titulo,
                                filaNombre,
                                filaUsuario,
                                filaPassword,
                                filaRol,
                                filaEstado,
                                botones,
                                table,
                                btnVolver);

                contenedor.setStyle(
                                "-fx-background-color: #F2F0E6;");

                Scene escena = new Scene(
                                contenedor,
                                850,
                                700);

                ventana.setTitle(
                                "Tatú Carreta - Usuarios");

                ventana.setScene(escena);
                ventana.show();
        }

        private void cargarUsuarios() {

                ObservableList<User> lista = FXCollections.observableArrayList(
                                userDAO.list());

                table.setItems(lista);
        }

        private void limpiarCampos() {

                txtName.clear();
                txtUser.clear();
                txtPassword.clear();

                comboRole.setValue(null);
                comboStatus.setValue("Activo");

                table.getSelectionModel().clearSelection();
        }
}