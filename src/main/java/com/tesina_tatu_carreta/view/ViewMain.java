package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.model.User;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    private static final String BACKGROUND_COLOR = "#F4F1EA";
    private static final String MENU_COLOR = "#1F3A2E";
    private static final String SECONDARY_MENU_COLOR = "#29483A";
    private static final String SELECTION_COLOR = "#B8A47E";
    private static final String TEXT_COLOR = "#FFFFFF";

    public ViewMain() {
    }

    public ViewMain(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        root = new BorderPane();
        root.setStyle(
                "-fx-background-color: " + BACKGROUND_COLOR + ";"
        );

        menu = createSidebar();

        root.setLeft(menu);

        showHomePanel();

        Scene scene = new Scene(root, 1200, 750);

        stage.setTitle(
                "Tatú Carreta - Management System"
        );

        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar() {

        VBox sidebar = new VBox();
        sidebar.setPrefWidth(250);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setSpacing(8);

        sidebar.setStyle(
                "-fx-background-color: " + MENU_COLOR + ";"
        );

        Label logo = new Label("TATÚ CARRETA");
        logo.setStyle(
                "-fx-text-fill: " + TEXT_COLOR + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle = new Label("SYSTEM MANAGEMENT");
        subtitle.setStyle(
                "-fx-text-fill: #D8D0C0;" +
                "-fx-font-size: 11px;"
        );

        VBox header = new VBox(3, logo, subtitle);
        header.setPadding(new Insets(0, 10, 20, 10));

        sidebar.getChildren().add(header);

        Button homeButton =
                createMenuButton("⌂", "System Overview");

        Button animalsButton =
                createMenuButton("🐾", "Animal Management");

        Button entriesButton =
                createMenuButton("＋", "Register Entry");

        Button movementsButton =
                createMenuButton("↔", "Entry History");

        Button enclosuresButton =
                createMenuButton("▣", "Enclosures");

        Button permanentEnclosureButton =
                createMenuButton("▤", "Permanent Enclosure");

        Button speciesButton =
                createMenuButton("◉", "Species");

        Button usersButton =
                createMenuButton("⚙", "User Management");

        sidebar.getChildren().addAll(
                homeButton,
                animalsButton,
                entriesButton,
                movementsButton,
                enclosuresButton,
                permanentEnclosureButton,
                speciesButton
        );

        if (currentUser != null &&
                "Administrator".equals(
                        currentUser.getRole()
                )) {

            sidebar.getChildren().add(usersButton);
        }

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().add(spacer);

        Label userNameLabel = new Label(
                currentUser != null
                        ? currentUser.getUsername()
                        : ""
        );

        userNameLabel.setStyle(
                "-fx-text-fill: " + TEXT_COLOR + ";" +
                "-fx-font-weight: bold;"
        );

        Label userRoleLabel = new Label(
                currentUser != null
                        ? currentUser.getRole()
                        : ""
        );

        userRoleLabel.setStyle(
                "-fx-text-fill: #C9C2B4;" +
                "-fx-font-size: 11px;"
        );

        VBox userData = new VBox(
                2,
                userNameLabel,
                userRoleLabel
        );

        userData.setPadding(
                new Insets(10)
        );

        Button logoutButton =
                createMenuButton("↪", "Log Out");

        logoutButton.setOnAction(event -> {

            stage.close();

            new ViewLogin().show(
                    new Stage()
            );
        });

        HBox userBox = new HBox(
                10,
                userData
        );

        userBox.setAlignment(Pos.CENTER_LEFT);

        sidebar.getChildren().addAll(
                userBox,
                logoutButton
        );

        homeButton.setOnAction(event -> {
            selectButton(homeButton);
            showHomePanel();
        });

        animalsButton.setOnAction(event -> {
            selectButton(animalsButton);
            stage.close();
            new ViewAnimals();
        });

        entriesButton.setOnAction(event -> {
            selectButton(entriesButton);
            stage.close();
            new ViewEntry().show();
        });

        movementsButton.setOnAction(event -> {
            selectButton(movementsButton);
            stage.close();
            new ViewMovement().show(stage);
        });

        enclosuresButton.setOnAction(event -> {
            selectButton(enclosuresButton);
            stage.close();
            new ViewEnclosures().show();
        });

        permanentEnclosureButton.setOnAction(event -> {
            selectButton(permanentEnclosureButton);
            stage.close();
            new ViewPermanentEnclosure().show();
        });

        speciesButton.setOnAction(event -> {
            selectButton(speciesButton);
            stage.close();
            new ViewSpecies().show();
        });

        usersButton.setOnAction(event -> {
            selectButton(usersButton);
            stage.close();
            new ViewUser().show();
        });

        selectButton(homeButton);

        return sidebar;
    }

    private Button createMenuButton(
            String icon,
            String text) {

        Button button = new Button(
                icon + "   " + text
        );

        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(
                new Insets(12, 15, 12, 15)
        );

        button.setStyle(
                normalButtonStyle()
        );

        button.setOnMouseEntered(event ->
                button.setStyle(
                        hoverButtonStyle()
                )
        );

        button.setOnMouseExited(event -> {

            if (button != selectedButton) {

                button.setStyle(
                        normalButtonStyle()
                );
            }
        });

        return button;
    }

    private void selectButton(Button button) {

        if (selectedButton != null) {

            selectedButton.setStyle(
                    normalButtonStyle()
            );
        }

        selectedButton = button;

        selectedButton.setStyle(
                "-fx-background-color: " +
                SELECTION_COLOR + ";" +
                "-fx-text-fill: " +
                TEXT_COLOR + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;"
        );
    }

    private String normalButtonStyle() {

        return
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXT_COLOR + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;";
    }

    private String hoverButtonStyle() {

        return
                "-fx-background-color: " +
                SECONDARY_MENU_COLOR + ";" +
                "-fx-text-fill: " + TEXT_COLOR + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 13px;";
    }

    private void showHomePanel() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        Label route = new Label(
                "Home / System Overview"
        );

        route.setStyle(
                "-fx-text-fill: #777777;" +
                "-fx-font-size: 12px;"
        );

        String userName =
                currentUser != null
                        ? currentUser.getUsername()
                        : "";

        Label title = new Label(
                "Hello, " + userName
        );

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label description = new Label(
                "Welcome to the Tatú Carreta Natural Reserve " +
                "management dashboard."
        );

        description.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #666666;"
        );

        VBox header = new VBox(
                5,
                route,
                title,
                description
        );

        HBox cards = new HBox(15);

        VBox animalsCard = createCard(
                "TOTAL ACTIVE ANIMALS",
                "0",
                "Currently registered"
        );

        VBox enclosuresCard = createCard(
                "ENCLOSURES",
                "0",
                "Current occupancy"
        );

        VBox entriesCard = createCard(
                "MONTHLY ENTRIES",
                "0",
                "Registered entries"
        );

        VBox exitsCard = createCard(
                "MONTHLY EXITS",
                "0",
                "Releases and transfers"
        );

        cards.getChildren().addAll(
                animalsCard,
                enclosuresCard,
                entriesCard,
                exitsCard
        );

        Label activityTitle = new Label(
                "Recent Activities"
        );

        activityTitle.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        Label noActivity = new Label(
                "The latest entries, movements, and " +
                "registered changes will appear here."
        );

        noActivity.setStyle(
                "-fx-text-fill: #777777;"
        );

        VBox activity = new VBox(
                10,
                activityTitle,
                noActivity
        );

        content.getChildren().addAll(
                header,
                cards,
                activity
        );

        root.setCenter(content);
    }

    private VBox createCard(
            String title,
            String number,
            String description) {

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #777777;"
        );

        Label numberLabel = new Label(number);

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

        VBox card = new VBox(
                8,
                titleLabel,
                numberLabel,
                descriptionLabel
        );

        card.setPadding(
                new Insets(20)
        );

        card.setPrefWidth(210);

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
}