package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Enclosure;

public class EnclosureDAO {

    // =========================
    // ADD
    // =========================

    public void add(Enclosure enclosure) {

        String sql = """
                INSERT INTO habitaculos (
                    nombre,
                    sector,
                    capacidad,
                    estado,
                    observaciones
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    enclosure.getName()
            );

            statement.setString(
                    2,
                    enclosure.getSector()
            );

            if (enclosure.getCapacity() != null) {

                statement.setInt(
                        3,
                        enclosure.getCapacity()
                );

            } else {

                statement.setNull(
                        3,
                        java.sql.Types.INTEGER
                );
            }

            statement.setString(
                    4,
                    enclosure.getStatus()
            );

            statement.setString(
                    5,
                    enclosure.getObservations()
            );

            statement.executeUpdate();

            System.out.println(
                    "Enclosure added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding enclosure."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // LIST
    // =========================

    public List<Enclosure> list() {

        List<Enclosure> enclosures =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_habitaculo,
                    nombre,
                    sector,
                    capacidad,
                    estado,
                    observaciones
                FROM habitaculos
                ORDER BY nombre
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                Enclosure enclosure =
                        new Enclosure();

                enclosure.setEnclosureId(
                        result.getInt(
                                "id_habitaculo"
                        )
                );

                enclosure.setName(
                        result.getString(
                                "nombre"
                        )
                );

                enclosure.setSector(
                        result.getString(
                                "sector"
                        )
                );

                int capacity =
                        result.getInt(
                                "capacidad"
                        );

                if (result.wasNull()) {

                    enclosure.setCapacity(null);

                } else {

                    enclosure.setCapacity(
                            capacity
                    );
                }

                enclosure.setStatus(
                        result.getString(
                                "estado"
                        )
                );

                enclosure.setObservations(
                        result.getString(
                                "observaciones"
                        )
                );

                enclosures.add(enclosure);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing enclosures."
            );

            System.out.println(e.getMessage());
        }

        return enclosures;
    }


    // =========================
    // UPDATE
    // =========================

    public void update(Enclosure enclosure) {

        String sql = """
                UPDATE habitaculos
                SET
                    nombre = ?,
                    sector = ?,
                    capacidad = ?,
                    estado = ?,
                    observaciones = ?
                WHERE id_habitaculo = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    enclosure.getName()
            );

            statement.setString(
                    2,
                    enclosure.getSector()
            );

            if (enclosure.getCapacity() != null) {

                statement.setInt(
                        3,
                        enclosure.getCapacity()
                );

            } else {

                statement.setNull(
                        3,
                        java.sql.Types.INTEGER
                );
            }

            statement.setString(
                    4,
                    enclosure.getStatus()
            );

            statement.setString(
                    5,
                    enclosure.getObservations()
            );

            statement.setInt(
                    6,
                    enclosure.getEnclosureId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Enclosure updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating enclosure."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // DELETE
    // =========================

    public void delete(int enclosureId) {

        String sql = """
                DELETE FROM habitaculos
                WHERE id_habitaculo = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    enclosureId
            );

            statement.executeUpdate();

            System.out.println(
                    "Enclosure deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting enclosure."
            );

            System.out.println(e.getMessage());
        }
    }
}