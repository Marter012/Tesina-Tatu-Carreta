package com.tesina_tatu_carreta.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    // =========================
    // SPECIES
    // =========================

    public static void createSpeciesTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS species (
                    species_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Species table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating species table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // ANIMALS
    // =========================

    public static void createAnimalsTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS animals (
                    animal_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    species_id INTEGER NOT NULL,
                    common_name TEXT NOT NULL,
                    scientific_name TEXT NOT NULL,
                    current_quantity INTEGER NOT NULL DEFAULT 1,
                    origin TEXT NOT NULL,
                    status TEXT NOT NULL,

                    FOREIGN KEY (species_id)
                    REFERENCES species(species_id)
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Animals table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating animals table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // ENTRIES / RECORDS
    // =========================

    public static void createEntriesTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS entries (
                    entry_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    record_number TEXT NOT NULL,
                    entry_date TEXT NOT NULL,
                    source_organization TEXT NOT NULL,
                    delivery_person TEXT,
                    origin TEXT NOT NULL,
                    entry_reason TEXT NOT NULL,
                    documentation TEXT,
                    observations TEXT
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Entries table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating entries table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // ENTRY DETAILS
    // =========================

    public static void createEntryDetailsTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS entry_details (
                    detail_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    entry_id INTEGER NOT NULL,
                    animal_id INTEGER NOT NULL,
                    quantity INTEGER NOT NULL,
                    sex TEXT,
                    age TEXT,
                    weight REAL,
                    entry_status TEXT,
                    observations TEXT,

                    FOREIGN KEY (entry_id)
                    REFERENCES entries(entry_id),

                    FOREIGN KEY (animal_id)
                    REFERENCES animals(animal_id)
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Entry details table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating entry details table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // ENCLOSURES
    // =========================

    public static void createEnclosuresTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS enclosures (
                    enclosure_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    sector TEXT NOT NULL,
                    capacity INTEGER,
                    status TEXT NOT NULL,
                    observations TEXT
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Enclosures table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating enclosures table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // PERMANENT STAFF
    // =========================

    public static void createPermanentStaffTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS permanent_staff (
                    staff_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    quantity INTEGER NOT NULL,
                    location_type TEXT NOT NULL,
                    enclosure_id INTEGER,
                    staff_entry_date TEXT NOT NULL,
                    status TEXT NOT NULL,
                    observations TEXT,

                    FOREIGN KEY (animal_id)
                    REFERENCES animals(animal_id),

                    FOREIGN KEY (enclosure_id)
                    REFERENCES enclosures(enclosure_id)
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Permanent staff table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating permanent staff table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // MOVEMENTS
    // =========================

    public static void createMovementsTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS movements (
                    movement_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    entry_id INTEGER,
                    movement_date TEXT NOT NULL,
                    movement_type TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    destination TEXT,
                    observations TEXT,

                    FOREIGN KEY (animal_id)
                    REFERENCES animals(animal_id),

                    FOREIGN KEY (entry_id)
                    REFERENCES entries(entry_id)
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Movements table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating movements table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // IDENTIFICATIONS
    // =========================

    public static void createIdentificationsTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS identifications (
                    identification_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    identification_type TEXT NOT NULL,
                    identification_number TEXT NOT NULL,
                    identification_date TEXT,
                    observations TEXT,

                    FOREIGN KEY (animal_id)
                    REFERENCES animals(animal_id)
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Identifications table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating identifications table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // USERS
    // =========================

    public static void createUsersTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,
                    status TEXT NOT NULL
                )
                """;

        try (Connection connection = SQLiteConnection.connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Users table created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating users table."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // DATABASE INITIALIZATION
    // =========================

    public static void initialize() {

        createSpeciesTable();
        createAnimalsTable();
        createEntriesTable();
        createEntryDetailsTable();
        createEnclosuresTable();
        createPermanentStaffTable();
        createMovementsTable();
        createIdentificationsTable();
        createUsersTable();

        System.out.println(
                "Database initialization completed."
        );
    }
}