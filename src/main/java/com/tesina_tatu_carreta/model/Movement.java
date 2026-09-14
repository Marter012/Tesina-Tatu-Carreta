package com.tesina_tatu_carreta.model;

public class Movement {

    private int movementId;
    private int animalId;
    private Integer entryId;
    private String movementDate;
    private String movementType;
    private int quantity;
    private String originLocation;
    private String destination;
    private String observations;

    public Movement() {
    }

    public Movement(
            int animalId,
            Integer entryId,
            String movementDate,
            String movementType,
            int quantity,
            String originLocation,
            String destination,
            String observations) {

        this.animalId = animalId;
        this.entryId = entryId;
        this.movementDate = movementDate;
        this.movementType = movementType;
        this.quantity = quantity;
        this.originLocation = originLocation;
        this.destination = destination;
        this.observations = observations;
    }

    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public String getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(String movementDate) {
        this.movementDate = movementDate;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}