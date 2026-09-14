package com.tesina_tatu_carreta.dao;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Species;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SpeciesDAO {

    // =========================================================
    // ADD
    // =========================================================

    public void add(Species species) {

        String sql = """
                INSERT INTO species (name)
                VALUES (?)
                """;

        try (Connection connection = SQLiteConnection.connect()) {

            if (connection == null) {
                System.out.println(
                        "Error: Could not connect to database."
                );
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(
                        1,
                        species.getName()
                );

                statement.executeUpdate();

                System.out.println(
                        "Species added successfully."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error adding species."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // LIST
    // =========================================================

    public List<Species> list() {

        List<Species> speciesList =
                new ArrayList<>();

        String sql = """
                SELECT species_id, name
                FROM species
                ORDER BY name ASC
                """;

        try (Connection connection = SQLiteConnection.connect()) {

            if (connection == null) {
                System.out.println(
                        "Error: Could not connect to database."
                );
                return speciesList;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql);
                 ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    Species species =
                            new Species();

                    species.setSpeciesId(
                            result.getInt("species_id")
                    );

                    species.setName(
                            result.getString("name")
                    );

                    speciesList.add(species);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing species."
            );

            e.printStackTrace();
        }

        return speciesList;
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public void update(Species species) {

        String sql = """
                UPDATE species
                SET name = ?
                WHERE species_id = ?
                """;

        try (Connection connection = SQLiteConnection.connect()) {

            if (connection == null) {
                System.out.println(
                        "Error: Could not connect to database."
                );
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(
                        1,
                        species.getName()
                );

                statement.setInt(
                        2,
                        species.getSpeciesId()
                );

                int rowsAffected =
                        statement.executeUpdate();

                if (rowsAffected > 0) {

                    System.out.println(
                            "Species updated successfully."
                    );

                } else {

                    System.out.println(
                            "No species found with ID: "
                                    + species.getSpeciesId()
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error updating species."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    public void delete(int speciesId) {

        String sql = """
                DELETE FROM species
                WHERE species_id = ?
                """;

        try (Connection connection = SQLiteConnection.connect()) {

            if (connection == null) {
                System.out.println(
                        "Error: Could not connect to database."
                );
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(
                        1,
                        speciesId
                );

                int rowsAffected =
                        statement.executeUpdate();

                if (rowsAffected > 0) {

                    System.out.println(
                            "Species deleted successfully."
                    );

                } else {

                    System.out.println(
                            "No species found with ID: "
                                    + speciesId
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error deleting species."
            );

            e.printStackTrace();
        }
    }
}