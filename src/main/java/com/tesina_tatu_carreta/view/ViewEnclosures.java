package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.EnclosureDAO;
import com.tesina_tatu_carreta.model.Enclosure;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewEnclosures {

        private final EnclosureDAO enclosureDAO = new EnclosureDAO();

        private final ObservableList<Enclosure> listEnclosures = FXCollections.observableArrayList();

        private Enclosure enclosureSelected;

        private TableView<Enclosure> table;

        private TextField txtNombre;
        private TextField txtSector;
        private TextField txtCapacidad;

        private ComboBox<String> cmbEstado;

        private TextArea txtObservaciones;

        public void show() {

                Stage escenario = new Stage();

                // =========================================
                // HEADER
                // =========================================

                Label breadcrumb = new Label("Home / Enclosure Management");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #7A8580;");

                Label titulo = new Label("Enclosure Management");

                titulo.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label subtitulo = new Label(
                                "Manage the reserve's enclosures");

                subtitulo.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #6B756F;");

                VBox encabezado = new VBox(
                                6,
                                breadcrumb,
                                titulo,
                                subtitulo);

                // =========================================
                // FIELDS
                // =========================================

                txtNombre = new TextField();

                txtNombre.setPromptText(
                                "Ex: Main aviary");

                txtSector = new TextField();

                txtSector.setPromptText(
                                "Ex: Sector A");

                txtCapacidad = new TextField();

                txtCapacidad.setPromptText(
                                "Ex: 50");

                cmbEstado = new ComboBox<>();

                cmbEstado.getItems().addAll(
                                "Active",
                                "Inactive");

                cmbEstado.setValue("Active");

                txtObservaciones = new TextArea();

                txtObservaciones.setPromptText(
                                "Additional observations...");

                txtObservaciones.setPrefRowCount(3);

                txtObservaciones.setWrapText(true);

                // =========================================
                // FIELD STYLE
                // =========================================

                String estiloCampo = "-fx-background-radius: 8;" +
                                "-fx-border-radius: 8;" +
                                "-fx-border-color: #D1D8D2;" +
                                "-fx-padding: 8;";

                txtNombre.setStyle(estiloCampo);
                txtSector.setStyle(estiloCampo);
                txtCapacidad.setStyle(estiloCampo);
                cmbEstado.setStyle(estiloCampo);
                txtObservaciones.setStyle(estiloCampo);

                txtNombre.setPrefHeight(38);
                txtSector.setPrefHeight(38);
                txtCapacidad.setPrefHeight(38);
                cmbEstado.setPrefHeight(38);

                // =========================================
                // FORM
                // =========================================

                Label lblNombre = crearLabelCampo("Name");

                Label lblSector = crearLabelCampo("Sector");

                Label lblCapacidad = crearLabelCampo("Capacity");

                Label lblEstado = crearLabelCampo("Status");

                Label lblObservaciones = crearLabelCampo("Observations");

                GridPane formulario = new GridPane();

                formulario.setHgap(20);
                formulario.setVgap(15);

                formulario.add(
                                lblNombre,
                                0,
                                0);

                formulario.add(
                                lblSector,
                                1,
                                0);

                formulario.add(
                                txtNombre,
                                0,
                                1);

                formulario.add(
                                txtSector,
                                1,
                                1);

                formulario.add(
                                lblCapacidad,
                                0,
                                2);

                formulario.add(
                                lblEstado,
                                1,
                                2);

                formulario.add(
                                txtCapacidad,
                                0,
                                3);

                formulario.add(
                                cmbEstado,
                                1,
                                3);

                formulario.add(
                                lblObservaciones,
                                0,
                                4,
                                2,
                                1);

                formulario.add(
                                txtObservaciones,
                                0,
                                5,
                                2,
                                1);

                GridPane.setHgrow(
                                txtNombre,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                txtSector,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                txtCapacidad,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                cmbEstado,
                                Priority.ALWAYS);

                GridPane.setHgrow(
                                txtObservaciones,
                                Priority.ALWAYS);

                // =========================================
                // BUTTONS
                // =========================================

                Button btnLimpiar = new Button("CLEAR");

                Button btnEliminar = new Button("DELETE");

                Button btnModificar = new Button("EDIT");

                Button btnGuardar = new Button("ADD");

                Button btnVolver = new Button("BACK");

                btnLimpiar.setPrefHeight(38);
                btnEliminar.setPrefHeight(38);
                btnModificar.setPrefHeight(38);
                btnGuardar.setPrefHeight(38);
                btnVolver.setPrefHeight(38);

                btnLimpiar.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");

                btnEliminar.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #A34A4A;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #E2C5C5;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");

                btnModificar.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");

                btnGuardar.setStyle(
                                "-fx-background-color: #254D3D;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");

                btnVolver.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: #405047;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #C9D2CB;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");

                HBox botones = new HBox(
                                10,
                                btnLimpiar,
                                btnEliminar,
                                btnModificar,
                                btnGuardar);

                botones.setAlignment(
                                Pos.CENTER_RIGHT);

                // =========================================
                // FORM CARD
                // =========================================

                Label tituloDatos = new Label("Enclosure Information");

                tituloDatos.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                VBox tarjetaDatos = new VBox(
                                20,
                                tituloDatos,
                                formulario,
                                botones);

                tarjetaDatos.setPadding(
                                new Insets(25));

                tarjetaDatos.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #D8DED9;" +
                                                "-fx-border-radius: 16;");

                // =========================================
                // TABLE
                // =========================================

                table = new TableView<>();

                TableColumn<Enclosure, Number> colId = new TableColumn<>("ID");

                colId.setCellValueFactory(
                                celda -> new SimpleIntegerProperty(
                                                celda.getValue()
                                                                .getEnclosureId()));

                TableColumn<Enclosure, String> colNombre = new TableColumn<>("Name");

                colNombre.setCellValueFactory(
                                celda -> new SimpleStringProperty(
                                                celda.getValue()
                                                                .getName()));

                TableColumn<Enclosure, String> colSector = new TableColumn<>("Sector");

                colSector.setCellValueFactory(
                                celda -> new SimpleStringProperty(
                                                celda.getValue()
                                                                .getSector()));

                TableColumn<Enclosure, Number> colCapacidad = new TableColumn<>("Capacity");

                colCapacidad.setCellValueFactory(
                                celda -> {

                                        Integer capacidad = celda.getValue()
                                                        .getCapacity();

                                        if (capacidad == null) {

                                                return new SimpleIntegerProperty(0);
                                        }

                                        return new SimpleIntegerProperty(
                                                        capacidad);
                                });

                TableColumn<Enclosure, String> colEstado = new TableColumn<>("Status");

                colEstado.setCellValueFactory(
                                celda -> new SimpleStringProperty(
                                                celda.getValue()
                                                                .getStatus()));

                table.getColumns().add(colId);
                table.getColumns().add(colNombre);
                table.getColumns().add(colSector);
                table.getColumns().add(colCapacidad);
                table.getColumns().add(colEstado);

                table.setItems(
                                listEnclosures);

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                table.setPrefHeight(280);

                table.setMinHeight(220);

                table.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable,
                                                                anterior,
                                                                seleccionado) -> {

                                                        if (seleccionado != null) {

                                                                cargarenclosureSelected(
                                                                                seleccionado);
                                                        }
                                                });

                // =========================================
                // TABLE CARD
                // =========================================

                Label tituloTabla = new Label("Registered Enclosures");

                tituloTabla.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                VBox tarjetaTabla = new VBox(
                                15,
                                tituloTabla,
                                table);

                tarjetaTabla.setPadding(
                                new Insets(25));

                tarjetaTabla.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 16;" +
                                                "-fx-border-color: #D8DED9;" +
                                                "-fx-border-radius: 16;");

                // =========================================
                // BACK BUTTON
                // =========================================

                HBox contenedorVolver = new HBox(btnVolver);

                contenedorVolver.setAlignment(
                                Pos.CENTER);

                // =========================================
                // ACTIONS
                // =========================================

                btnGuardar.setOnAction(
                                e -> guardar());

                btnModificar.setOnAction(
                                e -> modificar());

                btnEliminar.setOnAction(
                                e -> eliminar());

                btnLimpiar.setOnAction(
                                e -> limpiar());

                btnVolver.setOnAction(
                                e -> escenario.close());

                // =========================================
                // MAIN CONTAINER
                // =========================================

                VBox contenido = new VBox(
                                25,
                                encabezado,
                                tarjetaDatos,
                                tarjetaTabla,
                                contenedorVolver);

                contenido.setPadding(
                                new Insets(25, 35, 35, 35));

                contenido.setAlignment(
                                Pos.TOP_CENTER);

                contenido.setStyle(
                                "-fx-background-color: #F4F1E8;");

                // =========================================
                // SCROLL
                // =========================================

                ScrollPane scroll = new ScrollPane(contenido);

                scroll.setFitToWidth(true);

                scroll.setStyle(
                                "-fx-background: #F4F1E8;" +
                                                "-fx-background-color: #F4F1E8;");

                // =========================================
                // SCENE
                // =========================================

                Scene escena = new Scene(
                                scroll,
                                1100,
                                750);

                escenario.setTitle(
                                "Tatú Carreta - Enclosure Management");

                escenario.setMinWidth(900);

                escenario.setMinHeight(650);

                escenario.setScene(escena);

                escenario.setMaximized(true);

                loadEnclosures();

                escenario.show();
        }

        // =========================================
        // CREATE LABEL
        // =========================================

        private Label crearLabelCampo(
                        String texto) {

                Label label = new Label(texto);

                label.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #445149;");

                return label;
        }

        // =========================================
        // SAVE
        // =========================================

        private void guardar() {

                String nombre = txtNombre.getText().trim();

                String sector = txtSector.getText().trim();

                String capacidadTexto = txtCapacidad.getText().trim();

                String estado = cmbEstado.getValue();

                String observaciones = txtObservaciones.getText().trim();

                if (nombre.isBlank()
                                || sector.isBlank()) {

                        mostrarMensaje(
                                        Alert.AlertType.WARNING,
                                        "Please complete the required fields.");

                        return;
                }

                Integer capacidad = null;

                if (!capacidadTexto.isBlank()) {

                        try {

                                capacidad = Integer.parseInt(
                                                capacidadTexto);

                        } catch (NumberFormatException e) {

                                mostrarMensaje(
                                                Alert.AlertType.WARNING,
                                                "Capacity must be a number.");

                                return;
                        }
                }

                Enclosure enclosure = new Enclosure();

                enclosure.setName(nombre);

                enclosure.setSector(sector);

                enclosure.setCapacity(capacidad);

                enclosure.setStatus(estado);

                enclosure.setObservations(
                                observaciones);

                enclosureDAO.add(enclosure);

                loadEnclosures();

                limpiar();

                mostrarMensaje(
                                Alert.AlertType.INFORMATION,
                                "Enclosure added successfully.");
        }

        // =========================================
        // EDIT
        // =========================================

        private void modificar() {

                if (enclosureSelected == null) {

                        mostrarMensaje(
                                        Alert.AlertType.WARNING,
                                        "Select an enclosure to edit.");

                        return;
                }

                String nombre = txtNombre.getText().trim();

                String sector = txtSector.getText().trim();

                String capacidadTexto = txtCapacidad.getText().trim();

                if (nombre.isBlank()
                                || sector.isBlank()) {

                        mostrarMensaje(
                                        Alert.AlertType.WARNING,
                                        "Please complete the required fields.");

                        return;
                }

                Integer capacidad = null;

                if (!capacidadTexto.isBlank()) {

                        try {

                                capacidad = Integer.parseInt(
                                                capacidadTexto);

                        } catch (NumberFormatException e) {

                                mostrarMensaje(
                                                Alert.AlertType.WARNING,
                                                "Capacity must be a number.");

                                return;
                        }
                }

                enclosureSelected.setName(
                                nombre);

                enclosureSelected.setSector(
                                sector);

                enclosureSelected.setCapacity(
                                capacidad);

                enclosureSelected.setStatus(
                                cmbEstado.getValue());

                enclosureSelected.setObservations(
                                txtObservaciones
                                                .getText()
                                                .trim());

                enclosureDAO.update(
                                enclosureSelected);

                loadEnclosures();

                limpiar();

                mostrarMensaje(
                                Alert.AlertType.INFORMATION,
                                "Enclosure updated successfully.");
        }

        // =========================================
        // DELETE
        // =========================================

        private void eliminar() {

                if (enclosureSelected == null) {

                        mostrarMensaje(
                                        Alert.AlertType.WARNING,
                                        "Select an enclosure to delete.");

                        return;
                }

                enclosureDAO.delete(
                                enclosureSelected
                                                .getEnclosureId());

                loadEnclosures();

                limpiar();

                mostrarMensaje(
                                Alert.AlertType.INFORMATION,
                                "Enclosure deleted successfully.");
        }

        // =========================================
        // LOAD SELECTED
        // =========================================

        private void cargarenclosureSelected(
                        Enclosure enclosure) {

                enclosureSelected = enclosure;

                txtNombre.setText(
                                enclosure.getName());

                txtSector.setText(
                                enclosure.getSector());

                if (enclosure.getCapacity() != null) {

                        txtCapacidad.setText(
                                        String.valueOf(
                                                        enclosure.getCapacity()));

                } else {

                        txtCapacidad.clear();
                }

                cmbEstado.setValue(
                                enclosure.getStatus());

                txtObservaciones.setText(
                                enclosure.getObservations());
        }

        // =========================================
        // LOAD TABLE
        // =========================================

        private void loadEnclosures() {

                listEnclosures.setAll(
                                enclosureDAO.list());
        }

        // =========================================
        // CLEAR
        // =========================================

        private void limpiar() {

                enclosureSelected = null;

                txtNombre.clear();

                txtSector.clear();

                txtCapacidad.clear();

                cmbEstado.setValue("Active");

                txtObservaciones.clear();

                table.getSelectionModel()
                                .clearSelection();
        }

        // =========================================
        // MESSAGES
        // =========================================

        private void mostrarMensaje(
                        Alert.AlertType tipo,
                        String mensaje) {

                Alert alerta = new Alert(tipo);

                alerta.setTitle("Tatú Carreta");

                alerta.setHeaderText(null);

                alerta.setContentText(mensaje);

                alerta.showAndWait();
        }
}