package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Species;

public class SpeciesDAO {

    public void add(Species species) {

        String sql = """
                INSERT INTO especies (nombre)
                VALUES (?)
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    species.getName()
            );

            statement.executeUpdate();

            System.out.println(
                    "Species added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding species."
            );

            System.out.println(e.getMessage());
        }
    }

    public List<Species> list() {

        List<Species> speciesList = new ArrayList<>();

        String sql = """
                SELECT id_especie, nombre
                FROM especies
                ORDER BY nombre
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                Species species = new Species();

                species.setSpeciesId(
                        result.getInt("id_especie")
                );

                species.setName(
                        result.getString("nombre")
                );

                speciesList.add(species);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing species."
            );

            System.out.println(e.getMessage());
        }

        return speciesList;
    }

    public void update(Species species) {

        String sql = """
                UPDATE especies
                SET nombre = ?
                WHERE id_especie = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    species.getName()
            );

            statement.setInt(
                    2,
                    species.getSpeciesId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Species updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating species."
            );

            System.out.println(e.getMessage());
        }
    }

    public void delete(int speciesId) {

        String sql = """
                DELETE FROM especies
                WHERE id_especie = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    speciesId
            );

            statement.executeUpdate();

            System.out.println(
                    "Species deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting species."
            );

            System.out.println(e.getMessage());
        }
    }
}