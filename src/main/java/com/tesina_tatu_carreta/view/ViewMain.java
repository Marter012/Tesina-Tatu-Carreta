package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.model.User;
import com.tesina_tatu_carreta.dao.AnimalHoldingDAO;
import com.tesina_tatu_carreta.service.AnimalInventoryService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewMain extends Application {

        private User currentUser;

        private VBox menu;

        private Button selectedButton;

        private BorderPane root;

        private Stage stage;

        private final AnimalHoldingDAO holdingDAO = new AnimalHoldingDAO();

        private static final String BACKGROUND_COLOR = "#F4F1EA";

        private static final String MENU_COLOR = "#1F3A2E";

        private static final String SECONDARY_MENU_COLOR = "#29483A";

        private static final String SELECTION_COLOR = "#B8A47E";

        private static final String TEXT_COLOR = "#FFFFFF";

        // =========================================================
        // CONSTRUCTORS
        // =========================================================

        public ViewMain() {
        }

        public ViewMain(User currentUser) {

                this.currentUser = currentUser;
        }

        // =========================================================
        // START
        // =========================================================

        @Override
        public void start(Stage stage) {

                this.stage = stage;

                root = new BorderPane();

                root.setMaxSize(
                                Double.MAX_VALUE,
                                Double.MAX_VALUE);

                root.setStyle(
                                "-fx-background-color: "
                                                + BACKGROUND_COLOR
                                                + ";");

                menu = createSidebar();

                root.setLeft(
                                menu);

                showHomePanel();

                Scene scene = new Scene(
                                root,
                                1400,
                                850);

                stage.setTitle(
                                "Tatú Carreta - Sistema de Gestión");

                stage.setScene(
                                scene);

                stage.setMinWidth(
                                1100);

                stage.setMinHeight(
                                700);

                stage.setMaximized(
                                true);

                stage.show();
        }

        // =========================================================
        // SIDEBAR
        // =========================================================

        private VBox createSidebar() {

                VBox sidebar = new VBox();

                sidebar.setPrefWidth(
                                250);

                sidebar.setMinWidth(
                                250);

                sidebar.setPadding(
                                new Insets(
                                                20,
                                                10,
                                                20,
                                                10));

                sidebar.setSpacing(
                                8);

                sidebar.setStyle(
                                "-fx-background-color: "
                                                + MENU_COLOR
                                                + ";");

                // =====================================================
                // HEADER
                // =====================================================

                Label logo = new Label(
                                "TATÚ CARRETA");

                logo.setStyle(
                                "-fx-text-fill: "
                                                + TEXT_COLOR
                                                + ";"
                                                + "-fx-font-size: 22px;"
                                                + "-fx-font-weight: bold;");

                Label subtitle = new Label(
                                "SISTEMA DE GESTIÓN");

                subtitle.setStyle(
                                "-fx-text-fill: #D8D0C0;"
                                                + "-fx-font-size: 11px;");

                VBox header = new VBox(
                                3,
                                logo,
                                subtitle);

                header.setPadding(
                                new Insets(
                                                0,
                                                10,
                                                20,
                                                10));

                sidebar.getChildren().add(
                                header);

                // =====================================================
                // MAIN NAVIGATION
                // =====================================================

                Button homeButton = createMenuButton(
                                "⌂",
                                "Inicio");

                Button animalsButton = createMenuButton(
                                "🐾",
                                "Animales");

                Button entriesButton = createMenuButton(
                                "＋",
                                "Ingresos");

                Button movementsButton = createMenuButton(
                                "↔",
                                "Movimientos");

                Button permanentEnclosureButton = createMenuButton(
                                "⌂",
                                "Plantel permanente");

                // =====================================================
                // ACCIONES DE LOS BOTONES
                // =====================================================

                // INICIO
                homeButton.setOnAction(
                                event -> {

                                        selectButton(homeButton);

                                        showHomePanel();
                                });

                // ANIMALES
                animalsButton.setOnAction(
                                event -> {

                                        selectButton(animalsButton);

                                        showAnimalsPanel();
                                });

                // INGRESOS
                entriesButton.setOnAction(
                                event -> {

                                        selectButton(entriesButton);

                                        showEntryPanel();
                                });

                // MOVIMIENTOS
                movementsButton.setOnAction(
                                event -> {

                                        selectButton(movementsButton);

                                        showMovementPanel();
                                });

                // PLANTEL PERMANENTE
                permanentEnclosureButton.setOnAction(
                                event -> {

                                        selectButton(
                                                        permanentEnclosureButton);

                                        showPermanentEnclosurePanel();
                                });

                // =====================================================
                // AGREGAR BOTONES AL SIDEBAR
                // =====================================================

                sidebar.getChildren().addAll(
                                homeButton,
                                animalsButton,
                                entriesButton,
                                movementsButton,
                                permanentEnclosureButton);

                // =====================================================
                // ADMINISTRATION
                // =====================================================

                if (currentUser != null
                                && "Administrator".equals(
                                                currentUser.getRole())) {

                        Label administrationLabel = new Label(
                                        "ADMINISTRACIÓN");

                        administrationLabel.setStyle(
                                        "-fx-text-fill: #AFA99D;"
                                                        + "-fx-font-size: 10px;"
                                                        + "-fx-font-weight: bold;");

                        administrationLabel.setPadding(
                                        new Insets(
                                                        18,
                                                        10,
                                                        5,
                                                        10));

                        sidebar.getChildren().add(
                                        administrationLabel);

                        Button usersButton = createMenuButton(
                                        "⚙",
                                        "Usuarios");

                        sidebar.getChildren().add(
                                        usersButton);

                        usersButton.setOnAction(
                                        event -> {

                                                selectButton(
                                                                usersButton);

                                                showUserPanel();
                                        });
                }

                // =====================================================
                // INICIO SELECCIONADO POR DEFECTO
                // =====================================================

                selectButton(homeButton);

                // =====================================================
                // SPACER
                // =====================================================

                VBox spacer = new VBox();

                sidebar.getChildren().add(
                                spacer);

                // =====================================================
                // CURRENT USER
                // =====================================================

                Label userNameLabel = new Label(
                                currentUser != null
                                                ? currentUser.getUsername()
                                                : "");

                userNameLabel.setStyle(
                                "-fx-text-fill: "
                                                + TEXT_COLOR
                                                + ";"
                                                + "-fx-font-weight: bold;");

                Label userRoleLabel = new Label(
                                currentUser != null
                                                ? translateRole(
                                                                currentUser.getRole())
                                                : "");

                userRoleLabel.setStyle(
                                "-fx-text-fill: #C9C2B4;"
                                                + "-fx-font-size: 11px;");

                VBox userData = new VBox(
                                2,
                                userNameLabel,
                                userRoleLabel);

                userData.setPadding(
                                new Insets(10));

                HBox userBox = new HBox(
                                10,
                                userData);

                userBox.setAlignment(
                                Pos.CENTER_LEFT);

                sidebar.getChildren().add(
                                userBox);

                // =====================================================
                // LOGOUT
                // =====================================================

                Button logoutButton = createMenuButton(
                                "↪",
                                "Cerrar sesión");

                logoutButton.setOnAction(
                                event -> logout());

                sidebar.getChildren().add(
                                logoutButton);
                // =====================================================
                // BUTTON ACTIONS
                // =====================================================
                homeButton.setOnAction(
                                event -> {

                                        selectButton(
                                                        homeButton);

                                        showHomePanel();
                                });

                animalsButton.setOnAction(
                                event -> {

                                        selectButton(
                                                        animalsButton);

                                        showAnimalsPanel();
                                });

                entriesButton.setOnAction(
                                event -> {

                                        selectButton(
                                                        entriesButton);

                                        showEntryPanel();
                                });

                movementsButton.setOnAction(
                                event -> {

                                        selectButton(
                                                        movementsButton);

                                        showMovementPanel();
                                });

                permanentEnclosureButton.setOnAction(
                                event -> {

                                        selectButton(
                                                        permanentEnclosureButton);

                                        showPermanentEnclosurePanel();
                                });

                // =====================================================
                // DEFAULT SELECTION
                // =====================================================

                selectButton(
                                homeButton);

                return sidebar;
        }

        // =========================================================
        // MENU BUTTON
        // =========================================================

        private Button createMenuButton(
                        String icon,
                        String text) {

                Button button = new Button(
                                icon + "   " + text);

                button.setMaxWidth(
                                Double.MAX_VALUE);

                button.setAlignment(
                                Pos.CENTER_LEFT);

                button.setPadding(
                                new Insets(
                                                12,
                                                15,
                                                12,
                                                15));

                button.setStyle(
                                normalButtonStyle());

                button.setOnMouseEntered(
                                event -> button.setStyle(
                                                hoverButtonStyle()));

                button.setOnMouseExited(
                                event -> {

                                        if (button != selectedButton) {

                                                button.setStyle(
                                                                normalButtonStyle());
                                        }
                                });

                return button;
        }

        // =========================================================
        // SELECT MENU BUTTON
        // =========================================================

        private void selectButton(
                        Button button) {

                if (selectedButton != null) {

                        selectedButton.setStyle(
                                        normalButtonStyle());
                }

                selectedButton = button;

                selectedButton.setStyle(
                                selectedButtonStyle());
        }

        // =========================================================
        // MENU STYLES
        // =========================================================

        private String normalButtonStyle() {

                return "-fx-background-color: transparent;"
                                + "-fx-text-fill: "
                                + TEXT_COLOR
                                + ";"
                                + "-fx-background-radius: 6;"
                                + "-fx-font-size: 13px;";
        }

        private String hoverButtonStyle() {

                return "-fx-background-color: "
                                + SECONDARY_MENU_COLOR
                                + ";"
                                + "-fx-text-fill: "
                                + TEXT_COLOR
                                + ";"
                                + "-fx-background-radius: 6;"
                                + "-fx-font-size: 13px;";
        }

        private String selectedButtonStyle() {

                return "-fx-background-color: "
                                + SELECTION_COLOR
                                + ";"
                                + "-fx-text-fill: "
                                + TEXT_COLOR
                                + ";"
                                + "-fx-background-radius: 6;"
                                + "-fx-font-size: 13px;";
        }

        // =========================================================
        // CONTENT
        // =========================================================

        private void setContent(
                        Parent content) {

                if (content == null) {
                        return;
                }

                if (content instanceof javafx.scene.layout.Region region) {

                        region.setMaxSize(
                                        Double.MAX_VALUE,
                                        Double.MAX_VALUE);
                }

                root.setCenter(
                                content);
        }

        // =========================================================
        // HOME
        // =========================================================

        private void showHomePanel() {

                VBox content = new VBox(20);

                content.setPadding(
                                new Insets(30));

                content.setMaxSize(
                                Double.MAX_VALUE,
                                Double.MAX_VALUE);

                Label breadcrumb = new Label(
                                "Inicio / Resumen del sistema");

                breadcrumb.setStyle(
                                "-fx-text-fill: #777777;"
                                                + "-fx-font-size: 12px;");

                String userName = currentUser != null
                                ? currentUser.getUsername()
                                : "";

                Label title = new Label(
                                "Hola, " + userName);

                title.setStyle(
                                "-fx-font-size: 28px;"
                                                + "-fx-font-weight: bold;");

                Label description = new Label(
                                "Bienvenido al panel de gestión "
                                                + "de la Reserva Natural Tatú Carreta.");

                description.setStyle(
                                "-fx-font-size: 14px;"
                                                + "-fx-text-fill: #666666;");

                VBox header = new VBox(
                                5,
                                breadcrumb,
                                title,
                                description);

                // =====================================================
                // DASHBOARD CARDS
                // =====================================================

                int quarantine = holdingDAO.getTotalQuantity(
                                AnimalInventoryService.QUARANTINE);

                int permanent = holdingDAO.getTotalQuantity(
                                AnimalInventoryService.PERMANENT);

                int activeAnimals = quarantine + permanent;

                HBox cards = new HBox(20);

                cards.setMaxWidth(
                                Double.MAX_VALUE);

                VBox animalsCard = createCard(
                                "ANIMALES ACTIVOS",
                                String.valueOf(activeAnimals),
                                "Animales vivos actualmente en la reserva");

                VBox permanentCard = createCard(
                                "ANIMALES PERMANENTES",
                                String.valueOf(permanent),
                                "Animales vivos en plantel permanente");

                VBox quarantineCard = createCard(
                                "ANIMALES EN CUARENTENA",
                                String.valueOf(quarantine),
                                "Animales vivos en cuarentena");

                // IMPORTANTE:
                // Agregamos las 3 tarjetas al HBox
                cards.getChildren().addAll(
                                animalsCard,
                                permanentCard,
                                quarantineCard);

                // =====================================================
                // HOW TO USE
                // =====================================================

                Label helpTitle = new Label(
                                "¿Cómo usar el sistema?");

                helpTitle.setStyle(
                                "-fx-font-size: 22px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-text-fill: #23452C;");

                Label helpDescription = new Label(
                                "Desde este sistema puede registrar y consultar "
                                                + "la información de los animales de la Reserva Natural "
                                                + "Tatú Carreta. Para comenzar, seleccione una opción "
                                                + "del menú de la izquierda.");

                helpDescription.setWrapText(true);

                helpDescription.setStyle(
                                "-fx-font-size: 14px;"
                                                + "-fx-text-fill: #666666;");

                // =====================================================
                // ANIMALES
                // =====================================================

                Label animalsHelp = new Label(
                                "🐾  ANIMALES\n"
                                                + "Desde esta sección puede gestionar la información "
                                                + "de los animales registrados en la reserva.\n\n"
                                                + "• Registrar un nuevo animal.\n"
                                                + "• Consultar los animales existentes.\n"
                                                + "• Consultar su especie, nombre y demás información.\n"
                                                + "• Ver la cantidad disponible de cada animal.\n"
                                                + "• Realizar acciones sobre los animales, como traslados, "
                                                + "salidas o registrar una muerte.");

                // =====================================================
                // INGRESOS
                // =====================================================

                Label entriesHelp = new Label(
                                "➕  INGRESOS\n"
                                                + "Utilice esta sección cuando ingrese un animal a la reserva.\n\n"
                                                + "Aquí se registra el ingreso y la información correspondiente "
                                                + "al acta o registro de ingreso.\n\n"
                                                + "El ingreso permite dejar constancia de cuándo y cómo "
                                                + "ingresó el animal, su cantidad y el lugar inicial "
                                                + "donde será alojado.");

                // =====================================================
                // MOVIMIENTOS
                // =====================================================

                Label movementsHelp = new Label(
                                "↔  MOVIMIENTOS\n"
                                                + "Utilice esta sección para registrar cambios que ocurren "
                                                + "con los animales después de su ingreso.\n\n"
                                                + "• Traslados: cuando un animal cambia de ubicación.\n"
                                                + "• Salidas: cuando un animal deja la reserva.\n"
                                                + "• Muertes: cuando corresponde registrar el fallecimiento "
                                                + "de uno o más animales.\n\n"
                                                + "Los movimientos permiten mantener un historial de lo que "
                                                + "ocurrió con cada animal y conservar su trazabilidad.");

                // =====================================================
                // PLANTEL PERMANENTE
                // =====================================================

                Label permanentHelp = new Label(
                                "🏠  PLANTEL PERMANENTE\n"
                                                + "Esta sección permite consultar los animales que se "
                                                + "encuentran actualmente alojados en el plantel permanente.\n\n"
                                                + "Aquí puede visualizar qué animales se encuentran en "
                                                + "esta ubicación y consultar la información disponible "
                                                + "sobre ellos.");

                // =====================================================
                // DIFERENCIA ENTRE INGRESOS Y MOVIMIENTOS
                // =====================================================

                Label differenceHelp = new Label(
                                "💡  ¿Cuál es la diferencia entre INGRESOS y MOVIMIENTOS?\n"
                                                + "Un INGRESO se utiliza cuando un animal entra a la reserva "
                                                + "por primera vez o se registra su incorporación.\n\n"
                                                + "Un MOVIMIENTO se utiliza cuando un animal que ya está "
                                                + "registrado cambia su situación, por ejemplo, cuando "
                                                + "es trasladado, sale de la reserva o fallece.");

                // =====================================================
                // INFORMACIÓN DEL INICIO
                // =====================================================

                Label dashboardHelp = new Label(
                                "📊  INFORMACIÓN DEL INICIO\n"
                                                + "Las tarjetas que aparecen arriba muestran un resumen "
                                                + "actual de los animales de la reserva.\n\n"
                                                + "• ANIMALES ACTIVOS: cantidad total de animales vivos "
                                                + "que se encuentran actualmente en la reserva.\n"
                                                + "• ANIMALES PERMANENTES: animales vivos alojados en "
                                                + "el plantel permanente.\n"
                                                + "• ANIMALES EN CUARENTENA: animales vivos que se "
                                                + "encuentran actualmente en cuarentena.");

                // =====================================================
                // ESTILO DE LOS BLOQUES
                // =====================================================

                for (Label help : new Label[] {
                                animalsHelp,
                                entriesHelp,
                                movementsHelp,
                                permanentHelp,
                                differenceHelp,
                                dashboardHelp
                }) {

                        help.setWrapText(true);

                        help.setStyle(
                                        "-fx-font-size: 13px;"
                                                        + "-fx-text-fill: #555555;"
                                                        + "-fx-line-spacing: 3px;");
                }

                // =====================================================
                // CONTENEDOR DE AYUDA
                // =====================================================

                VBox helpContent = new VBox(
                                15,
                                helpTitle,
                                helpDescription,
                                animalsHelp,
                                entriesHelp,
                                movementsHelp,
                                permanentHelp,
                                differenceHelp,
                                dashboardHelp);

                helpContent.setPadding(
                                new Insets(25));

                helpContent.setMaxWidth(
                                Double.MAX_VALUE);

                helpContent.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-background-radius: 12;"
                                                + "-fx-border-color: #DDDAD1;"
                                                + "-fx-border-radius: 12;");

                content.getChildren().addAll(
                                header,
                                cards,
                                helpContent);

                // =====================================================
                // SHOW HOME
                // =====================================================

                setContent(content);
        }

        // =========================================================
        // ANIMALS
        // =========================================================

        private void showAnimalsPanel() {

                try {

                        ViewAnimals animalsView = new ViewAnimals();

                        setContent(
                                        animalsView.getView());

                } catch (Exception error) {

                        showError(
                                        "Error al cargar la gestión de animales.",
                                        error);
                }
        }

        // =========================================================
        // ENTRIES
        // =========================================================

        private void showEntryPanel() {

                try {

                        ViewEntry entryView = new ViewEntry();

                        setContent(
                                        entryView.getView());

                } catch (Exception error) {

                        showError(
                                        "Error al cargar la gestión de ingresos.",
                                        error);
                }
        }

        // =========================================================
        // MOVEMENTS
        // =========================================================

        private void showMovementPanel() {

                try {

                        ViewMovement movementView = new ViewMovement();

                        setContent(
                                        movementView.getView());

                } catch (Exception error) {

                        showError(
                                        "Error al cargar los movimientos.",
                                        error);
                }
        }

        // =========================================================
        // PERMANENT ENCLOSURE
        // =========================================================

        private void showPermanentEnclosurePanel() {

                try {

                        ViewPermanentEnclosure permanentEnclosureView = new ViewPermanentEnclosure();

                        setContent(
                                        permanentEnclosureView.getView());

                } catch (Exception error) {

                        showError(
                                        "Error al cargar el plantel permanente.",
                                        error);
                }
        }

        // =========================================================
        // USERS
        // =========================================================

        private void showUserPanel() {

                try {

                        ViewUser userView = new ViewUser();

                        setContent(
                                        userView.getView());

                } catch (Exception error) {

                        showError(
                                        "Error al cargar la gestión de usuarios.",
                                        error);
                }
        }

        // =========================================================
        // LOGOUT
        // =========================================================

        private void logout() {

                try {

                        Stage loginStage = new Stage();

                        new ViewLogin().show(
                                        loginStage);

                        stage.close();

                } catch (Exception error) {

                        showError(
                                        "No se pudo cerrar la sesión.",
                                        error);
                }
        }

        // =========================================================
        // DASHBOARD CARD
        // =========================================================

        private VBox createCard(
                        String title,
                        String number,
                        String description) {

                Label titleLabel = new Label(title);

                titleLabel.setStyle(
                                "-fx-font-size: 11px;"
                                                + "-fx-font-weight: bold;"
                                                + "-fx-text-fill: #777777;");

                Label numberLabel = new Label(number);

                numberLabel.setStyle(
                                "-fx-font-size: 28px;"
                                                + "-fx-font-weight: bold;");

                Label descriptionLabel = new Label(description);

                descriptionLabel.setStyle(
                                "-fx-font-size: 11px;"
                                                + "-fx-text-fill: #888888;");

                VBox card = new VBox(
                                8,
                                titleLabel,
                                numberLabel,
                                descriptionLabel);

                card.setPadding(
                                new Insets(20));

                card.setPrefWidth(
                                210);

                card.setMaxWidth(
                                Double.MAX_VALUE);

                card.setStyle(
                                "-fx-background-color: white;"
                                                + "-fx-background-radius: 10;"
                                                + "-fx-border-radius: 10;"
                                                + "-fx-border-color: #DDDDDD;");

                HBox.setHgrow(
                                card,
                                Priority.ALWAYS);

                return card;
        }

        // =========================================================
        // ROLE TRANSLATION
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

        // =========================================================
        // ERROR
        // =========================================================

        private void showError(
                        String message,
                        Exception error) {

                System.err.println(
                                message);

                error.printStackTrace();
        }
}