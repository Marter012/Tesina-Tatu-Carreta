package com.tesina_tatu_carreta.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {

    private static final String DATABASE_URL =
            "jdbc:sqlite:tatu_carreta.db";

    private SQLiteConnection() {
    }

    public static Connection connect()
            throws SQLException {

        Connection connection =
                DriverManager.getConnection(
                        DATABASE_URL
                );

        try (
                Statement statement =
                        connection.createStatement()
        ) {

            statement.execute(
                    "PRAGMA foreign_keys = ON"
            );
        }

        return connection;
    }
}