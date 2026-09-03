package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Animal;

public class AnimalDAO {

    // CREATE
    public void add(Animal animal) {

        String sql = """
                INSERT INTO animales (
                    id_especie,
                    nombre_vulgar,
                    nombre_cientifico,
                    cantidad_actual,
                    origen,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animal.getSpeciesId()
            );

            statement.setString(
                    2,
                    animal.getCommonName()
            );

            statement.setString(
                    3,
                    animal.getScientificName()
            );

            statement.setInt(
                    4,
                    animal.getCurrentQuantity()
            );

            statement.setString(
                    5,
                    animal.getOrigin()
            );

            statement.setString(
                    6,
                    animal.getStatus()
            );

            statement.executeUpdate();

            System.out.println(
                    "Animal added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding animal."
            );

            System.out.println(e.getMessage());
        }
    }


    // DELETE
    public void delete(int animalId) {

        String sql = """
                DELETE FROM animales
                WHERE id_animal = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId
            );

            statement.executeUpdate();

            System.out.println(
                    "Animal deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting animal."
            );

            System.out.println(e.getMessage());
        }
    }


    // UPDATE
    public void update(Animal animal) {

        String sql = """
                UPDATE animales
                SET
                    id_especie = ?,
                    nombre_vulgar = ?,
                    nombre_cientifico = ?,
                    cantidad_actual = ?,
                    origen = ?,
                    estado = ?
                WHERE id_animal = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animal.getSpeciesId()
            );

            statement.setString(
                    2,
                    animal.getCommonName()
            );

            statement.setString(
                    3,
                    animal.getScientificName()
            );

            statement.setInt(
                    4,
                    animal.getCurrentQuantity()
            );

            statement.setString(
                    5,
                    animal.getOrigin()
            );

            statement.setString(
                    6,
                    animal.getStatus()
            );

            statement.setInt(
                    7,
                    animal.getAnimalId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Animal updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating animal."
            );

            System.out.println(e.getMessage());
        }
    }


    // READ
    public List<Animal> list() {

        List<Animal> animals = new ArrayList<>();

        String sql = """
                SELECT
                    id_animal,
                    id_especie,
                    nombre_vulgar,
                    nombre_cientifico,
                    cantidad_actual,
                    origen,
                    estado
                FROM animales
                ORDER BY nombre_vulgar
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                Animal animal = new Animal();

                animal.setAnimalId(
                        result.getInt("id_animal")
                );

                animal.setSpeciesId(
                        result.getInt("id_especie")
                );

                animal.setCommonName(
                        result.getString("nombre_vulgar")
                );

                animal.setScientificName(
                        result.getString("nombre_cientifico")
                );

                animal.setCurrentQuantity(
                        result.getInt("cantidad_actual")
                );

                animal.setOrigin(
                        result.getString("origen")
                );

                animal.setStatus(
                        result.getString("estado")
                );

                animals.add(animal);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing animals."
            );

            System.out.println(e.getMessage());
        }

        return animals;
    }
}