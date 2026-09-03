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
                INSERT INTO plantel_permanente (
                    id_animal,
                    cantidad,
                    tipo_ubicacion,
                    id_habitaculo,
                    fecha_ingreso_plantel,
                    estado,
                    observaciones
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
                    p.id_plantel,
                    p.id_animal,
                    a.nombre_vulgar AS nombre_animal,
                    p.cantidad,
                    p.tipo_ubicacion,
                    p.id_habitaculo,
                    h.nombre AS nombre_habitaculo,
                    p.fecha_ingreso_plantel,
                    p.estado,
                    p.observaciones
                FROM plantel_permanente p

                INNER JOIN animales a
                    ON p.id_animal = a.id_animal

                LEFT JOIN habitaculos h
                    ON p.id_habitaculo = h.id_habitaculo

                ORDER BY p.id_plantel
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
                        result.getInt("id_plantel")
                );

                enclosure.setAnimalId(
                        result.getInt("id_animal")
                );

                enclosure.setAnimalName(
                        result.getString("nombre_animal")
                );

                enclosure.setQuantity(
                        result.getInt("cantidad")
                );

                enclosure.setLocationType(
                        result.getString("tipo_ubicacion")
                );

                int enclosureId =
                        result.getInt("id_habitaculo");

                if (result.wasNull()) {

                    enclosure.setEnclosureId(null);

                } else {

                    enclosure.setEnclosureId(enclosureId);
                }

                enclosure.setEnclosureName(
                        result.getString("nombre_habitaculo")
                );

                enclosure.setEnclosureEntryDate(
                        result.getString("fecha_ingreso_plantel")
                );

                enclosure.setStatus(
                        result.getString("estado")
                );

                enclosure.setObservations(
                        result.getString("observaciones")
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
                UPDATE plantel_permanente
                SET
                    id_animal = ?,
                    cantidad = ?,
                    tipo_ubicacion = ?,
                    id_habitaculo = ?,
                    fecha_ingreso_plantel = ?,
                    estado = ?,
                    observaciones = ?
                WHERE id_plantel = ?
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
                DELETE FROM plantel_permanente
                WHERE id_plantel = ?
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