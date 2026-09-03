package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.model.Species;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewSpecies {

        private final SpeciesDAO speciesDAO = new SpeciesDAO();

        private final TableView<Species> table = new TableView<>();

        private final TextField txtNombre = new TextField();

        public void show() {

                Stage ventana = new Stage();

                // =========================================
                // HEADER
                // =========================================

                Label breadcrumb = new Label("Home / Species Management");

                breadcrumb.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #7A8580;");

                Label titulo = new Label("Species Management");

                titulo.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label subtitulo = new Label(
                                "Manage the species registered in the reserve");

                subtitulo.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #6B756F;");

                VBox encabezado = new VBox(
                                6,
                                breadcrumb,
                                titulo,
                                subtitulo);

                // =========================================
                // SPECIES INFORMATION
                // =========================================

                Label tituloDatos = new Label("Species Information");

                tituloDatos.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #2E4138;");

                Label lblNombre = new Label("Species name");

                lblNombre.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #445149;");

                txtNombre.setPromptText(
                                "E.g.: Birds, Mammals, Reptiles");

                txtNombre.setPrefHeight(38);

                txtNombre.setMaxWidth(
                                Double.MAX_VALUE);

                txtNombre.setStyle(
                                "-fx-background-radius: 8;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-border-color: #D1D8D2;" +
                                                "-fx-padding: 8;");

                VBox campoNombre = new VBox(
                                8,
                                lblNombre,
                                txtNombre);

                // =========================================
                // BUTTONS
                // =========================================

                Button btnLimpiar = new Button("CLEAR");

                Button btnEliminar = new Button("DELETE");

                Button btnModificar = new Button("EDIT");

                Button btnAgregar = new Button("ADD");

                Button btnVolver = new Button("BACK");

                btnLimpiar.setPrefHeight(36);
                btnEliminar.setPrefHeight(36);
                btnModificar.setPrefHeight(36);
                btnAgregar.setPrefHeight(36);
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

                btnAgregar.setStyle(
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
                                btnAgregar);

                botones.setAlignment(
                                Pos.CENTER_RIGHT);

                VBox tarjetaDatos = new VBox(
                                20,
                                tituloDatos,
                                campoNombre,
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

                TableColumn<Species, Integer> columnaId = new TableColumn<>("ID");

                columnaId.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "idEspecie"));

                columnaId.setPrefWidth(120);

                TableColumn<Species, String> columnaNombre = new TableColumn<>(
                                "Species name");

                columnaNombre.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "nombre"));

                table.getColumns().clear();

                table.getColumns().add(columnaId);
                table.getColumns().add(columnaNombre);

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                // The table can grow but does not unnecessarily
                // occupy the entire screen.

                table.setPrefHeight(280);

                table.setMinHeight(200);

                VBox.setVgrow(
                                table,
                                Priority.ALWAYS);

                Label tituloTabla = new Label("Registered Species");

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

                contenedorVolver.setPadding(
                                new Insets(5, 0, 15, 0));

                // =========================================
                // LOAD DATA
                // =========================================

                cargarEspecies();

                // =========================================
                // SELECT
                // =========================================

                table.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (observable,
                                                                anterior,
                                                                seleccionada) -> {

                                                        if (seleccionada != null) {

                                                                txtNombre.setText(
                                                                                seleccionada.getName());
                                                        }
                                                });

                // =========================================
                // ADD
                // =========================================

                btnAgregar.setOnAction(e -> {

                        if (txtNombre.getText().isBlank()) {
                                return;
                        }

                        Species especie = new Species();

                        especie.setName(
                                        txtNombre.getText().trim());

                        speciesDAO.add(especie);

                        limpiarCampos();

                        cargarEspecies();
                });

                // =========================================
                // EDIT
                // =========================================

                btnModificar.setOnAction(e -> {

                        Species seleccionada = table.getSelectionModel()
                                        .getSelectedItem();

                        if (seleccionada == null
                                        || txtNombre.getText().isBlank()) {

                                return;
                        }

                        seleccionada.setName(
                                        txtNombre.getText().trim());

                        speciesDAO.update(
                                        seleccionada);

                        limpiarCampos();

                        cargarEspecies();
                });

                // =========================================
                // DELETE
                // =========================================

                btnEliminar.setOnAction(e -> {

                        Species seleccionada = table.getSelectionModel()
                                        .getSelectedItem();

                        if (seleccionada == null) {
                                return;
                        }

                        speciesDAO.delete(
                                        seleccionada.getSpeciesId());

                        limpiarCampos();

                        cargarEspecies();
                });

                // =========================================
                // CLEAR
                // =========================================

                btnLimpiar.setOnAction(e -> limpiarCampos());

                // =========================================
                // BACK
                // =========================================

                btnVolver.setOnAction(e -> ventana.close());

                // =========================================
                // CONTENT
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

                contenido.setMaxWidth(
                                Double.MAX_VALUE);

                contenido.setStyle(
                                "-fx-background-color: #F4F1E8;");

                // =========================================
                // SCROLL
                // =========================================

                ScrollPane scroll = new ScrollPane(contenido);

                scroll.setFitToWidth(true);

                scroll.setFitToHeight(true);

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

                ventana.setTitle(
                                "Tatú Carreta - Species Management");

                ventana.setMinWidth(900);

                ventana.setMinHeight(650);

                ventana.setScene(escena);

                // IMPORTANT:
                // The window opens maximized so it is
                // never cut off on the screen.

                ventana.setMaximized(true);

                ventana.show();
        }

        // =========================================
        // LOAD SPECIES
        // =========================================

        private void cargarEspecies() {

                ObservableList<Species> lista = FXCollections.observableArrayList(
                                speciesDAO.list());

                table.setItems(lista);
        }

        // =========================================
        // CLEAR
        // =========================================

        private void limpiarCampos() {

                txtNombre.clear();

                table.getSelectionModel()
                                .clearSelection();
        }
}