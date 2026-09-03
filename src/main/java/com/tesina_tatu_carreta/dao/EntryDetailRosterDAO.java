package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.EntryDetailRoster;

public class EntryDetailRosterDAO {

    public List<EntryDetailRoster> list() {

        List<EntryDetailRoster> details =
                new ArrayList<>();

        String sql = """
                SELECT
                    d.id_detalle,
                    a.nombre_vulgar,
                    d.id_ingreso,
                    d.cantidad
                FROM detalle_ingreso d
                INNER JOIN animales a
                    ON d.id_animal = a.id_animal
                ORDER BY d.id_ingreso DESC
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                EntryDetailRoster detail =
                        new EntryDetailRoster(
                                result.getInt(
                                        "id_detalle"
                                ),
                                result.getString(
                                        "nombre_vulgar"
                                ),
                                result.getInt(
                                        "id_ingreso"
                                ),
                                result.getInt(
                                        "cantidad"
                                )
                        );

                details.add(detail);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing entered animals."
            );

            System.out.println(e.getMessage());
        }

        return details;
    }
}