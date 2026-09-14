package com.tesina_tatu_carreta.dao;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EntryDAO {

    public int insertAndReturnId(
            Connection connection,
            Entry entry)
            throws SQLException {

        String sql = """
                INSERT INTO entries
                (
                    record_number,
                    entry_date,
                    source_organization,
                    delivery_responsible,
                    origin,
                    entry_reason,
                    documentation,
                    observations
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    entry.getRecordNumber()
            );

            statement.setString(
                    2,
                    entry.getEntryDate()
            );

            statement.setString(
                    3,
                    entry.getSourceOrganization()
            );

            statement.setString(
                    4,
                    entry.getDeliveryResponsible()
            );

            statement.setString(
                    5,
                    entry.getOrigin()
            );

            statement.setString(
                    6,
                    entry.getEntryReason()
            );

            statement.setString(
                    7,
                    entry.getDocumentation()
            );

            statement.setString(
                    8,
                    entry.getObservations()
            );

            statement.executeUpdate();

            try (
                    ResultSet keys =
                            statement.getGeneratedKeys()
            ) {

                if (keys.next()) {

                    return keys.getInt(1);
                }
            }
        }

        return 0;
    }

    public void add(
            Connection connection,
            Entry entry)
            throws SQLException {

        String sql = """
                INSERT INTO entries
                (
                    record_number,
                    entry_date,
                    source_organization,
                    delivery_responsible,
                    origin,
                    entry_reason,
                    documentation,
                    observations
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    entry.getRecordNumber()
            );

            statement.setString(
                    2,
                    entry.getEntryDate()
            );

            statement.setString(
                    3,
                    entry.getSourceOrganization()
            );

            statement.setString(
                    4,
                    entry.getDeliveryResponsible()
            );

            statement.setString(
                    5,
                    entry.getOrigin()
            );

            statement.setString(
                    6,
                    entry.getEntryReason()
            );

            statement.setString(
                    7,
                    entry.getDocumentation()
            );

            statement.setString(
                    8,
                    entry.getObservations()
            );

            statement.executeUpdate();
        }
    }

    public void add(
            Entry entry) {

        try (Connection connection =
                     SQLiteConnection.connect()) {

            add(
                    connection,
                    entry
            );

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error adding entry.",
                    exception
            );
        }
    }

    public Entry findByRecordNumber(
            String recordNumber) {

        String sql = """
                SELECT
                    entry_id,
                    record_number,
                    entry_date,
                    source_organization,
                    delivery_responsible,
                    origin,
                    entry_reason,
                    documentation,
                    observations
                FROM entries
                WHERE record_number = ?
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    recordNumber
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSet(
                            resultSet
                    );
                }
            }

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error finding entry.",
                    exception
            );
        }

        return null;
    }

    public List<Entry> list() {

        List<Entry> entries =
                new ArrayList<>();

        String sql = """
                SELECT
                    entry_id,
                    record_number,
                    entry_date,
                    source_organization,
                    delivery_responsible,
                    origin,
                    entry_reason,
                    documentation,
                    observations
                FROM entries
                ORDER BY entry_id DESC
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

                entries.add(
                        mapResultSet(resultSet)
                );
            }

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error listing entries.",
                    exception
            );
        }

        return entries;
    }

    public List<Entry> listByAnimal(
            int animalId) {

        List<Entry> entries =
                new ArrayList<>();

        String sql = """
                SELECT DISTINCT
                    e.entry_id,
                    e.record_number,
                    e.entry_date,
                    e.source_organization,
                    e.delivery_responsible,
                    e.origin,
                    e.entry_reason,
                    e.documentation,
                    e.observations
                FROM entries e
                INNER JOIN entry_details d
                    ON d.entry_id = e.entry_id
                WHERE d.animal_id = ?
                ORDER BY e.entry_id DESC
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    animalId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    entries.add(
                            mapResultSet(resultSet)
                    );
                }
            }

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error listing entries by animal.",
                    exception
            );
        }

        return entries;
    }

    public void update(
            Entry entry) {

        String sql = """
                UPDATE entries
                SET
                    record_number = ?,
                    entry_date = ?,
                    source_organization = ?,
                    delivery_responsible = ?,
                    origin = ?,
                    entry_reason = ?,
                    documentation = ?,
                    observations = ?
                WHERE entry_id = ?
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    entry.getRecordNumber()
            );

            statement.setString(
                    2,
                    entry.getEntryDate()
            );

            statement.setString(
                    3,
                    entry.getSourceOrganization()
            );

            statement.setString(
                    4,
                    entry.getDeliveryResponsible()
            );

            statement.setString(
                    5,
                    entry.getOrigin()
            );

            statement.setString(
                    6,
                    entry.getEntryReason()
            );

            statement.setString(
                    7,
                    entry.getDocumentation()
            );

            statement.setString(
                    8,
                    entry.getObservations()
            );

            statement.setInt(
                    9,
                    entry.getEntryId()
            );

            statement.executeUpdate();

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error updating entry.",
                    exception
            );
        }
    }

    public void delete(
            Connection connection,
            int entryId)
            throws SQLException {

        String sql = """
                DELETE FROM entries
                WHERE entry_id = ?
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    entryId
            );

            statement.executeUpdate();
        }
    }

    public void delete(
            int entryId) {

        try (Connection connection =
                     SQLiteConnection.connect()) {

            delete(
                    connection,
                    entryId
            );

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error deleting entry.",
                    exception
            );
        }
    }

    // =========================================================
    // APPEND OBSERVATION
    // =========================================================

    public void appendObservation(
            Connection connection,
            int entryId,
            String observation)
            throws SQLException {

        if (observation == null
                || observation.isBlank()) {

            return;
        }

        String sql = """
                UPDATE entries
                SET observations =
                    CASE
                        WHEN observations IS NULL
                             OR TRIM(observations) = ''
                        THEN ?
                        ELSE observations
                             || CHAR(10)
                             || ?
                    END
                WHERE entry_id = ?
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    observation.trim()
            );

            statement.setString(
                    2,
                    observation.trim()
            );

            statement.setInt(
                    3,
                    entryId
            );

            statement.executeUpdate();
        }
    }

    private Entry mapResultSet(
            ResultSet resultSet)
            throws SQLException {

        Entry entry =
                new Entry();

        entry.setEntryId(
                resultSet.getInt(
                        "entry_id"
                )
        );

        entry.setRecordNumber(
                resultSet.getString(
                        "record_number"
                )
        );

        entry.setEntryDate(
                resultSet.getString(
                        "entry_date"
                )
        );

        entry.setSourceOrganization(
                resultSet.getString(
                        "source_organization"
                )
        );

        entry.setDeliveryResponsible(
                resultSet.getString(
                        "delivery_responsible"
                )
        );

        entry.setOrigin(
                resultSet.getString(
                        "origin"
                )
        );

        entry.setEntryReason(
                resultSet.getString(
                        "entry_reason"
                )
        );

        entry.setDocumentation(
                resultSet.getString(
                        "documentation"
                )
        );

        entry.setObservations(
                resultSet.getString(
                        "observations"
                )
        );

        return entry;
    }
}