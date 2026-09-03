package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.SpeciesDAO;
import com.tesina_tatu_carreta.model.Animal;
import com.tesina_tatu_carreta.model.Species;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewAnimals {

        private final AnimalDAO animalDAO = new AnimalDAO();

        private final TableView<Animal> table = new TableView<>();

        private final ComboBox<Species> comboEspecie = new ComboBox<>();

        private final TextField txtNombreVulgar = new TextField();

        private final TextField txtNombreCientifico = new TextField();

        private final TextField txtCantidad = new TextField();

        private final ComboBox<String> comboOrigen = new ComboBox<>();

        private final ComboBox<String> comboEstado = new ComboBox<>();

        private final String COLOR_FONDO = "#F4F2EA";

        private final String COLOR_VERDE = "#23452C";

        public void show() {

                Stage view = new Stage();

                // =====================================
                // HEADER
                // =====================================

                Label ruta = new Label(
                                "Home / Animal Management");

                ruta.setStyle(
                                "-fx-font-size: 11px;" +
                                                "-fx-text-fill: #8A918E;");

                Label titulo = new Label(
                                "Animal Management");

                titulo.setStyle(
                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + COLOR_VERDE + ";");

                Label descripcion = new Label(
                                "Manage the animals registered in the reserve.");

                descripcion.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #727A76;");

                VBox encabezado = new VBox(6);

                encabezado.getChildren().addAll(
                                ruta,
                                titulo,
                                descripcion);

                // =====================================
                // CONFIGURE FIELDS
                // =====================================

                comboEspecie.setPromptText(
                                "Select species");

                txtNombreVulgar.setPromptText(
                                "E.g.: Blue-fronted Amazon");

                txtNombreCientifico.setPromptText(
                                "E.g.: Amazona aestiva");

                txtCantidad.setPromptText(
                                "E.g.: 1");

                comboOrigen.getItems().addAll(
                                "Admission",
                                "Already existing in the reserve");

                comboOrigen.setValue(
                                "Admission");

                comboEstado.getItems().addAll(
                                "Active",
                                "Inactive");

                comboEstado.setValue(
                                "Active");

                cargarEspecies();

                // =====================================
                // FORM
                // =====================================

                Label tituloFormulario = new Label("Animal Information");

                tituloFormulario.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + COLOR_VERDE + ";");

                GridPane formulario = new GridPane();

                formulario.setHgap(20);
                formulario.setVgap(15);

                formulario.setPadding(
                                new Insets(20, 0, 0, 0));

                ColumnConstraints columna1 = new ColumnConstraints();

                columna1.setPercentWidth(50);

                ColumnConstraints columna2 = new ColumnConstraints();

                columna2.setPercentWidth(50);

                formulario.getColumnConstraints().addAll(
                                columna1,
                                columna2);

                // SPECIES

                formulario.add(
                                crearLabelCampo("Species"),
                                0,
                                0);

                formulario.add(
                                comboEspecie,
                                0,
                                1);

                // COMMON NAME

                formulario.add(
                                crearLabelCampo("Common name"),
                                1,
                                0);

                formulario.add(
                                txtNombreVulgar,
                                1,
                                1);

                // SCIENTIFIC NAME

                formulario.add(
                                crearLabelCampo("Scientific name"),
                                0,
                                2);

                formulario.add(
                                txtNombreCientifico,
                                0,
                                3);

                // QUANTITY

                formulario.add(
                                crearLabelCampo(
                                                "Total registered quantity"),
                                1,
                                2);

                formulario.add(
                                txtCantidad,
                                1,
                                3);

                // ORIGIN

                formulario.add(
                                crearLabelCampo("Origin"),
                                0,
                                4);

                formulario.add(
                                comboOrigen,
                                0,
                                5);

                // STATUS

                formulario.add(
                                crearLabelCampo("Status"),
                                1,
                                4);

                formulario.add(
                                comboEstado,
                                1,
                                5);

                configurarCampo(comboEspecie);
                configurarCampo(txtNombreVulgar);
                configurarCampo(txtNombreCientifico);
                configurarCampo(txtCantidad);
                configurarCampo(comboOrigen);
                configurarCampo(comboEstado);

                // =====================================
                // BUTTONS
                // =====================================

                Button btnAgregar = crearBotonPrincipal("ADD");

                Button btnModificar = crearBotonSecundario("EDIT");

                Button btnEliminar = crearBotonEliminar("DELETE");

                Button btnLimpiar = crearBotonSecundario("CLEAR");

                HBox botones = new HBox(12);

                botones.setAlignment(
                                Pos.CENTER_RIGHT);

                botones.setPadding(
                                new Insets(20, 0, 0, 0));

                botones.getChildren().addAll(
                                btnLimpiar,
                                btnEliminar,
                                btnModificar,
                                btnAgregar);

                // =====================================
                // FORM CARD
                // =====================================

                VBox tarjetaFormulario = new VBox();

                tarjetaFormulario.setPadding(
                                new Insets(25));

                tarjetaFormulario.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                tarjetaFormulario.getChildren().addAll(
                                tituloFormulario,
                                formulario,
                                botones);

                // =====================================
                // TABLE
                // =====================================

                Label tituloTabla = new Label(
                                "Registered Animals");

                tituloTabla.setStyle(
                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: " + COLOR_VERDE + ";");

                // ID

                TableColumn<Animal, Integer> columnaId = new TableColumn<>("ID");

                columnaId.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "idAnimal"));

                // COMMON NAME

                TableColumn<Animal, String> columnaVulgar = new TableColumn<>(
                                "Common name");

                columnaVulgar.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "nombreVulgar"));

                // SCIENTIFIC NAME

                TableColumn<Animal, String> columnaCientifico = new TableColumn<>(
                                "Scientific name");

                columnaCientifico.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "nombreCientifico"));

                // QUANTITY

                TableColumn<Animal, Integer> columnaCantidad = new TableColumn<>(
                                "Quantity");

                columnaCantidad.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "cantidadActual"));

                // ORIGIN

                TableColumn<Animal, String> columnaOrigen = new TableColumn<>(
                                "Origin");

                columnaOrigen.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "origen"));

                // STATUS

                TableColumn<Animal, String> columnaEstado = new TableColumn<>(
                                "Status");

                columnaEstado.setCellValueFactory(
                                new PropertyValueFactory<>(
                                                "estado"));

                table.getColumns().add(columnaId);
                table.getColumns().add(columnaVulgar);
                table.getColumns().add(columnaCientifico);
                table.getColumns().add(columnaCantidad);
                table.getColumns().add(columnaOrigen);
                table.getColumns().add(columnaEstado);

                table.setColumnResizePolicy(
                                TableView.CONSTRAINED_RESIZE_POLICY);

                table.setPrefHeight(330);

                table.setMinHeight(330);

                table.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-border-color: #E1E1DC;");

                VBox seccionTabla = new VBox(18);

                seccionTabla.setPadding(
                                new Insets(25));

                seccionTabla.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #DDDAD1;" +
                                                "-fx-border-radius: 14;");

                seccionTabla.getChildren().addAll(
                                tituloTabla,
                                table);

                cargarAnimales();

                // =====================================
                // SELECT ANIMAL
                // =====================================

                table.getSelectionModel()
                                .selectedItemProperty()
                                .addListener(
                                                (
                                                                observable,
                                                                anterior,
                                                                seleccionado) -> {

                                                        if (seleccionado != null) {

                                                                txtNombreVulgar.setText(
                                                                                seleccionado
                                                                                                .getCommonName());

                                                                txtNombreCientifico.setText(
                                                                                seleccionado
                                                                                                .getScientificName());

                                                                txtCantidad.setText(
                                                                                String.valueOf(
                                                                                                seleccionado
                                                                                                                .getCurrentQuantity()));

                                                                comboOrigen.setValue(
                                                                                seleccionado
                                                                                                .getOrigin());

                                                                comboEstado.setValue(
                                                                                seleccionado
                                                                                                .getStatus());

                                                                for (Species especie : comboEspecie.getItems()) {

                                                                        if (especie.getSpeciesId() == seleccionado
                                                                                        .getSpeciesId()) {

                                                                                comboEspecie.setValue(
                                                                                                especie);

                                                                                break;
                                                                        }
                                                                }
                                                        }
                                                });

                // =====================================
                // ADD
                // =====================================

                btnAgregar.setOnAction(e -> {

                        if (comboEspecie.getValue() == null
                                        || txtNombreVulgar
                                                        .getText()
                                                        .isBlank()
                                        || txtNombreCientifico
                                                        .getText()
                                                        .isBlank()
                                        || txtCantidad
                                                        .getText()
                                                        .isBlank()
                                        || comboOrigen
                                                        .getValue() == null
                                        || comboEstado
                                                        .getValue() == null) {

                                return;
                        }

                        try {

                                int cantidad = Integer.parseInt(
                                                txtCantidad
                                                                .getText()
                                                                .trim());

                                if (cantidad <= 0) {

                                        return;
                                }

                                Animal animal = new Animal();

                                animal.setSpeciesId(
                                                comboEspecie
                                                                .getValue()
                                                                .getSpeciesId());

                                animal.setCommonName(
                                                txtNombreVulgar
                                                                .getText()
                                                                .trim());

                                animal.setScientificName(
                                                txtNombreCientifico
                                                                .getText()
                                                                .trim());

                                animal.setCurrentQuantity(
                                                cantidad);

                                animal.setOrigin(
                                                comboOrigen.getValue());

                                animal.setStatus(
                                                comboEstado.getValue());

                                animalDAO.add(animal);

                                limpiarCampos();

                                cargarAnimales();

                        } catch (NumberFormatException error) {

                                System.out.println(
                                                "Quantity must be a number.");
                        }
                });

                // =====================================
                // EDIT
                // =====================================

                btnModificar.setOnAction(e -> {

                        Animal seleccionado = table.getSelectionModel()
                                        .getSelectedItem();

                        if (seleccionado == null
                                        || comboEspecie
                                                        .getValue() == null
                                        || txtCantidad
                                                        .getText()
                                                        .isBlank()
                                        || comboOrigen
                                                        .getValue() == null) {

                                return;
                        }

                        try {

                                int cantidad = Integer.parseInt(
                                                txtCantidad
                                                                .getText()
                                                                .trim());

                                if (cantidad <= 0) {

                                        return;
                                }

                                seleccionado.setSpeciesId(
                                                comboEspecie
                                                                .getValue()
                                                                .getSpeciesId());

                                seleccionado.setCommonName(
                                                txtNombreVulgar
                                                                .getText()
                                                                .trim());

                                seleccionado.setScientificName(
                                                txtNombreCientifico
                                                                .getText()
                                                                .trim());

                                seleccionado.setCurrentQuantity(
                                                cantidad);

                                seleccionado.setOrigin(
                                                comboOrigen.getValue());

                                seleccionado.setStatus(
                                                comboEstado.getValue());

                                animalDAO.update(
                                                seleccionado);

                                limpiarCampos();

                                cargarAnimales();

                        } catch (NumberFormatException error) {

                                System.out.println(
                                                "Quantity must be a number.");
                        }
                });

                // =====================================
                // DELETE
                // =====================================

                btnEliminar.setOnAction(e -> {

                        Animal seleccionado = table.getSelectionModel()
                                        .getSelectedItem();

                        if (seleccionado == null) {

                                return;
                        }

                        animalDAO.delete(
                                        seleccionado.getAnimalId());

                        limpiarCampos();

                        cargarAnimales();
                });

                // =====================================
                // CLEAR
                // =====================================

                btnLimpiar.setOnAction(e -> {

                        limpiarCampos();
                });

                // =====================================
                // MAIN CONTENT
                // =====================================

                VBox contenido = new VBox(25);

                contenido.setPadding(
                                new Insets(35, 40, 35, 40));

                contenido.setFillWidth(true);

                contenido.setStyle(
                                "-fx-background-color: "
                                                + COLOR_FONDO
                                                + ";");

                contenido.getChildren().addAll(
                                encabezado,
                                tarjetaFormulario,
                                seccionTabla);

                // =====================================
                // MAIN SCROLL
                // =====================================

                ScrollPane scroll = new ScrollPane();

                scroll.setContent(contenido);

                scroll.setFitToWidth(true);

                scroll.setFitToHeight(false);

                scroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scroll.setStyle(
                                "-fx-background: " + COLOR_FONDO + ";" +
                                                "-fx-background-color: " + COLOR_FONDO + ";");

                // =====================================
                // SCENE
                // =====================================

                Scene escena = new Scene(
                                scroll,
                                1200,
                                850);

                view.setTitle(
                                "Tatú Carreta - Animal Management");

                view.setScene(escena);

                view.setMinWidth(1000);

                view.setMinHeight(700);

                view.setMaximized(true);

                view.show();
        }

        // =====================================
        // FIELD LABEL
        // =====================================

        private Label crearLabelCampo(
                        String texto) {

                Label label = new Label(texto);

                label.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #4B5752;");

                return label;
        }

        // =====================================
        // CONFIGURE FIELDS
        // =====================================

        private void configurarCampo(
                        Control campo) {

                campo.setMaxWidth(
                                Double.MAX_VALUE);

                campo.setPrefHeight(38);

                campo.setStyle(
                                "-fx-background-color: #FAFAF8;" +
                                                "-fx-border-color: #D7D9D2;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 13px;");
        }

        // =====================================
        // PRIMARY BUTTON
        // =====================================

        private Button crearBotonPrincipal(
                        String texto) {

                Button boton = new Button(texto);

                boton.setPrefHeight(38);

                boton.setStyle(
                                "-fx-background-color: "
                                                + COLOR_VERDE + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 12px;");

                return boton;
        }

        // =====================================
        // SECONDARY BUTTON
        // =====================================

        private Button crearBotonSecundario(
                        String texto) {

                Button boton = new Button(texto);

                boton.setPrefHeight(38);

                boton.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-text-fill: " + COLOR_VERDE + ";" +
                                                "-fx-border-color: #B8C7B8;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 12px;");

                return boton;
        }

        // =====================================
        // DELETE BUTTON
        // =====================================

        private Button crearBotonEliminar(
                        String texto) {

                Button boton = new Button(texto);

                boton.setPrefHeight(38);

                boton.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-text-fill: #A94442;" +
                                                "-fx-border-color: #D8B3B3;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 12px;");

                return boton;
        }

        // =====================================
        // LOAD SPECIES
        // =====================================

        private void cargarEspecies() {

                ObservableList<Species> especies = FXCollections.observableArrayList(
                                new SpeciesDAO().list());

                comboEspecie.setItems(
                                especies);
        }

        // =====================================
        // LOAD ANIMALS
        // =====================================

        private void cargarAnimales() {

                ObservableList<Animal> lista = FXCollections.observableArrayList(
                                animalDAO.list());

                table.setItems(
                                lista);
        }

        // =====================================
        // CLEAR FIELDS
        // =====================================

        private void limpiarCampos() {

                comboEspecie.setValue(null);

                txtNombreVulgar.clear();

                txtNombreCientifico.clear();

                txtCantidad.clear();

                comboOrigen.setValue(
                                "Admission");

                comboEstado.setValue(
                                "Active");

                table.getSelectionModel()
                                .clearSelection();
        }
}