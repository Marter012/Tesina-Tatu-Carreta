package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.EntryDetailRoster;

public class EntryDetailRosterDAO {

    // =========================
    // LIST
    // =========================

    public List<EntryDetailRoster> list() {

        List<EntryDetailRoster> details =
                new ArrayList<>();

        String sql = """
                SELECT
                    d.detail_id,
                    a.common_name,
                    d.entry_id,
                    d.quantity
                FROM entry_details d
                INNER JOIN animals a
                    ON d.animal_id = a.animal_id
                ORDER BY d.entry_id DESC
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
                                        "detail_id"
                                ),
                                result.getString(
                                        "common_name"
                                ),
                                result.getInt(
                                        "entry_id"
                                ),
                                result.getInt(
                                        "quantity"
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