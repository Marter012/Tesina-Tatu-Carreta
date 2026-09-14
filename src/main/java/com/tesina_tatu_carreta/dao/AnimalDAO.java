package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Animal;

public class AnimalDAO {

    public void add(Animal animal) {

        String sql = """
                INSERT INTO animals (
                    species_id,
                    common_name,
                    scientific_name,
                    current_quantity,
                    origin,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(
                    1,
                    animal.getSpeciesId());

            statement.setString(
                    2,
                    animal.getCommonName());

            statement.setString(
                    3,
                    animal.getScientificName());

            statement.setInt(
                    4,
                    0);

            statement.setString(
                    5,
                    animal.getOrigin());

            statement.setString(
                    6,
                    animal.getStatus());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    animal.setAnimalId(
                            generatedKeys.getInt(1));
                }
            }

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Error adding animal.",
                    exception);
        }
    }

    public void update(Animal animal) {

        String sql = """
                UPDATE animals
                SET
                    species_id = ?,
                    common_name = ?,
                    scientific_name = ?,
                    origin = ?,
                    status = ?
                WHERE animal_id = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animal.getSpeciesId());

            statement.setString(
                    2,
                    animal.getCommonName());

            statement.setString(
                    3,
                    animal.getScientificName());

            statement.setString(
                    4,
                    animal.getOrigin());

            statement.setString(
                    5,
                    animal.getStatus());

            statement.setInt(
                    6,
                    animal.getAnimalId());

            statement.executeUpdate();

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Error updating animal.",
                    exception);
        }
    }

    public void updateCurrentQuantity(
            Connection connection,
            int animalId,
            int currentQuantity)
            throws java.sql.SQLException {

        String status =
                currentQuantity > 0
                        ? "Active"
                        : "Inactive";

        String sql = """
                UPDATE animals
                SET
                    current_quantity = ?,
                    status = ?
                WHERE animal_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    currentQuantity);

            statement.setString(
                    2,
                    status);

            statement.setInt(
                    3,
                    animalId);

            statement.executeUpdate();
        }
    }

    public void delete(int animalId) {

        String sql = """
                DELETE FROM animals
                WHERE animal_id = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId);

            statement.executeUpdate();

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Error deleting animal.",
                    exception);
        }
    }

    public List<Animal> list() {

        List<Animal> animals = new ArrayList<>();

        String sql = """
                SELECT
                    animal_id,
                    species_id,
                    common_name,
                    scientific_name,
                    current_quantity,
                    origin,
                    status
                FROM animals
                ORDER BY common_name
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                Animal animal = new Animal();

                animal.setAnimalId(
                        result.getInt("animal_id"));

                animal.setSpeciesId(
                        result.getInt("species_id"));

                animal.setCommonName(
                        result.getString("common_name"));

                animal.setScientificName(
                        result.getString("scientific_name"));

                animal.setCurrentQuantity(
                        result.getInt("current_quantity"));

                animal.setOrigin(
                        result.getString("origin"));

                animal.setStatus(
                        result.getString("status"));

                animals.add(animal);
            }

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Error listing animals.",
                    exception);
        }

        return animals;
    }

    public Animal findById(int animalId) {

        String sql = """
                SELECT
                    animal_id,
                    species_id,
                    common_name,
                    scientific_name,
                    current_quantity,
                    origin,
                    status
                FROM animals
                WHERE animal_id = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId);

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {

                    Animal animal = new Animal();

                    animal.setAnimalId(
                            result.getInt("animal_id"));

                    animal.setSpeciesId(
                            result.getInt("species_id"));

                    animal.setCommonName(
                            result.getString("common_name"));

                    animal.setScientificName(
                            result.getString("scientific_name"));

                    animal.setCurrentQuantity(
                            result.getInt("current_quantity"));

                    animal.setOrigin(
                            result.getString("origin"));

                    animal.setStatus(
                            result.getString("status"));

                    return animal;
                }
            }

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Error finding animal.",
                    exception);
        }

        return null;
    }
}