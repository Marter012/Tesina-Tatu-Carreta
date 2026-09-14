package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.User;

public class UserDAO {

    // =========================================================
    // ADD USER
    // =========================================================

    public void add(User user) {

        String sql = """
                INSERT INTO users (
                    full_name,
                    username,
                    password,
                    role,
                    status
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = SQLiteConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getStatus());

            statement.executeUpdate();

            System.out.println(
                    "User added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding user."
            );

            System.out.println(
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // INITIALIZE ADMINISTRATOR
    // =========================================================

    public void initializeAdministrator() {

        String checkSql = """
                SELECT user_id
                FROM users
                WHERE username = ?
                """;

        String insertSql = """
                INSERT INTO users (
                    full_name,
                    username,
                    password,
                    role,
                    status
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = SQLiteConnection.connect();
                PreparedStatement checkStatement =
                        connection.prepareStatement(checkSql)
        ) {

            checkStatement.setString(
                    1,
                    "administrator"
            );

            try (
                    ResultSet result =
                            checkStatement.executeQuery()
            ) {

                if (result.next()) {

                    System.out.println(
                            "Administrator user already exists."
                    );

                    return;
                }
            }

            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(insertSql)
            ) {

                insertStatement.setString(
                        1,
                        "System Administrator"
                );

                insertStatement.setString(
                        2,
                        "administrator"
                );

                insertStatement.setString(
                        3,
                        "admin123"
                );

                insertStatement.setString(
                        4,
                        "Administrator"
                );

                insertStatement.setString(
                        5,
                        "Active"
                );

                insertStatement.executeUpdate();

                System.out.println(
                        "Administrator user created successfully."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error initializing administrator user."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // LIST USERS
    // =========================================================

    public List<User> list() {

        List<User> users =
                new ArrayList<>();

        String sql = """
                SELECT
                    user_id,
                    full_name,
                    username,
                    password,
                    role,
                    status
                FROM users
                ORDER BY full_name
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            while (result.next()) {

                User user =
                        new User();

                user.setUserId(
                        result.getInt("user_id")
                );

                user.setFullName(
                        result.getString("full_name")
                );

                user.setUsername(
                        result.getString("username")
                );

                user.setPassword(
                        result.getString("password")
                );

                user.setRole(
                        result.getString("role")
                );

                user.setStatus(
                        result.getString("status")
                );

                users.add(user);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing users."
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return users;
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    public void update(User user) {

        String sql = """
                UPDATE users
                SET
                    full_name = ?,
                    username = ?,
                    password = ?,
                    role = ?,
                    status = ?
                WHERE user_id = ?
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

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

            System.out.println(
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    public void delete(int userId) {

        String sql = """
                DELETE FROM users
                WHERE user_id = ?
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

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

            System.out.println(
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public User login(
            String username,
            String password
    ) {

        String sql = """
                SELECT
                    user_id,
                    full_name,
                    username,
                    password,
                    role,
                    status
                FROM users
                WHERE username = ?
                AND password = ?
                AND status = 'Active'
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    username
            );

            statement.setString(
                    2,
                    password
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                if (result.next()) {

                    User user =
                            new User();

                    user.setUserId(
                            result.getInt("user_id")
                    );

                    user.setFullName(
                            result.getString("full_name")
                    );

                    user.setUsername(
                            result.getString("username")
                    );

                    user.setPassword(
                            result.getString("password")
                    );

                    user.setRole(
                            result.getString("role")
                    );

                    user.setStatus(
                            result.getString("status")
                    );

                    return user;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error logging in."
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // TEST USERS
    // =========================================================

    public void testUsers() {

        String sql = """
                SELECT
                    user_id,
                    full_name,
                    username,
                    password,
                    role,
                    status
                FROM users
                """;

        try (
                Connection connection =
                        SQLiteConnection.connect();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            System.out.println(
                    "----- USERS -----"
            );

            boolean found = false;

            while (result.next()) {

                found = true;

                System.out.println(
                        "ID: " +
                        result.getInt("user_id")
                );

                System.out.println(
                        "Name: " +
                        result.getString("full_name")
                );

                System.out.println(
                        "Username: " +
                        result.getString("username")
                );

                System.out.println(
                        "Password: " +
                        result.getString("password")
                );

                System.out.println(
                        "Role: " +
                        result.getString("role")
                );

                System.out.println(
                        "Status: " +
                        result.getString("status")
                );

                System.out.println(
                        "----------------"
                );
            }

            if (!found) {

                System.out.println(
                        "NO USERS FOUND."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR READING USERS:"
            );

            e.printStackTrace();
        }
    }
}