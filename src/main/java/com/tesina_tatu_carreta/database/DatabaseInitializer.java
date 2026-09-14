package com.tesina_tatu_carreta.database;

import com.tesina_tatu_carreta.dao.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {

        try (Connection connection = SQLiteConnection.connect();
                Statement statement = connection.createStatement()) {

            createSpeciesTable(statement);
            createAnimalsTable(statement);
            createEntriesTable(statement);
            createEnclosuresTable(statement);
            createEntryDetailsTable(statement);
            createPermanentEnclosuresTable(statement);
            createAnimalHoldingsTable(statement);
            createMovementsTable(statement);
            createIdentificationsTable(statement);
            createUsersTable(statement);

            createIndexes(statement);

            createDefaultAdminUser();

            System.out.println(
                    "Database initialized successfully.");

        } catch (SQLException exception) {

            System.err.println(
                    "Database initialization failed: "
                            + exception.getMessage());

            exception.printStackTrace();
        }
    }

    private static void createSpeciesTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS species (
                    species_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
                """);
    }

    private static void createAnimalsTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS animals (
                    animal_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    species_id INTEGER NOT NULL,
                    common_name TEXT NOT NULL,
                    scientific_name TEXT NOT NULL,
                    current_quantity INTEGER NOT NULL DEFAULT 0,
                    origin TEXT,
                    status TEXT NOT NULL DEFAULT 'Active',

                    FOREIGN KEY (species_id)
                        REFERENCES species(species_id)
                )
                """);
    }

    private static void createEntriesTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS entries (
                    entry_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    record_number TEXT NOT NULL UNIQUE,
                    entry_date TEXT NOT NULL,
                    source_organization TEXT,
                    delivery_responsible TEXT,
                    origin TEXT,
                    entry_reason TEXT,
                    documentation TEXT,
                    observations TEXT
                )
                """);
    }

    private static void createEntryDetailsTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS entry_details (
                    detail_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    entry_id INTEGER,
                    animal_id INTEGER NOT NULL,
                    enclosure_id INTEGER,
                    quantity INTEGER NOT NULL CHECK(quantity > 0),
                    sex TEXT,
                    age TEXT,
                    weight REAL DEFAULT 0,
                    destination TEXT NOT NULL
                        CHECK(destination IN ('QUARANTINE', 'PERMANENT')),
                    entry_status TEXT,
                    observations TEXT,

                    FOREIGN KEY (entry_id)
                        REFERENCES entries(entry_id)
                        ON DELETE CASCADE,

                    FOREIGN KEY (animal_id)
                        REFERENCES animals(animal_id),

                    FOREIGN KEY (enclosure_id)
                        REFERENCES enclosures(enclosure_id)
                )
                """);

        /*
         * The application may already have an existing
         * entry_details table created before enclosure_id
         * was added.
         *
         * SQLite does not modify an existing table when
         * CREATE TABLE IF NOT EXISTS is executed.
         *
         * Therefore we check whether enclosure_id exists
         * and add it only when necessary.
         */
        addEnclosureIdToExistingEntryDetails(statement);
    }

    private static void addEnclosureIdToExistingEntryDetails(
            Statement statement) throws SQLException {

        boolean columnExists = false;

        try (var resultSet = statement.executeQuery(
                "PRAGMA table_info(entry_details)")) {

            while (resultSet.next()) {

                String columnName = resultSet.getString("name");

                if ("enclosure_id".equalsIgnoreCase(
                        columnName)) {

                    columnExists = true;
                    break;
                }
            }
        }

        if (!columnExists) {

            statement.execute("""
                    ALTER TABLE entry_details
                    ADD COLUMN enclosure_id INTEGER
                    """);

            System.out.println(
                    "Column enclosure_id added to entry_details.");
        }
    }

    private static void createEnclosuresTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS enclosures (
                    enclosure_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    sector TEXT,
                    capacity INTEGER NOT NULL DEFAULT 0,
                    status TEXT NOT NULL DEFAULT 'Active',
                    observations TEXT
                )
                """);
    }

    private static void createPermanentEnclosuresTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS permanent_enclosures (
                    permanent_enclosure_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    quantity INTEGER NOT NULL CHECK(quantity > 0),
                    location_type TEXT NOT NULL,
                    enclosure_id INTEGER,
                    enclosure_entry_date TEXT,
                    status TEXT NOT NULL DEFAULT 'Active',
                    observations TEXT,

                    FOREIGN KEY (animal_id)
                        REFERENCES animals(animal_id),

                    FOREIGN KEY (enclosure_id)
                        REFERENCES enclosures(enclosure_id)
                )
                """);
    }

    private static void createAnimalHoldingsTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS animal_holdings (
                    holding_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    entry_id INTEGER,
                    location_type TEXT NOT NULL
                        CHECK(location_type IN ('QUARANTINE', 'PERMANENT')),
                    quantity INTEGER NOT NULL
                        CHECK(quantity >= 0),
                    status TEXT NOT NULL DEFAULT 'Active',

                    FOREIGN KEY (animal_id)
                        REFERENCES animals(animal_id),

                    FOREIGN KEY (entry_id)
                        REFERENCES entries(entry_id)
                )
                """);
    }

    private static void createMovementsTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS movements (
                    movement_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    entry_id INTEGER,
                    movement_date TEXT NOT NULL,
                    movement_type TEXT NOT NULL
                        CHECK(
                            movement_type IN (
                                'ENTRY',
                                'TRANSFER',
                                'DEATH',
                                'EXIT'
                            )
                        ),
                    quantity INTEGER NOT NULL
                        CHECK(quantity > 0),
                    origin_location TEXT,
                    destination TEXT,
                    observations TEXT,

                    FOREIGN KEY (animal_id)
                        REFERENCES animals(animal_id),

                    FOREIGN KEY (entry_id)
                        REFERENCES entries(entry_id)
                )
                """);
    }

    private static void createIdentificationsTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS identifications (
                    identification_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    animal_id INTEGER NOT NULL,
                    identification_type TEXT,
                    identification_number TEXT,
                    identification_date TEXT,
                    observations TEXT,

                    FOREIGN KEY (animal_id)
                        REFERENCES animals(animal_id)
                )
                """);
    }

    private static void createUsersTable(
            Statement statement) throws SQLException {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Active'
                )
                """);
    }

    private static void createIndexes(
            Statement statement) throws SQLException {

        /*
         * Holdings with an entry:
         * One holding per animal + entry + location.
         */
        statement.execute("""
                CREATE UNIQUE INDEX IF NOT EXISTS
                idx_animal_holdings_with_entry
                ON animal_holdings(
                    animal_id,
                    entry_id,
                    location_type
                )
                WHERE entry_id IS NOT NULL
                """);

        /*
         * Holdings without an entry:
         * One holding per animal + location.
         */
        statement.execute("""
                CREATE UNIQUE INDEX IF NOT EXISTS
                idx_animal_holdings_without_entry
                ON animal_holdings(
                    animal_id,
                    location_type
                )
                WHERE entry_id IS NULL
                """);

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_holdings_animal
                ON animal_holdings(animal_id)
                """);

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_movements_animal
                ON movements(animal_id)
                """);

        statement.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_movements_entry
                ON movements(entry_id)
                """);
    }

    private static void createDefaultAdminUser() {

        try {

            UserDAO userDAO = new UserDAO();

            userDAO.initializeAdministrator();

        } catch (Exception exception) {

            System.err.println(
                    "Could not initialize default user: "
                            + exception.getMessage());
        }
    }
}