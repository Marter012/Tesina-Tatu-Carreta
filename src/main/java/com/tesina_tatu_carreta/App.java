package com.tesina_tatu_carreta;

import com.tesina_tatu_carreta.database.DatabaseInitializer;
import com.tesina_tatu_carreta.view.ViewLogin;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // Create the required database tables if they do not exist
        DatabaseInitializer.createSpeciesTable();
        DatabaseInitializer.createAnimalsTable();
        DatabaseInitializer.createEntriesTable();
        DatabaseInitializer.createEntryDetailsTable();
        DatabaseInitializer.createEnclosuresTable();
        DatabaseInitializer.createPermanentStaffTable();
        DatabaseInitializer.createMovementsTable();
        DatabaseInitializer.createIdentificationsTable();
        DatabaseInitializer.createUsersTable();

        // Open login window
        ViewLogin loginWindow = new ViewLogin();
        loginWindow.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
