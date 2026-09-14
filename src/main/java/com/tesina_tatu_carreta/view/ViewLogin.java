package com.tesina_tatu_carreta.view;

import com.tesina_tatu_carreta.dao.UserDAO;
import com.tesina_tatu_carreta.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewLogin {

    private final UserDAO userDAO = new UserDAO();

    public void show(Stage stage) {

        // =========================
        // LOGO
        // =========================

        Image logo = new Image(
                getClass().getResourceAsStream(
                        "/images/logo.png"
                )
        );

        ImageView logoImage = new ImageView(logo);

        logoImage.setFitWidth(150);
        logoImage.setPreserveRatio(true);


        // =========================
        // NOMBRE DEL SISTEMA
        // =========================

        Label systemTitle =
                new Label("TATÚ CARRETA");

        systemTitle.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );

        Label systemSubtitle =
                new Label("Gestión de Fauna Silvestre");

        systemSubtitle.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-text-fill: #68746B;"
        );


        // =========================
        // TÍTULO LOGIN
        // =========================

        Label loginTitle =
                new Label("Iniciar sesión");

        loginTitle.setStyle(
                "-fx-font-size: 23px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #23452C;"
        );

        Label description =
                new Label(
                        "Ingrese sus credenciales para acceder al sistema"
                );

        description.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #6B7280;"
        );


        // =========================
        // USUARIO
        // =========================

        Label usernameLabel =
                new Label("Usuario");

        usernameLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #34463A;"
        );

        TextField usernameField =
                new TextField();

        usernameField.setPromptText(
                "Ingrese su usuario"
        );

        usernameField.setPrefHeight(38);

        usernameField.setStyle(
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: #C7D0C8;" +
                "-fx-font-size: 13px;"
        );


        // =========================
        // CONTRASEÑA
        // =========================

        Label passwordLabel =
                new Label("Contraseña");

        passwordLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #34463A;"
        );

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Ingrese su contraseña"
        );

        passwordField.setPrefHeight(38);

        passwordField.setStyle(
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: #C7D0C8;" +
                "-fx-font-size: 13px;"
        );


        // =========================
        // BOTÓN
        // =========================

        Button loginButton =
                new Button("INICIAR SESIÓN");

        loginButton.setMaxWidth(
                Double.MAX_VALUE
        );

        loginButton.setPrefHeight(42);

        loginButton.setStyle(
                "-fx-background-color: #23452C;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"
        );


        // =========================
        // ACCIÓN LOGIN
        // =========================

        loginButton.setOnAction(e -> {

            String username =
                    usernameField.getText().trim();

            String password =
                    passwordField.getText();

            if (username.isBlank()
                    || password.isBlank()) {

                showMessage(
                        "Ingrese su usuario y contraseña."
                );

                return;
            }

            User user =
                    userDAO.login(
                            username,
                            password
                    );

            if (user != null) {

                ViewMain mainWindow =
                        new ViewMain(user);

                Stage newWindow =
                        new Stage();

                mainWindow.start(
                        newWindow
                );

                stage.close();

            } else {

                showMessage(
                        "El usuario o la contraseña son incorrectos, o el usuario se encuentra inactivo."
                );
            }
        });


        passwordField.setOnAction(e ->
                loginButton.fire()
        );


        // =========================
        // TARJETA LOGIN
        // =========================

        VBox loginCard = new VBox(9);

        loginCard.setAlignment(
                Pos.CENTER_LEFT
        );

        loginCard.setPadding(
                new Insets(25)
        );

        loginCard.setPrefWidth(390);

        loginCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: #D2D8D1;" +
                "-fx-border-radius: 18;" +
                "-fx-border-width: 1;"
        );

        loginCard.getChildren().addAll(
                loginTitle,
                description,
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                loginButton
        );


        // =========================
        // PIE
        // =========================

        Label footer =
                new Label(
                        "Reserva Natural Tatú Carreta"
                );

        footer.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #7A857C;"
        );


        // =========================
        // CONTENEDOR
        // =========================

        VBox container =
                new VBox(10);

        // IMPORTANTE:
        container.setAlignment(
                Pos.CENTER
        );

        container.setPadding(
                new Insets(15)
        );

        container.getChildren().addAll(
                logoImage,
                systemTitle,
                systemSubtitle,
                loginCard,
                footer
        );

        container.setStyle(
                "-fx-background-color: #F2F0E6;"
        );


        // =========================
        // VENTANA
        // =========================

        Scene scene =
                new Scene(
                        container,
                        600,
                        700
                );

        stage.setTitle(
                "Tatú Carreta - Iniciar sesión"
        );

        stage.setScene(
                scene
        );

        stage.setMinWidth(600);
        stage.setMinHeight(700);

        stage.show();
    }


    private void showMessage(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(
                "Tatú Carreta"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}