package com.tesina_tatu_carreta.service;

import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.EntryDetailDAO;
import com.tesina_tatu_carreta.dao.MovementDAO;
import com.tesina_tatu_carreta.dao.EnclosureDAO;
import com.tesina_tatu_carreta.model.Entry;
import com.tesina_tatu_carreta.model.EntryDetail;
import com.tesina_tatu_carreta.database.SQLiteConnection;

import java.sql.Connection;
import java.util.List;

public class EntryService {

    private final EntryDAO entryDAO =
            new EntryDAO();

    private final EntryDetailDAO detailDAO =
            new EntryDetailDAO();

    private final MovementDAO movementDAO =
            new MovementDAO();

    private final EnclosureDAO enclosureDAO =
            new EnclosureDAO();

    private final AnimalInventoryService
            inventoryService =
            new AnimalInventoryService();

    public int createEntry(
            Entry entry,
            List<EntryDetail> details) {

        validateEntry(entry);
        validateDetails(details);

        try (Connection connection =
                     SQLiteConnection.connect()) {

            connection.setAutoCommit(false);

            try {

                int entryId =
                        entryDAO.insertAndReturnId(
                                connection,
                                entry
                        );

                for (EntryDetail detail : details) {

                    detail.setEntryId(
                            entryId
                    );

                    detailDAO.add(
                            connection,
                            detail
                    );

                    inventoryService.registerEntry(
                            connection,
                            detail,
                            entry.getEntryDate()
                    );

                    /*
                     * If an enclosure was selected,
                     * mark it as inactive.
                     */
                    if (detail.getEnclosureId() != null) {

                        enclosureDAO.updateStatus(
                                connection,
                                detail.getEnclosureId(),
                                "Inactive"
                        );
                    }
                }

                connection.commit();

                return entryId;

            } catch (Exception exception) {

                connection.rollback();

                throw exception;
            }

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage(),
                    exception
            );
        }
    }

    public void updateEntry(
            Entry entry) {

        validateEntry(entry);

        entryDAO.update(entry);
    }

    public void deleteEntry(
            int entryId) {

        if (movementDAO.existsForEntry(entryId)) {

            throw new IllegalStateException(
                    "This entry cannot be deleted because it already has inventory history."
            );
        }

        try (Connection connection =
                     SQLiteConnection.connect()) {

            connection.setAutoCommit(false);

            try {

                /*
                 * Before deleting the details,
                 * release their enclosures.
                 */
                List<EntryDetail> details =
                        detailDAO.listByEntry(entryId);

                for (EntryDetail detail : details) {

                    if (detail.getEnclosureId() != null) {

                        enclosureDAO.updateStatus(
                                connection,
                                detail.getEnclosureId(),
                                "Active"
                        );
                    }
                }

                detailDAO.deleteByEntry(
                        connection,
                        entryId
                );

                entryDAO.delete(
                        connection,
                        entryId
                );

                connection.commit();

            } catch (Exception exception) {

                connection.rollback();

                throw exception;
            }

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage(),
                    exception
            );
        }
    }

    private void validateEntry(
            Entry entry) {

        if (entry == null) {

            throw new IllegalArgumentException(
                    "Entry is required."
            );
        }

        if (entry.getRecordNumber() == null
                || entry.getRecordNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Record number is required."
            );
        }

        if (entry.getEntryDate() == null
                || entry.getEntryDate().isBlank()) {

            throw new IllegalArgumentException(
                    "Entry date is required."
            );
        }
    }

    private void validateDetails(
            List<EntryDetail> details) {

        if (details == null
                || details.isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one animal is required."
            );
        }

        for (EntryDetail detail : details) {

            if (detail.getAnimalId() <= 0) {

                throw new IllegalArgumentException(
                        "A valid animal is required."
                );
            }

            if (detail.getQuantity() <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero."
                );
            }

            if (detail.getWeight() < 0) {

                throw new IllegalArgumentException(
                        "Weight cannot be negative."
                );
            }

            if (!AnimalInventoryService.QUARANTINE
                    .equals(detail.getDestination())
                    && !AnimalInventoryService.PERMANENT
                    .equals(detail.getDestination())) {

                throw new IllegalArgumentException(
                        "A valid destination is required."
                );
            }
        }
    }
}