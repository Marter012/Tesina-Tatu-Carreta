package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.EntryDetail;

public class EntryDetailDAO {

    public void add(EntryDetail detail) {

        String sql = """
                INSERT INTO detalle_ingreso (
                    id_ingreso,
                    id_animal,
                    cantidad,
                    sexo,
                    edad,
                    peso,
                    estado_ingreso,
                    observaciones
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    detail.getEntryId()
            );

            statement.setInt(
                    2,
                    detail.getAnimalId()
            );

            statement.setInt(
                    3,
                    detail.getQuantity()
            );

            statement.setString(
                    4,
                    detail.getSex()
            );

            statement.setString(
                    5,
                    detail.getAge()
            );

            statement.setDouble(
                    6,
                    detail.getWeight()
            );

            statement.setString(
                    7,
                    detail.getEntryStatus()
            );

            statement.setString(
                    8,
                    detail.getObservations()
            );

            statement.executeUpdate();

            System.out.println(
                    "Entry detail added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding entry detail."
            );

            System.out.println(e.getMessage());
        }
    }

    public List<EntryDetail> listByEntry(int entryId) {

        List<EntryDetail> details = new ArrayList<>();

        String sql = """
                SELECT
                    id_detalle,
                    id_ingreso,
                    id_animal,
                    cantidad,
                    sexo,
                    edad,
                    peso,
                    estado_ingreso,
                    observaciones
                FROM detalle_ingreso
                WHERE id_ingreso = ?
                ORDER BY id_detalle
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entryId
            );

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    EntryDetail detail =
                            new EntryDetail();

                    detail.setDetailId(
                            result.getInt("id_detalle")
                    );

                    detail.setEntryId(
                            result.getInt("id_ingreso")
                    );

                    detail.setAnimalId(
                            result.getInt("id_animal")
                    );

                    detail.setQuantity(
                            result.getInt("cantidad")
                    );

                    detail.setSex(
                            result.getString("sexo")
                    );

                    detail.setAge(
                            result.getString("edad")
                    );

                    detail.setWeight(
                            result.getDouble("peso")
                    );

                    detail.setEntryStatus(
                            result.getString(
                                    "estado_ingreso"
                            )
                    );

                    detail.setObservations(
                            result.getString(
                                    "observaciones"
                            )
                    );

                    details.add(detail);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing entry details."
            );

            System.out.println(e.getMessage());
        }

        return details;
    }

    public void update(EntryDetail detail) {

        String sql = """
                UPDATE detalle_ingreso
                SET
                    id_animal = ?,
                    cantidad = ?,
                    sexo = ?,
                    edad = ?,
                    peso = ?,
                    estado_ingreso = ?,
                    observaciones = ?
                WHERE id_detalle = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    detail.getAnimalId()
            );

            statement.setInt(
                    2,
                    detail.getQuantity()
            );

            statement.setString(
                    3,
                    detail.getSex()
            );

            statement.setString(
                    4,
                    detail.getAge()
            );

            statement.setDouble(
                    5,
                    detail.getWeight()
            );

            statement.setString(
                    6,
                    detail.getEntryStatus()
            );

            statement.setString(
                    7,
                    detail.getObservations()
            );

            statement.setInt(
                    8,
                    detail.getDetailId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Entry detail updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating entry detail."
            );

            System.out.println(e.getMessage());
        }
    }

    public void delete(int detailId) {

        String sql = """
                DELETE FROM detalle_ingreso
                WHERE id_detalle = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    detailId
            );

            statement.executeUpdate();

            System.out.println(
                    "Entry detail deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting entry detail."
            );

            System.out.println(e.getMessage());
        }
    }
}