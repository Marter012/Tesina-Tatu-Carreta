package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Movement;

public class MovementDAO {

    // =========================
    // ADD
    // =========================

    public void add(Movement movement) {

        String sql = """
                INSERT INTO movimientos (
                    id_animal,
                    id_ingreso,
                    fecha_movimiento,
                    tipo_movimiento,
                    cantidad,
                    destino,
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
                    movement.getAnimalId()
            );

            // OPTIONAL ENTRY ID
            if (movement.getEntryId() != null) {

                statement.setInt(
                        2,
                        movement.getEntryId()
                );

            } else {

                statement.setNull(
                        2,
                        java.sql.Types.INTEGER
                );
            }

            statement.setString(
                    3,
                    movement.getMovementDate()
            );

            statement.setString(
                    4,
                    movement.getMovementType()
            );

            statement.setInt(
                    5,
                    movement.getQuantity()
            );

            statement.setString(
                    6,
                    movement.getDestination()
            );

            statement.setString(
                    7,
                    movement.getObservations()
            );

            statement.executeUpdate();

            System.out.println(
                    "Movement added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding movement."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // LIST
    // =========================

    public List<Movement> list() {

        List<Movement> movements =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_movimiento,
                    id_animal,
                    id_ingreso,
                    fecha_movimiento,
                    tipo_movimiento,
                    cantidad,
                    destino,
                    observaciones
                FROM movimientos
                ORDER BY fecha_movimiento DESC
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                Movement movement =
                        new Movement();

                movement.setMovementId(
                        result.getInt(
                                "id_movimiento"
                        )
                );

                movement.setAnimalId(
                        result.getInt(
                                "id_animal"
                        )
                );

                int entryId =
                        result.getInt(
                                "id_ingreso"
                        );

                if (result.wasNull()) {

                    movement.setEntryId(null);

                } else {

                    movement.setEntryId(
                            entryId
                    );
                }

                movement.setMovementDate(
                        result.getString(
                                "fecha_movimiento"
                        )
                );

                movement.setMovementType(
                        result.getString(
                                "tipo_movimiento"
                        )
                );

                movement.setQuantity(
                        result.getInt(
                                "cantidad"
                        )
                );

                movement.setDestination(
                        result.getString(
                                "destino"
                        )
                );

                movement.setObservations(
                        result.getString(
                                "observaciones"
                        )
                );

                movements.add(
                        movement
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing movements."
            );

            System.out.println(e.getMessage());
        }

        return movements;
    }


    // =========================
    // DELETE
    // =========================

    public void delete(int movementId) {

        String sql = """
                DELETE FROM movimientos
                WHERE id_movimiento = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    movementId
            );

            statement.executeUpdate();

            System.out.println(
                    "Movement deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting movement."
            );

            System.out.println(e.getMessage());
        }
    }
}