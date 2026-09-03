package com.tesina_tatu_carreta.model;

public class Movement {

    private int movementId;

    // Animal associated with the movement
    private int animalId;

    // Optional: identifies the entry from which the movement originated
    private Integer entryId;

    private String movementDate;

    // RELEASE / TRANSFER / DEATH / PERMANENT ENCLOSURE
    private String movementType;

    private int quantity;

    // Mainly used for transfers
    private String destination;

    private String observations;


    // =========================
    // EMPTY CONSTRUCTOR
    // =========================

    public Movement() {
    }


    // =========================
    // FULL CONSTRUCTOR
    // =========================

    public Movement(
            int movementId,
            int animalId,
            Integer entryId,
            String movementDate,
            String movementType,
            int quantity,
            String destination,
            String observations) {

        this.movementId = movementId;
        this.animalId = animalId;
        this.entryId = entryId;
        this.movementDate = movementDate;
        this.movementType = movementType;
        this.quantity = quantity;
        this.destination = destination;
        this.observations = observations;
    }


    // =========================
    // MOVEMENT ID
    // =========================

    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }


    // =========================
    // ANIMAL ID
    // =========================

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }


    // =========================
    // ENTRY ID
    // =========================

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }


    // =========================
    // DATE
    // =========================

    public String getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(String movementDate) {
        this.movementDate = movementDate;
    }


    // =========================
    // MOVEMENT TYPE
    // =========================

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }


    // =========================
    // QUANTITY
    // =========================

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    // =========================
    // DESTINATION
    // =========================

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    // =========================
    // OBSERVATIONS
    // =========================

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}