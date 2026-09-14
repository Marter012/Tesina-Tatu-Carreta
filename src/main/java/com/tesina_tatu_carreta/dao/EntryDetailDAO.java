package com.tesina_tatu_carreta.dao;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.EntryDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EntryDetailDAO {

    public void add(
            Connection connection,
            EntryDetail detail)
            throws SQLException {

        String sql = """
                INSERT INTO entry_details
                (
                    entry_id,
                    animal_id,
                    enclosure_id,
                    quantity,
                    sex,
                    age,
                    weight,
                    destination,
                    entry_status,
                    observations
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            setNullableEntryId(
                    statement,
                    1,
                    detail.getEntryId()
            );

            statement.setInt(
                    2,
                    detail.getAnimalId()
            );

            if (detail.getEnclosureId() != null) {

                statement.setInt(
                        3,
                        detail.getEnclosureId()
                );

            } else {

                statement.setNull(
                        3,
                        Types.INTEGER
                );
            }

            statement.setInt(
                    4,
                    detail.getQuantity()
            );

            statement.setString(
                    5,
                    detail.getSex()
            );

            statement.setString(
                    6,
                    detail.getAge()
            );

            statement.setDouble(
                    7,
                    detail.getWeight()
            );

            statement.setString(
                    8,
                    detail.getDestination()
            );

            statement.setString(
                    9,
                    detail.getEntryStatus()
            );

            statement.setString(
                    10,
                    detail.getObservations()
            );

            statement.executeUpdate();
        }
    }

    public List<EntryDetail> listByEntry(
            int entryId) {

        List<EntryDetail> details =
                new ArrayList<>();

        String sql = """
                SELECT
                    detail_id,
                    entry_id,
                    animal_id,
                    enclosure_id,
                    quantity,
                    sex,
                    age,
                    weight,
                    destination,
                    entry_status,
                    observations
                FROM entry_details
                WHERE entry_id = ?
                ORDER BY detail_id
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    entryId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    details.add(
                            map(resultSet)
                    );
                }
            }

        } catch (SQLException exception) {

            throw new RuntimeException(
                    "Error loading entry details.",
                    exception
            );
        }

        return details;
    }

    public void deleteByEntry(
            Connection connection,
            int entryId)
            throws SQLException {

        String sql = """
                DELETE FROM entry_details
                WHERE entry_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entryId
            );

            statement.executeUpdate();
        }
    }

    private Integer getNullableEntryId(
            ResultSet resultSet)
            throws SQLException {

        int value =
                resultSet.getInt("entry_id");

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }

    private Integer getNullableEnclosureId(
            ResultSet resultSet)
            throws SQLException {

        int value =
                resultSet.getInt("enclosure_id");

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }

    private void setNullableEntryId(
            PreparedStatement statement,
            int parameterIndex,
            Integer entryId)
            throws SQLException {

        if (entryId == null) {

            statement.setNull(
                    parameterIndex,
                    Types.INTEGER
            );

        } else {

            statement.setInt(
                    parameterIndex,
                    entryId
            );
        }
    }

    private EntryDetail map(
            ResultSet resultSet)
            throws SQLException {

        EntryDetail detail =
                new EntryDetail();

        detail.setDetailId(
                resultSet.getInt(
                        "detail_id"
                )
        );

        detail.setEntryId(
                getNullableEntryId(resultSet)
        );

        detail.setAnimalId(
                resultSet.getInt(
                        "animal_id"
                )
        );

        detail.setEnclosureId(
                getNullableEnclosureId(resultSet)
        );

        detail.setQuantity(
                resultSet.getInt(
                        "quantity"
                )
        );

        detail.setSex(
                resultSet.getString(
                        "sex"
                )
        );

        detail.setAge(
                resultSet.getString(
                        "age"
                )
        );

        detail.setWeight(
                resultSet.getDouble(
                        "weight"
                )
        );

        detail.setDestination(
                resultSet.getString(
                        "destination"
                )
        );

        detail.setEntryStatus(
                resultSet.getString(
                        "entry_status"
                )
        );

        detail.setObservations(
                resultSet.getString(
                        "observations"
                )
        );

        return detail;
    }
}