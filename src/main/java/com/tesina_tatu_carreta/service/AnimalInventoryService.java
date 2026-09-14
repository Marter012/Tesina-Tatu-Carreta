package com.tesina_tatu_carreta.service;

import com.tesina_tatu_carreta.dao.AnimalDAO;
import com.tesina_tatu_carreta.dao.AnimalHoldingDAO;
import com.tesina_tatu_carreta.dao.EntryDAO;
import com.tesina_tatu_carreta.dao.MovementDAO;
import com.tesina_tatu_carreta.database.SQLiteConnection;
import com.tesina_tatu_carreta.model.AnimalHolding;
import com.tesina_tatu_carreta.model.EntryDetail;
import com.tesina_tatu_carreta.model.Movement;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class AnimalInventoryService {

    public static final String QUARANTINE =
            "QUARANTINE";

    public static final String PERMANENT =
            "PERMANENT";

    public static final String ENTRY =
            "ENTRY";

    public static final String TRANSFER =
            "TRANSFER";

    public static final String DEATH =
            "DEATH";

    public static final String EXIT =
            "EXIT";

    private final AnimalHoldingDAO holdingDAO =
            new AnimalHoldingDAO();

    private final MovementDAO movementDAO =
            new MovementDAO();

    private final EntryDAO entryDAO =
            new EntryDAO();

    private final AnimalDAO animalDAO =
            new AnimalDAO();

    // =========================================================
    // ENTRY
    // =========================================================

    public void registerEntry(
            Connection connection,
            EntryDetail detail,
            String movementDate)
            throws SQLException {

        validateLocation(
                detail.getDestination()
        );

        validateQuantity(
                detail.getQuantity()
        );

        AnimalHolding holding =
                holdingDAO.find(
                        connection,
                        detail.getAnimalId(),
                        detail.getEntryId(),
                        detail.getDestination()
                );

        if (holding == null) {

            AnimalHolding newHolding =
                    new AnimalHolding();

            newHolding.setAnimalId(
                    detail.getAnimalId()
            );

            newHolding.setEntryId(
                    detail.getEntryId()
            );

            newHolding.setLocationType(
                    detail.getDestination()
            );

            newHolding.setQuantity(
                    detail.getQuantity()
            );

            newHolding.setStatus(
                    "Active"
            );

            holdingDAO.add(
                    connection,
                    newHolding
            );

        } else {

            holdingDAO.updateQuantity(
                    connection,
                    holding.getHoldingId(),
                    holding.getQuantity()
                            + detail.getQuantity(),
                    "Active"
            );
        }

        // =====================================================
        // SYNCHRONIZE ANIMAL CURRENT QUANTITY
        // =====================================================

        synchronizeAnimalQuantity(
                connection,
                detail.getAnimalId()
        );

        // =====================================================
        // REGISTER MOVEMENT
        // =====================================================

        Movement movement =
                new Movement();

        movement.setAnimalId(
                detail.getAnimalId()
        );

        movement.setEntryId(
                detail.getEntryId()
        );

        movement.setMovementDate(
                movementDate
        );

        movement.setMovementType(
                ENTRY
        );

        movement.setQuantity(
                detail.getQuantity()
        );

        movement.setOriginLocation(
                null
        );

        movement.setDestination(
                detail.getDestination()
        );

        movement.setObservations(
                detail.getObservations()
        );

        movementDAO.add(
                connection,
                movement
        );
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    public void transfer(
            int animalId,
            Integer entryId,
            String fromLocation,
            String toLocation,
            int quantity,
            String observations) {

        transfer(
                animalId,
                entryId,
                fromLocation,
                toLocation,
                quantity,
                LocalDate.now().toString(),
                observations
        );
    }

    // =========================================================
    // TRANSFER WITH CUSTOM DATE
    // =========================================================

    public void transfer(
            int animalId,
            Integer entryId,
            String fromLocation,
            String toLocation,
            int quantity,
            String movementDate,
            String observations) {

        validateLocation(fromLocation);
        validateLocation(toLocation);

        if (fromLocation.equals(toLocation)) {

            throw new IllegalArgumentException(
                    "Source and destination locations must be different."
            );
        }

        validateQuantity(quantity);

        try (Connection connection =
                     SQLiteConnection.connect()) {

            connection.setAutoCommit(false);

            try {

                transfer(
                        connection,
                        animalId,
                        entryId,
                        fromLocation,
                        toLocation,
                        quantity,
                        movementDate,
                        observations
                );

                connection.commit();

            } catch (Exception exception) {

                connection.rollback();

                throw new RuntimeException(
                        exception.getMessage(),
                        exception
                );
            }

        } catch (Exception exception) {

            if (exception instanceof RuntimeException) {

                throw (RuntimeException) exception;
            }

            throw new RuntimeException(
                    exception.getMessage(),
                    exception
            );
        }
    }

    // =========================================================
    // TRANSFER - SAME CONNECTION
    // =========================================================

    public void transfer(
            Connection connection,
            int animalId,
            Integer entryId,
            String fromLocation,
            String toLocation,
            int quantity,
            String movementDate,
            String observations)
            throws SQLException {

        validateLocation(fromLocation);
        validateLocation(toLocation);

        if (fromLocation.equals(toLocation)) {

            throw new IllegalArgumentException(
                    "Source and destination locations must be different."
            );
        }

        validateQuantity(quantity);

        AnimalHolding source =
                requireAvailableHolding(
                        connection,
                        animalId,
                        entryId,
                        fromLocation,
                        quantity
                );

        AnimalHolding target =
                holdingDAO.find(
                        connection,
                        animalId,
                        entryId,
                        toLocation
                );

        if (target == null) {

            AnimalHolding newHolding =
                    new AnimalHolding();

            newHolding.setAnimalId(
                    animalId
            );

            newHolding.setEntryId(
                    entryId
            );

            newHolding.setLocationType(
                    toLocation
            );

            newHolding.setQuantity(
                    quantity
            );

            newHolding.setStatus(
                    "Active"
            );

            holdingDAO.add(
                    connection,
                    newHolding
            );

        } else {

            holdingDAO.updateQuantity(
                    connection,
                    target.getHoldingId(),
                    target.getQuantity()
                            + quantity,
                    "Active"
            );
        }

        int remaining =
                source.getQuantity()
                        - quantity;

        holdingDAO.updateQuantity(
                connection,
                source.getHoldingId(),
                remaining,
                remaining == 0
                        ? "Inactive"
                        : "Active"
        );

        // =====================================================
        // TRANSFER DOES NOT CHANGE TOTAL QUANTITY
        // BUT WE SYNCHRONIZE IT ANYWAY
        // =====================================================

        synchronizeAnimalQuantity(
                connection,
                animalId
        );

        Movement movement =
                new Movement();

        movement.setAnimalId(
                animalId
        );

        movement.setEntryId(
                entryId
        );

        movement.setMovementDate(
                movementDate
        );

        movement.setMovementType(
                TRANSFER
        );

        movement.setQuantity(
                quantity
        );

        movement.setOriginLocation(
                fromLocation
        );

        movement.setDestination(
                toLocation
        );

        movement.setObservations(
                observations
        );

        movementDAO.add(
                connection,
                movement
        );

        appendEntryObservation(
                connection,
                entryId,
                buildTransferObservation(
                        quantity,
                        fromLocation,
                        toLocation,
                        observations
                )
        );
    }

    // =========================================================
    // DEATH
    // =========================================================

    public void registerDeath(
            int animalId,
            Integer entryId,
            String location,
            int quantity,
            String observations) {

        registerDeath(
                animalId,
                entryId,
                location,
                quantity,
                LocalDate.now().toString(),
                observations
        );
    }

    // =========================================================
    // DEATH WITH CUSTOM DATE
    // =========================================================

    public void registerDeath(
            int animalId,
            Integer entryId,
            String location,
            int quantity,
            String movementDate,
            String observations) {

        removeQuantity(
                animalId,
                entryId,
                location,
                quantity,
                DEATH,
                null,
                movementDate,
                observations
        );
    }

    // =========================================================
    // EXIT
    // =========================================================

    public void registerExit(
            int animalId,
            Integer entryId,
            String location,
            int quantity,
            String destination,
            String observations) {

        registerExit(
                animalId,
                entryId,
                location,
                quantity,
                destination,
                LocalDate.now().toString(),
                observations
        );
    }

    // =========================================================
    // EXIT WITH CUSTOM DATE
    // =========================================================

    public void registerExit(
            int animalId,
            Integer entryId,
            String location,
            int quantity,
            String destination,
            String movementDate,
            String observations) {

        if (destination == null
                || destination.isBlank()) {

            throw new IllegalArgumentException(
                    "Exit destination is required."
            );
        }

        removeQuantity(
                animalId,
                entryId,
                location,
                quantity,
                EXIT,
                destination,
                movementDate,
                observations
        );
    }

    // =========================================================
    // REMOVE QUANTITY
    // =========================================================

    private void removeQuantity(
            int animalId,
            Integer entryId,
            String location,
            int quantity,
            String movementType,
            String destination,
            String movementDate,
            String observations) {

        validateLocation(location);
        validateQuantity(quantity);

        try (Connection connection =
                     SQLiteConnection.connect()) {

            connection.setAutoCommit(false);

            try {

                AnimalHolding source =
                        requireAvailableHolding(
                                connection,
                                animalId,
                                entryId,
                                location,
                                quantity
                        );

                int remaining =
                        source.getQuantity()
                                - quantity;

                holdingDAO.updateQuantity(
                        connection,
                        source.getHoldingId(),
                        remaining,
                        remaining == 0
                                ? "Inactive"
                                : "Active"
                );

                // =================================================
                // SYNCHRONIZE ANIMAL CURRENT QUANTITY
                // =================================================

                synchronizeAnimalQuantity(
                        connection,
                        animalId
                );

                // =================================================
                // CREATE MOVEMENT
                // =================================================

                Movement movement =
                        new Movement();

                movement.setAnimalId(
                        animalId
                );

                movement.setEntryId(
                        entryId
                );

                movement.setMovementDate(
                        movementDate == null
                                || movementDate.isBlank()
                                ? LocalDate.now().toString()
                                : movementDate
                );

                movement.setMovementType(
                        movementType
                );

                movement.setQuantity(
                        quantity
                );

                movement.setOriginLocation(
                        location
                );

                movement.setDestination(
                        destination
                );

                movement.setObservations(
                        observations
                );

                movementDAO.add(
                        connection,
                        movement
                );

                // =================================================
                // ADD HISTORICAL OBSERVATION TO ENTRY
                // =================================================

                String entryObservation;

                if (EXIT.equals(movementType)) {

                    entryObservation =
                            buildExitObservation(
                                    quantity,
                                    location,
                                    destination,
                                    observations
                            );

                } else if (DEATH.equals(movementType)) {

                    entryObservation =
                            buildDeathObservation(
                                    quantity,
                                    location,
                                    observations
                            );

                } else {

                    entryObservation =
                            observations;
                }

                appendEntryObservation(
                        connection,
                        entryId,
                        entryObservation
                );

                connection.commit();

            } catch (Exception exception) {

                connection.rollback();

                throw new RuntimeException(
                        exception.getMessage(),
                        exception
                );
            }

        } catch (Exception exception) {

            if (exception instanceof RuntimeException) {

                throw (RuntimeException) exception;
            }

            throw new RuntimeException(
                    exception.getMessage(),
                    exception
            );
        }
    }

    // =========================================================
    // SYNCHRONIZE ANIMAL QUANTITY
    // =========================================================

    private void synchronizeAnimalQuantity(
            Connection connection,
            int animalId)
            throws SQLException {

        int currentQuantity =
                holdingDAO.getTotalAvailableQuantity(
                        connection,
                        animalId
                );

        animalDAO.updateCurrentQuantity(
                connection,
                animalId,
                currentQuantity
        );
    }

    // =========================================================
    // AVAILABLE HOLDING
    // =========================================================

    private AnimalHolding requireAvailableHolding(
            Connection connection,
            int animalId,
            Integer entryId,
            String location,
            int quantity)
            throws SQLException {

        AnimalHolding holding =
                holdingDAO.find(
                        connection,
                        animalId,
                        entryId,
                        location
                );

        if (holding == null
                || holding.getQuantity() < quantity) {

            int available =
                    holding == null
                            ? 0
                            : holding.getQuantity();

            throw new IllegalArgumentException(
                    "The requested quantity is not available. "
                            + "Available: "
                            + available
            );
        }

        return holding;
    }

    // =========================================================
    // ENTRY OBSERVATION
    // =========================================================

    private void appendEntryObservation(
            Connection connection,
            Integer entryId,
            String observation)
            throws SQLException {

        if (entryId == null
                || observation == null
                || observation.isBlank()) {

            return;
        }

        entryDAO.appendObservation(
                connection,
                entryId,
                observation
        );
    }

    // =========================================================
    // OBSERVATION BUILDERS
    // =========================================================

    private String buildTransferObservation(
            int quantity,
            String fromLocation,
            String toLocation,
            String observations) {

        StringBuilder result =
                new StringBuilder();

        result.append(
                "Transfer of "
        );

        result.append(quantity);

        result.append(
                " animal(s) from "
        );

        result.append(
                fromLocation
        );

        result.append(
                " to "
        );

        result.append(
                toLocation
        );

        result.append(".");

        if (observations != null
                && !observations.isBlank()) {

            result.append(" ");

            result.append(
                    observations.trim()
            );
        }

        return result.toString();
    }

    private String buildExitObservation(
            int quantity,
            String location,
            String destination,
            String observations) {

        StringBuilder result =
                new StringBuilder();

        result.append(
                "Exit of "
        );

        result.append(quantity);

        result.append(
                " animal(s) from "
        );

        result.append(
                location
        );

        result.append(
                " to "
        );

        result.append(
                destination.trim()
        );

        result.append(".");

        if (observations != null
                && !observations.isBlank()) {

            result.append(" ");

            result.append(
                    observations.trim()
            );
        }

        return result.toString();
    }

    private String buildDeathObservation(
            int quantity,
            String location,
            String observations) {

        StringBuilder result =
                new StringBuilder();

        result.append(
                "Death of "
        );

        result.append(quantity);

        result.append(
                " animal(s) in "
        );

        result.append(
                location
        );

        result.append(".");

        if (observations != null
                && !observations.isBlank()) {

            result.append(" ");

            result.append(
                    observations.trim()
            );
        }

        return result.toString();
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateLocation(
            String location) {

        if (!QUARANTINE.equals(location)
                && !PERMANENT.equals(location)) {

            throw new IllegalArgumentException(
                    "Invalid inventory location."
            );
        }
    }

    private void validateQuantity(
            int quantity) {

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }
    }
}