package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.PermanentEnclosure;

public class PermanentEnclosureDAO {

    // =========================
    // ADD
    // =========================

    public void add(PermanentEnclosure enclosure) {

        String sql = """
                INSERT INTO permanent_enclosures (
                    animal_id,
                    quantity,
                    location_type,
                    enclosure_id,
                    enclosure_entry_date,
                    status,
                    observations
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    enclosure.getAnimalId()
            );

            statement.setInt(
                    2,
                    enclosure.getQuantity()
            );

            statement.setString(
                    3,
                    enclosure.getLocationType()
            );

            if (enclosure.getEnclosureId() != null) {

                statement.setInt(
                        4,
                        enclosure.getEnclosureId()
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            statement.setString(
                    5,
                    enclosure.getEnclosureEntryDate()
            );

            statement.setString(
                    6,
                    enclosure.getStatus()
            );

            statement.setString(
                    7,
                    enclosure.getObservations()
            );

            statement.executeUpdate();

            System.out.println(
                    "Permanent enclosure record added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding permanent enclosure record."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // LIST
    // =========================

    public List<PermanentEnclosure> list() {

        List<PermanentEnclosure> enclosures =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.permanent_enclosure_id,
                    p.animal_id,
                    a.common_name AS animal_name,
                    p.quantity,
                    p.location_type,
                    p.enclosure_id,
                    e.name AS enclosure_name,
                    p.enclosure_entry_date,
                    p.status,
                    p.observations
                FROM permanent_enclosures p

                INNER JOIN animals a
                    ON p.animal_id = a.animal_id

                LEFT JOIN enclosures e
                    ON p.enclosure_id = e.enclosure_id

                ORDER BY p.permanent_enclosure_id
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                PermanentEnclosure enclosure =
                        new PermanentEnclosure();

                enclosure.setPermanentEnclosureId(
                        result.getInt(
                                "permanent_enclosure_id"
                        )
                );

                enclosure.setAnimalId(
                        result.getInt(
                                "animal_id"
                        )
                );

                enclosure.setAnimalName(
                        result.getString(
                                "animal_name"
                        )
                );

                enclosure.setQuantity(
                        result.getInt(
                                "quantity"
                        )
                );

                enclosure.setLocationType(
                        result.getString(
                                "location_type"
                        )
                );

                int enclosureId =
                        result.getInt(
                                "enclosure_id"
                        );

                if (result.wasNull()) {

                    enclosure.setEnclosureId(null);

                } else {

                    enclosure.setEnclosureId(
                            enclosureId
                    );
                }

                enclosure.setEnclosureName(
                        result.getString(
                                "enclosure_name"
                        )
                );

                enclosure.setEnclosureEntryDate(
                        result.getString(
                                "enclosure_entry_date"
                        )
                );

                enclosure.setStatus(
                        result.getString(
                                "status"
                        )
                );

                enclosure.setObservations(
                        result.getString(
                                "observations"
                        )
                );

                enclosures.add(enclosure);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing permanent enclosure records."
            );

            System.out.println(e.getMessage());
        }

        return enclosures;
    }


    // =========================
    // UPDATE
    // =========================

    public void update(PermanentEnclosure enclosure) {

        String sql = """
                UPDATE permanent_enclosures
                SET
                    animal_id = ?,
                    quantity = ?,
                    location_type = ?,
                    enclosure_id = ?,
                    enclosure_entry_date = ?,
                    status = ?,
                    observations = ?
                WHERE permanent_enclosure_id = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    enclosure.getAnimalId()
            );

            statement.setInt(
                    2,
                    enclosure.getQuantity()
            );

            statement.setString(
                    3,
                    enclosure.getLocationType()
            );

            if (enclosure.getEnclosureId() != null) {

                statement.setInt(
                        4,
                        enclosure.getEnclosureId()
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            statement.setString(
                    5,
                    enclosure.getEnclosureEntryDate()
            );

            statement.setString(
                    6,
                    enclosure.getStatus()
            );

            statement.setString(
                    7,
                    enclosure.getObservations()
            );

            statement.setInt(
                    8,
                    enclosure.getPermanentEnclosureId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Permanent enclosure record updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating permanent enclosure record."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // DELETE
    // =========================

    public void delete(int permanentEnclosureId) {

        String sql = """
                DELETE FROM permanent_enclosures
                WHERE permanent_enclosure_id = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    permanentEnclosureId
            );

            statement.executeUpdate();

            System.out.println(
                    "Permanent enclosure record deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting permanent enclosure record."
            );

            System.out.println(e.getMessage());
        }
    }
}