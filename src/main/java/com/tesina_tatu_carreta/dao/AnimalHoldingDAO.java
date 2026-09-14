package com.tesina_tatu_carreta.dao;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.AnimalHolding;
import com.tesina_tatu_carreta.model.AnimalInventorySummary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AnimalHoldingDAO {

    public AnimalHolding find(
            Connection connection,
            int animalId,
            Integer entryId,
            String locationType)
            throws SQLException {

        String sql;

        if (entryId == null) {

            sql = """
                    SELECT
                        h.holding_id,
                        h.animal_id,
                        h.entry_id,
                        a.common_name,
                        h.location_type,
                        h.quantity,
                        h.status
                    FROM animal_holdings h
                    INNER JOIN animals a
                        ON a.animal_id = h.animal_id
                    WHERE h.animal_id = ?
                      AND h.entry_id IS NULL
                      AND h.location_type = ?
                    """;

        } else {

            sql = """
                    SELECT
                        h.holding_id,
                        h.animal_id,
                        h.entry_id,
                        a.common_name,
                        h.location_type,
                        h.quantity,
                        h.status
                    FROM animal_holdings h
                    INNER JOIN animals a
                        ON a.animal_id = h.animal_id
                    WHERE h.animal_id = ?
                      AND h.entry_id = ?
                      AND h.location_type = ?
                    """;
        }

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, animalId);

            if (entryId == null) {

                statement.setString(
                        2,
                        locationType);

            } else {

                statement.setInt(
                        2,
                        entryId);

                statement.setString(
                        3,
                        locationType);
            }

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {
                    return map(result);
                }
            }
        }

        return null;
    }

    // =========================================================
    // ADD
    // =========================================================

    public int add(
            Connection connection,
            AnimalHolding holding)
            throws SQLException {

        String sql = """
                INSERT INTO animal_holdings
                (
                    animal_id,
                    entry_id,
                    location_type,
                    quantity,
                    status
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    holding.getAnimalId());

            if (holding.getEntryId() == null) {

                statement.setNull(
                        2,
                        Types.INTEGER);

            } else {

                statement.setInt(
                        2,
                        holding.getEntryId());
            }

            statement.setString(
                    3,
                    holding.getLocationType());

            statement.setInt(
                    4,
                    holding.getQuantity());

            statement.setString(
                    5,
                    holding.getStatus());

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // UPDATE QUANTITY
    // =========================================================

    public void updateQuantity(
            Connection connection,
            int holdingId,
            int quantity,
            String status)
            throws SQLException {

        String sql = """
                UPDATE animal_holdings
                SET quantity = ?,
                    status = ?
                WHERE holding_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    quantity);

            statement.setString(
                    2,
                    status);

            statement.setInt(
                    3,
                    holdingId);

            statement.executeUpdate();
        }
    }

    // =========================================================
    // GET AVAILABLE QUANTITY
    // =========================================================

    public int getAvailableQuantity(
            int animalId,
            Integer entryId,
            String locationType) {

        try (Connection connection =
                     SQLiteConnection.connect()) {

            return getAvailableQuantity(
                    connection,
                    animalId,
                    entryId,
                    locationType);

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error getting available animal quantity.",
                    exception);
        }
    }

    // =========================================================
    // GET AVAILABLE QUANTITY - SAME CONNECTION
    // =========================================================

    public int getAvailableQuantity(
            Connection connection,
            int animalId,
            Integer entryId,
            String locationType)
            throws SQLException {

        AnimalHolding holding =
                find(
                        connection,
                        animalId,
                        entryId,
                        locationType);

        if (holding == null
                || holding.getQuantity() <= 0) {

            return 0;
        }

        return holding.getQuantity();
    }

    // =========================================================
    // TOTAL AVAILABLE QUANTITY BY ANIMAL
    // =========================================================

    public int getTotalAvailableQuantity(
            Connection connection,
            int animalId)
            throws SQLException {

        String sql = """
                SELECT COALESCE(
                    SUM(
                        CASE
                            WHEN quantity > 0
                            THEN quantity
                            ELSE 0
                        END
                    ),
                    0
                )
                FROM animal_holdings
                WHERE animal_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId);

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // TOTAL AVAILABLE QUANTITY BY ANIMAL
    // =========================================================

    public int getTotalAvailableQuantity(
            int animalId) {

        try (Connection connection =
                     SQLiteConnection.connect()) {

            return getTotalAvailableQuantity(
                    connection,
                    animalId);

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error getting total available animal quantity.",
                    exception);
        }
    }

    // =========================================================
    // AVAILABLE QUANTITY BY ANIMAL + ENTRY
    // =========================================================

    public int getAvailableQuantityByAnimalAndEntry(
            int animalId,
            int entryId) {

        try (Connection connection =
                     SQLiteConnection.connect()) {

            return getAvailableQuantityByAnimalAndEntry(
                    connection,
                    animalId,
                    entryId);

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error getting quantity by animal and entry.",
                    exception);
        }
    }

    // =========================================================
    // AVAILABLE QUANTITY BY ANIMAL + ENTRY
    // SAME CONNECTION
    // =========================================================

    public int getAvailableQuantityByAnimalAndEntry(
            Connection connection,
            int animalId,
            int entryId)
            throws SQLException {

        String sql = """
                SELECT COALESCE(
                    SUM(
                        CASE
                            WHEN quantity > 0
                            THEN quantity
                            ELSE 0
                        END
                    ),
                    0
                )
                FROM animal_holdings
                WHERE animal_id = ?
                  AND entry_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId);

            statement.setInt(
                    2,
                    entryId);

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // AVAILABLE QUANTITY BY ANIMAL + ENTRY + LOCATION
    // =========================================================

    public int getAvailableQuantityByAnimalAndEntryAndLocation(
            int animalId,
            int entryId,
            String locationType) {

        try (Connection connection =
                     SQLiteConnection.connect()) {

            return getAvailableQuantityByAnimalAndEntryAndLocation(
                    connection,
                    animalId,
                    entryId,
                    locationType);

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error getting quantity by animal, entry and location.",
                    exception);
        }
    }

    // =========================================================
    // AVAILABLE QUANTITY BY ANIMAL + ENTRY + LOCATION
    // SAME CONNECTION
    // =========================================================

    public int getAvailableQuantityByAnimalAndEntryAndLocation(
            Connection connection,
            int animalId,
            int entryId,
            String locationType)
            throws SQLException {

        String sql = """
                SELECT COALESCE(
                    SUM(
                        CASE
                            WHEN quantity > 0
                            THEN quantity
                            ELSE 0
                        END
                    ),
                    0
                )
                FROM animal_holdings
                WHERE animal_id = ?
                  AND entry_id = ?
                  AND location_type = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId);

            statement.setInt(
                    2,
                    entryId);

            statement.setString(
                    3,
                    locationType);

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt(1);
                }
            }
        }

        return 0;
    }

    // =========================================================
    // LIST HOLDINGS BY ANIMAL
    // =========================================================

    public List<AnimalHolding> listByAnimal(
            int animalId) {

        List<AnimalHolding> result =
                new ArrayList<>();

        String sql = """
                SELECT
                    h.holding_id,
                    h.animal_id,
                    h.entry_id,
                    a.common_name,
                    h.location_type,
                    h.quantity,
                    h.status
                FROM animal_holdings h
                INNER JOIN animals a
                    ON a.animal_id = h.animal_id
                WHERE h.animal_id = ?
                  AND h.quantity > 0
                ORDER BY
                    CASE
                        WHEN h.entry_id IS NULL THEN 1
                        ELSE 0
                    END,
                    h.entry_id DESC
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    animalId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    result.add(
                            map(resultSet));
                }
            }

        } catch (SQLException exception) {

            exception.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // LIST HOLDINGS BY ENTRY
    // =========================================================

    public List<AnimalHolding> listByEntry(
            int entryId) {

        List<AnimalHolding> result =
                new ArrayList<>();

        String sql = """
                SELECT
                    h.holding_id,
                    h.animal_id,
                    h.entry_id,
                    a.common_name,
                    h.location_type,
                    h.quantity,
                    h.status
                FROM animal_holdings h
                INNER JOIN animals a
                    ON a.animal_id = h.animal_id
                WHERE h.entry_id = ?
                  AND h.quantity > 0
                ORDER BY a.common_name
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    entryId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    result.add(
                            map(resultSet));
                }
            }

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error listing holdings by entry.",
                    exception);
        }

        return result;
    }

    // =========================================================
    // INVENTORY SUMMARY
    // =========================================================
    //
    // IMPORTANTE:
    // Se muestran TODOS los animales registrados.
    //
    // Antes existía:
    //
    // HAVING permanent_quantity
    //      + quarantine_quantity > 0
    //
    // Eso hacía desaparecer los animales con cantidad 0.
    // Ahora el LEFT JOIN permite conservarlos.
    // =========================================================

    public List<AnimalInventorySummary> listInventorySummary() {

        List<AnimalInventorySummary> result =
                new ArrayList<>();

        String sql = """
                SELECT
                    a.animal_id,
                    a.common_name,
                    a.scientific_name,

                    COALESCE(
                        SUM(
                            CASE
                                WHEN h.location_type = 'PERMANENT'
                                AND h.quantity > 0
                                THEN h.quantity
                                ELSE 0
                            END
                        ),
                        0
                    ) AS permanent_quantity,

                    COALESCE(
                        SUM(
                            CASE
                                WHEN h.location_type = 'QUARANTINE'
                                AND h.quantity > 0
                                THEN h.quantity
                                ELSE 0
                            END
                        ),
                        0
                    ) AS quarantine_quantity,

                    a.status

                FROM animals a

                LEFT JOIN animal_holdings h
                    ON h.animal_id = a.animal_id

                GROUP BY
                    a.animal_id,
                    a.common_name,
                    a.scientific_name,
                    a.status

                ORDER BY a.common_name
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                result.add(
                        new AnimalInventorySummary(
                                resultSet.getInt(
                                        "animal_id"),

                                resultSet.getString(
                                        "common_name"),

                                resultSet.getString(
                                        "scientific_name"),

                                resultSet.getInt(
                                        "permanent_quantity"),

                                resultSet.getInt(
                                        "quarantine_quantity"),

                                resultSet.getString(
                                        "status")));
            }

        } catch (SQLException exception) {

            exception.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // TOTAL BY LOCATION
    // =========================================================

    public int getTotalQuantity(
            String locationType) {

        String sql = """
                SELECT COALESCE(SUM(quantity), 0)
                FROM animal_holdings
                WHERE location_type = ?
                  AND quantity > 0
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    locationType);

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                if (result.next()) {
                    return result.getInt(1);
                }
            }

        } catch (SQLException exception) {

            exception.printStackTrace();
        }

        return 0;
    }

    // =========================================================
    // NULLABLE ENTRY ID
    // =========================================================

    private Integer getNullableEntryId(
            ResultSet result)
            throws SQLException {

        int entryId =
                result.getInt("entry_id");

        if (result.wasNull()) {
            return null;
        }

        return entryId;
    }

    // =========================================================
    // MAP
    // =========================================================

    private AnimalHolding map(
            ResultSet result)
            throws SQLException {

        return new AnimalHolding(
                result.getInt("holding_id"),
                result.getInt("animal_id"),
                getNullableEntryId(result),
                result.getString("common_name"),
                result.getString("location_type"),
                result.getInt("quantity"),
                result.getString("status"));
    }
}