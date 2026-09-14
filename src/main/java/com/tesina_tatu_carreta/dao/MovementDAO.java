package com.tesina_tatu_carreta.dao;

import com.tesina_tatu_carreta.model.Movement;
import com.tesina_tatu_carreta.database.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MovementDAO {

        public void add(Movement movement) {

                try (
                                Connection connection = SQLiteConnection.connect()) {

                        add(connection, movement);

                } catch (SQLException exception) {

                        throw new RuntimeException(
                                        "Error adding movement.",
                                        exception);
                }
        }

        public int add(
                        Connection connection,
                        Movement movement)
                        throws SQLException {

                String sql = """
                                INSERT INTO movements
                                (
                                    animal_id,
                                    entry_id,
                                    movement_date,
                                    movement_type,
                                    quantity,
                                    origin_location,
                                    destination,
                                    observations
                                )
                                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                                """;

                try (PreparedStatement statement = connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS)) {

                        statement.setInt(
                                        1,
                                        movement.getAnimalId());

                        if (movement.getEntryId() == null) {
                                statement.setNull(
                                                2,
                                                Types.INTEGER);
                        } else {
                                statement.setInt(
                                                2,
                                                movement.getEntryId());
                        }

                        statement.setString(
                                        3,
                                        movement.getMovementDate());

                        statement.setString(
                                        4,
                                        movement.getMovementType());

                        statement.setInt(
                                        5,
                                        movement.getQuantity());

                        if (movement.getOriginLocation() == null) {
                                statement.setNull(
                                                6,
                                                Types.VARCHAR);
                        } else {
                                statement.setString(
                                                6,
                                                movement.getOriginLocation());
                        }

                        if (movement.getDestination() == null) {
                                statement.setNull(
                                                7,
                                                Types.VARCHAR);
                        } else {
                                statement.setString(
                                                7,
                                                movement.getDestination());
                        }

                        statement.setString(
                                        8,
                                        movement.getObservations());

                        statement.executeUpdate();

                        try (ResultSet keys = statement.getGeneratedKeys()) {

                                if (keys.next()) {
                                        return keys.getInt(1);
                                }
                        }
                }

                return 0;
        }

        public List<Movement> list() {

                List<Movement> result = new ArrayList<>();

                String sql = """
                                SELECT
                                    movement_id,
                                    animal_id,
                                    entry_id,
                                    movement_date,
                                    movement_type,
                                    quantity,
                                    origin_location,
                                    destination,
                                    observations
                                FROM movements
                                ORDER BY movement_id DESC
                                """;

                try (Connection connection = SQLiteConnection.connect();
                                PreparedStatement statement = connection.prepareStatement(sql);
                                ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {

                                Movement movement = new Movement();

                                movement.setMovementId(
                                                resultSet.getInt(
                                                                "movement_id"));

                                movement.setAnimalId(
                                                resultSet.getInt(
                                                                "animal_id"));

                                int entryId = resultSet.getInt(
                                                "entry_id");

                                movement.setEntryId(
                                                resultSet.wasNull()
                                                                ? null
                                                                : entryId);

                                movement.setMovementDate(
                                                resultSet.getString(
                                                                "movement_date"));

                                movement.setMovementType(
                                                resultSet.getString(
                                                                "movement_type"));

                                movement.setQuantity(
                                                resultSet.getInt(
                                                                "quantity"));

                                movement.setOriginLocation(
                                                resultSet.getString(
                                                                "origin_location"));

                                movement.setDestination(
                                                resultSet.getString(
                                                                "destination"));

                                movement.setObservations(
                                                resultSet.getString(
                                                                "observations"));

                                result.add(movement);
                        }

                } catch (SQLException exception) {
                        exception.printStackTrace();
                }

                return result;
        }

        public boolean existsForEntry(
                        int entryId) {

                String sql = "SELECT COUNT(*) FROM movements WHERE entry_id = ?";

                try (Connection connection = SQLiteConnection.connect();
                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setInt(1, entryId);

                        try (ResultSet result = statement.executeQuery()) {

                                return result.next()
                                                && result.getInt(1) > 0;
                        }

                } catch (SQLException exception) {
                        exception.printStackTrace();
                        return false;
                }
        }
}