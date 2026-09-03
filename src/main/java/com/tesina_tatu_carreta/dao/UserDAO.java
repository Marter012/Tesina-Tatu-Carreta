package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.User;

public class UserDAO {

    // =========================
    // ADD
    // =========================

    public void add(User user) {

        String sql = """
                INSERT INTO usuarios (
                    nombre_usuario,
                    usuario,
                    password,
                    rol,
                    estado
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    user.getFullName()
            );

            statement.setString(
                    2,
                    user.getUsername()
            );

            statement.setString(
                    3,
                    user.getPassword()
            );

            statement.setString(
                    4,
                    user.getRole()
            );

            statement.setString(
                    5,
                    user.getStatus()
            );

            statement.executeUpdate();

            System.out.println(
                    "User added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding user."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // LIST
    // =========================

    public List<User> list() {

        List<User> users =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_usuario,
                    nombre_usuario,
                    usuario,
                    password,
                    rol,
                    estado
                FROM usuarios
                ORDER BY nombre_usuario
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                User user =
                        new User();

                user.setUserId(
                        result.getInt(
                                "id_usuario"
                        )
                );

                user.setFullName(
                        result.getString(
                                "nombre_usuario"
                        )
                );

                user.setUsername(
                        result.getString(
                                "usuario"
                        )
                );

                user.setPassword(
                        result.getString(
                                "password"
                        )
                );

                user.setRole(
                        result.getString(
                                "rol"
                        )
                );

                user.setStatus(
                        result.getString(
                                "estado"
                        )
                );

                users.add(user);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing users."
            );

            System.out.println(e.getMessage());
        }

        return users;
    }


    // =========================
    // UPDATE
    // =========================

    public void update(User user) {

        String sql = """
                UPDATE usuarios
                SET
                    nombre_usuario = ?,
                    usuario = ?,
                    password = ?,
                    rol = ?,
                    estado = ?
                WHERE id_usuario = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    user.getFullName()
            );

            statement.setString(
                    2,
                    user.getUsername()
            );

            statement.setString(
                    3,
                    user.getPassword()
            );

            statement.setString(
                    4,
                    user.getRole()
            );

            statement.setString(
                    5,
                    user.getStatus()
            );

            statement.setInt(
                    6,
                    user.getUserId()
            );

            statement.executeUpdate();

            System.out.println(
                    "User updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating user."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // DELETE
    // =========================

    public void delete(int userId) {

        String sql = """
                DELETE FROM usuarios
                WHERE id_usuario = ?
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    userId
            );

            statement.executeUpdate();

            System.out.println(
                    "User deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting user."
            );

            System.out.println(e.getMessage());
        }
    }


    // =========================
    // LOGIN
    // =========================

    public User login(
            String username,
            String password) {

        String sql = """
                SELECT
                    id_usuario,
                    nombre_usuario,
                    usuario,
                    password,
                    rol,
                    estado
                FROM usuarios
                WHERE usuario = ?
                AND password = ?
                AND estado = 'Activo'
                """;

        try (Connection connection =
                     SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    username
            );

            statement.setString(
                    2,
                    password
            );

            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {

                    User user =
                            new User();

                    user.setUserId(
                            result.getInt(
                                    "id_usuario"
                            )
                    );

                    user.setFullName(
                            result.getString(
                                    "nombre_usuario"
                            )
                    );

                    user.setUsername(
                            result.getString(
                                    "usuario"
                            )
                    );

                    user.setPassword(
                            result.getString(
                                    "password"
                            )
                    );

                    user.setRole(
                            result.getString(
                                    "rol"
                            )
                    );

                    user.setStatus(
                            result.getString(
                                    "estado"
                            )
                    );

                    return user;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error logging in."
            );

            System.out.println(e.getMessage());
        }

        return null;
    }
}