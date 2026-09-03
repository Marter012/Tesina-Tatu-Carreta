package com.tesina_tatu_carreta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.Entry;

public class EntryDAO {

    public void add(Entry entry) {

        String sql = """
                INSERT INTO ingresos (
                    numero_acta,
                    fecha_ingreso,
                    organismo_procedencia,
                    responsable_entrega,
                    procedencia,
                    motivo_ingreso,
                    documentacion,
                    observaciones
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    entry.getRecordNumber()
            );

            statement.setString(
                    2,
                    entry.getEntryDate()
            );

            statement.setString(
                    3,
                    entry.getSourceOrganization()
            );

            statement.setString(
                    4,
                    entry.getDeliveryResponsible()
            );

            statement.setString(
                    5,
                    entry.getOrigin()
            );

            statement.setString(
                    6,
                    entry.getEntryReason()
            );

            statement.setString(
                    7,
                    entry.getDocumentation()
            );

            statement.setString(
                    8,
                    entry.getObservations()
            );

            statement.executeUpdate();

            System.out.println(
                    "Entry added successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error adding entry."
            );

            System.out.println(e.getMessage());
        }
    }

    public List<Entry> list() {

        List<Entry> entries = new ArrayList<>();

        String sql = """
                SELECT
                    id_ingreso,
                    numero_acta,
                    fecha_ingreso,
                    organismo_procedencia,
                    responsable_entrega,
                    procedencia,
                    motivo_ingreso,
                    documentacion,
                    observaciones
                FROM ingresos
                ORDER BY id_ingreso DESC
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                Entry entry = new Entry();

                entry.setEntryId(
                        result.getInt("id_ingreso")
                );

                entry.setRecordNumber(
                        result.getString("numero_acta")
                );

                entry.setEntryDate(
                        result.getString("fecha_ingreso")
                );

                entry.setSourceOrganization(
                        result.getString(
                                "organismo_procedencia"
                        )
                );

                entry.setDeliveryResponsible(
                        result.getString(
                                "responsable_entrega"
                        )
                );

                entry.setOrigin(
                        result.getString("procedencia")
                );

                entry.setEntryReason(
                        result.getString("motivo_ingreso")
                );

                entry.setDocumentation(
                        result.getString("documentacion")
                );

                entry.setObservations(
                        result.getString("observaciones")
                );

                entries.add(entry);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing entries."
            );

            System.out.println(e.getMessage());
        }

        return entries;
    }

    public void update(Entry entry) {

        String sql = """
                UPDATE ingresos
                SET
                    numero_acta = ?,
                    fecha_ingreso = ?,
                    organismo_procedencia = ?,
                    responsable_entrega = ?,
                    procedencia = ?,
                    motivo_ingreso = ?,
                    documentacion = ?,
                    observaciones = ?
                WHERE id_ingreso = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    entry.getRecordNumber()
            );

            statement.setString(
                    2,
                    entry.getEntryDate()
            );

            statement.setString(
                    3,
                    entry.getSourceOrganization()
            );

            statement.setString(
                    4,
                    entry.getDeliveryResponsible()
            );

            statement.setString(
                    5,
                    entry.getOrigin()
            );

            statement.setString(
                    6,
                    entry.getEntryReason()
            );

            statement.setString(
                    7,
                    entry.getDocumentation()
            );

            statement.setString(
                    8,
                    entry.getObservations()
            );

            statement.setInt(
                    9,
                    entry.getEntryId()
            );

            statement.executeUpdate();

            System.out.println(
                    "Entry updated successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error updating entry."
            );

            System.out.println(e.getMessage());
        }
    }

    public void delete(int entryId) {

        String sql = """
                DELETE FROM ingresos
                WHERE id_ingreso = ?
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entryId
            );

            statement.executeUpdate();

            System.out.println(
                    "Entry deleted successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error deleting entry."
            );

            System.out.println(e.getMessage());
        }
    }

    public List<Entry> listByAnimal(int animalId) {

        List<Entry> entries = new ArrayList<>();

        String sql = """
                SELECT DISTINCT
                    i.id_ingreso,
                    i.numero_acta,
                    i.fecha_ingreso,
                    i.organismo_procedencia,
                    i.responsable_entrega,
                    i.procedencia,
                    i.motivo_ingreso,
                    i.documentacion,
                    i.observaciones
                FROM ingresos i
                INNER JOIN detalle_ingreso d
                    ON i.id_ingreso = d.id_ingreso
                WHERE d.id_animal = ?
                ORDER BY i.id_ingreso DESC
                """;

        try (Connection connection = SQLiteConnection.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    animalId
            );

            try (ResultSet result =
                         statement.executeQuery()) {

                while (result.next()) {

                    Entry entry = new Entry();

                    entry.setEntryId(
                            result.getInt("id_ingreso")
                    );

                    entry.setRecordNumber(
                            result.getString("numero_acta")
                    );

                    entry.setEntryDate(
                            result.getString("fecha_ingreso")
                    );

                    entry.setSourceOrganization(
                            result.getString(
                                    "organismo_procedencia"
                            )
                    );

                    entry.setDeliveryResponsible(
                            result.getString(
                                    "responsable_entrega"
                            )
                    );

                    entry.setOrigin(
                            result.getString("procedencia")
                    );

                    entry.setEntryReason(
                            result.getString(
                                    "motivo_ingreso"
                            )
                    );

                    entry.setDocumentation(
                            result.getString(
                                    "documentacion"
                            )
                    );

                    entry.setObservations(
                            result.getString(
                                    "observaciones"
                            )
                    );

                    entries.add(entry);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listing entries by animal."
            );

            System.out.println(e.getMessage());
        }

        return entries;
    }
}