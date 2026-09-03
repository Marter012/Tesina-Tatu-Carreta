package com.tesina_tatu_carreta.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static final String URL = "jdbc:sqlite:tatu_carreta.db";

    public static Connection connect() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("SQLite connection successful.");
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite.");
            System.out.println(e.getMessage());
        }

        return connection;
    }
}