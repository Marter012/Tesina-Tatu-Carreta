package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.model.User;

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

    private static final String BACKGROUND_COLOR =
            "#F4F1EA";

    private static final String MENU_COLOR =
            "#1F3A2E";

    private static final String SECONDARY_MENU_COLOR =
            "#29483A";

    private static final String SELECTION_COLOR =
            "#B8A47E";

    private static final String TEXT_COLOR =
            "#FFFFFF";

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public ViewMain() {
    }

    public ViewMain(
            User currentUser) {

        this.currentUser =
                currentUser;
    }

    // =========================================================
    // START
    // =========================================================

    @Override
    public void start(
            Stage stage) {

        this.stage =
                stage;

        root =
                new BorderPane();

        root.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        root.setStyle(
                "-fx-background-color: "
                        + BACKGROUND_COLOR
                        + ";"
        );

        menu =
                createSidebar();

        root.setLeft(
                menu
        );

        showHomePanel();

        Scene scene =
                new Scene(
                        root,
                        1400,
                        850
                );

        stage.setTitle(
                "Tatú Carreta - Sistema de Gestión"
        );

        stage.setScene(
                scene
        );

        stage.setMinWidth(
                1100
        );

        stage.setMinHeight(
                700
        );

        stage.setMaximized(
                true
        );

        stage.show();
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar =
                new VBox();

        sidebar.setPrefWidth(
                250
        );

        sidebar.setMinWidth(
                250
        );

        sidebar.setPadding(
                new Insets(
                        20,
                        10,
                        20,
                        10
                )
        );

        sidebar.setSpacing(
                8
        );

        sidebar.setStyle(
                "-fx-background-color: "
                        + MENU_COLOR
                        + ";"
        );

        // =====================================================
        // HEADER
        // =====================================================

        Label logo =
                new Label(
                        "TATÚ CARRETA"
                );

        logo.setStyle(
                "-fx-text-fill: "
                        + TEXT_COLOR
                        + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "SISTEMA DE GESTIÓN"
                );

        subtitle.setStyle(
                "-fx-text-fill: #D8D0C0;" +
                "-fx-font-size: 11px;"
        );

        VBox header =
                new VBox(
                        3,
                        logo,
                        subtitle
                );

        header.setPadding(
                new Insets(
                        0,
                        10,
                        20,
                        10
                )
        );

        sidebar.getChildren().add(
                header
        );

        // =====================================================
        // MAIN NAVIGATION
        // =====================================================

        Button homeButton =
                createMenuButton(
                        "⌂",
                        "Inicio"
                );

        Button animalsButton =
                createMenuButton(
                        "🐾",
                        "Animales"
                );

        Button entriesButton =
                createMenuButton(
                        "＋",
                        "Ingresos"
                );

        Button movementsButton =
                createMenuButton(
                        "↔",
                        "Movimientos"
                );

        Button enclosuresButton =
                createMenuButton(
                        "▣",
                        "Recintos"
                );

        Button permanentEnclosureButton =
                createMenuButton(
                        "⌂",
                        "Ubicación permanente"
                );

        Button speciesButton =
                createMenuButton(
                        "◉",
                        "Especies"
                );

        sidebar.getChildren().addAll(
                homeButton,
                animalsButton,
                entriesButton,
                movementsButton,
                enclosuresButton,
                permanentEnclosureButton,
                speciesButton
        );

        // =====================================================
        // ADMINISTRATION
        // =====================================================

        if (currentUser != null
                && "Administrator".equals(
                        currentUser.getRole())) {

            Label administrationLabel =
                    new Label(
                            "ADMINISTRACIÓN"
                    );

            administrationLabel.setStyle(
                    "-fx-text-fill: #AFA99D;" +
                    "-fx-font-size: 10px;" +
                    "-fx-font-weight: bold;"
            );

            administrationLabel.setPadding(
                    new Insets(
                            18,
                            10,
                            5,
                            10
                    )
            );

            sidebar.getChildren().add(
                    administrationLabel
            );

            Button usersButton =
                    createMenuButton(
                            "⚙",
                            "Usuarios"
                    );

            sidebar.getChildren().add(
                    usersButton
            );

            usersButton.setOnAction(
                    event -> {

                        selectButton(
                                usersButton
                        );

                        showUserPanel();
                    }
            );
        }

        // =====================================================
        // SPACER
        // =====================================================

        VBox spacer =
                new VBox();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        sidebar.getChildren().add(
                spacer
        );

        // =====================================================
        // CURRENT USER
        // =====================================================

        Label userNameLabel =
                new Label(
                        currentUser != null
                                ? currentUser.getUsername()
                                : ""
                );

        userNameLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_COLOR
                        + ";" +
                "-fx-font-weight: bold;"
        );

        Label userRoleLabel =
                new Label(
                        currentUser != null
                                ? translateRole(
                                        currentUser.getRole()
                                )
                                : ""
                );

        userRoleLabel.setStyle(
                "-fx-text-fill: #C9C2B4;" +
                "-fx-font-size: 11px;"
        );

        VBox userData =
                new VBox(
                        2,
                        userNameLabel,
                        userRoleLabel
                );

        userData.setPadding(
                new Insets(10)
        );

        HBox userBox =
                new HBox(
                        10,
                        userData
                );

        userBox.setAlignment(
                Pos.CENTER_LEFT
        );

        sidebar.getChildren().add(
                userBox
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        Button logoutButton =
                createMenuButton(
                        "↪",
                        "Cerrar sesión"
                );

        logoutButton.setOnAction(
                event -> logout()
        );

        sidebar.getChildren().add(
                logoutButton
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        homeButton.setOnAction(
                event -> {

                    selectButton(
                            homeButton
                    );

                    showHomePanel();
                }
        );

        animalsButton.setOnAction(
                event -> {

                    selectButton(
                            animalsButton
                    );

                    showAnimalsPanel();
                }
        );

        entriesButton.setOnAction(
                event -> {

                    selectButton(
                            entriesButton
                    );

                    showEntryPanel();
                }
        );

        movementsButton.setOnAction(
                event -> {

                    selectButton(
                            movementsButton
                    );

                    showMovementPanel();
                }
        );

        enclosuresButton.setOnAction(
                event -> {

                    selectButton(
                            enclosuresButton
                    );

                    showEnclosuresPanel();
                }
        );

        permanentEnclosureButton.setOnAction(
                event -> {

                    selectButton(
                            permanentEnclosureButton
                    );

                    showPermanentEnclosurePanel();
                }
        );

        speciesButton.setOnAction(
                event -> {

                    selectButton(
                            speciesButton
                    );

                    showSpeciesPanel();
                }
        );

        // =====================================================
        // DEFAULT SELECTION
        // =====================================================

        selectButton(
                homeButton
        );

        return sidebar;
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private Button createMenuButton(
            String icon,
            String text) {

        Button button =
                new Button(
                        icon + "   " + text
                );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setPadding(
                new Insets(
                        12,
                        15,
                        12,
                        15
                )
        );

        button.setStyle(
                normalButtonStyle()
        );

        button.setOnMouseEntered(
                event ->
                        button.setStyle(
                                hoverButtonStyle()
                        )
        );

        button.setOnMouseExited(
                event -> {

                    if (button != selectedButton) {

                        button.setStyle(
                                normalButtonStyle()
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // SELECT MENU BUTTON
    // =========================================================

    private void selectButton(
            Button button) {

        if (selectedButton != null) {

            selectedButton.setStyle(
                    normalButtonStyle()
            );
        }

        selectedButton =
                button;

        selectedButton.setStyle(
                selectedButtonStyle()
        );
    }

    // =========================================================
    // MENU STYLES
    // =========================================================

    private String normalButtonStyle() {

        return "-fx-background-color: transparent;" +
                "-fx-text-fill: "
                + TEXT_COLOR
                + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;";
    }

    private String hoverButtonStyle() {

        return "-fx-background-color: "
                + SECONDARY_MENU_COLOR
                + ";" +
                "-fx-text-fill: "
                + TEXT_COLOR
                + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;";
    }

    private String selectedButtonStyle() {

        return "-fx-background-color: "
                + SELECTION_COLOR
                + ";" +
                "-fx-text-fill: "
                + TEXT_COLOR
                + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;";
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
                    Double.MAX_VALUE
            );
        }

        root.setCenter(
                content
        );
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHomePanel() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        content.setMaxSize(
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        Label breadcrumb =
                new Label(
                        "Inicio / Resumen del sistema"
                );

        breadcrumb.setStyle(
                "-fx-text-fill: #777777;" +
                "-fx-font-size: 12px;"
        );

        String userName =
                currentUser != null
                        ? currentUser.getUsername()
                        : "";

        Label title =
                new Label(
                        "Hola, " + userName
                );

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label description =
                new Label(
                        "Bienvenido al panel de gestión "
                                + "de la Reserva Natural Tatú Carreta."
                );

        description.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #666666;"
        );

        VBox header =
                new VBox(
                        5,
                        breadcrumb,
                        title,
                        description
                );

        // =====================================================
        // DASHBOARD CARDS
        // =====================================================

        HBox cards =
                new HBox(15);

        cards.setMaxWidth(
                Double.MAX_VALUE
        );

        VBox animalsCard =
                createCard(
                        "ANIMALES ACTIVOS",
                        "0",
                        "Actualmente registrados"
                );

        VBox quarantineCard =
                createCard(
                        "EN CUARENTENA",
                        "0",
                        "Animales en observación"
                );

        VBox entriesCard =
                createCard(
                        "INGRESOS",
                        "0",
                        "Ingresos registrados"
                );

        VBox enclosuresCard =
                createCard(
                        "RECINTOS",
                        "0",
                        "Recintos registrados"
                );

        cards.getChildren().addAll(
                animalsCard,
                quarantineCard,
                entriesCard,
                enclosuresCard
        );

        // =====================================================
        // RECENT ACTIVITY
        // =====================================================

        Label activityTitle =
                new Label(
                        "Actividad reciente"
                );

        activityTitle.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        Label noActivity =
                new Label(
                        "Los últimos ingresos, movimientos y "
                                + "cambios registrados aparecerán aquí."
                );

        noActivity.setStyle(
                "-fx-text-fill: #777777;"
        );

        VBox activity =
                new VBox(
                        10,
                        activityTitle,
                        noActivity
                );

        content.getChildren().addAll(
                header,
                cards,
                activity
        );

        VBox.setVgrow(
                activity,
                Priority.ALWAYS
        );

        setContent(
                content
        );
    }

    // =========================================================
    // ANIMALS
    // =========================================================

    private void showAnimalsPanel() {

        try {

            ViewAnimals animalsView =
                    new ViewAnimals();

            setContent(
                    animalsView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar la gestión de animales.",
                    error
            );
        }
    }

    // =========================================================
    // ENTRIES
    // =========================================================

    private void showEntryPanel() {

        try {

            ViewEntry entryView =
                    new ViewEntry();

            setContent(
                    entryView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar la gestión de ingresos.",
                    error
            );
        }
    }

    // =========================================================
    // MOVEMENTS
    // =========================================================

    private void showMovementPanel() {

        try {

            ViewMovement movementView =
                    new ViewMovement();

            setContent(
                    movementView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar los movimientos.",
                    error
            );
        }
    }

    // =========================================================
    // ENCLOSURES
    // =========================================================

    private void showEnclosuresPanel() {

        try {

            ViewEnclosures enclosuresView =
                    new ViewEnclosures();

            setContent(
                    enclosuresView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar los recintos.",
                    error
            );
        }
    }

    // =========================================================
    // PERMANENT ENCLOSURE
    // =========================================================

    private void showPermanentEnclosurePanel() {

        try {

            ViewPermanentEnclosure
                    permanentEnclosureView =
                    new ViewPermanentEnclosure();

            setContent(
                    permanentEnclosureView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar las ubicaciones permanentes.",
                    error
            );
        }
    }

    // =========================================================
    // SPECIES
    // =========================================================

    private void showSpeciesPanel() {

        try {

            ViewSpecies speciesView =
                    new ViewSpecies();

            setContent(
                    speciesView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar la gestión de especies.",
                    error
            );
        }
    }

    // =========================================================
    // USERS
    // =========================================================

    private void showUserPanel() {

        try {

            ViewUser userView =
                    new ViewUser();

            setContent(
                    userView.getView()
            );

        } catch (Exception error) {

            showError(
                    "Error al cargar la gestión de usuarios.",
                    error
            );
        }
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        try {

            Stage loginStage =
                    new Stage();

            new ViewLogin().show(
                    loginStage
            );

            stage.close();

        } catch (Exception error) {

            showError(
                    "No se pudo cerrar la sesión.",
                    error
            );
        }
    }

    // =========================================================
    // DASHBOARD CARD
    // =========================================================

    private VBox createCard(
            String title,
            String number,
            String description) {

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #777777;"
        );

        Label numberLabel =
                new Label(number);

        numberLabel.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #888888;"
        );

        VBox card =
                new VBox(
                        8,
                        titleLabel,
                        numberLabel,
                        descriptionLabel
                );

        card.setPadding(
                new Insets(20)
        );

        card.setPrefWidth(
                210
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: #DDDDDD;"
        );

        HBox.setHgrow(
                card,
                Priority.ALWAYS
        );

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
                message
        );

        error.printStackTrace();
    }
}