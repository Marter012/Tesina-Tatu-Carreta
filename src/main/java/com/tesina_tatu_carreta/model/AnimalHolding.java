package com.tesina_tatu_carreta.model;

public class AnimalHolding {

    private int holdingId;
    private int animalId;
    private Integer entryId;
    private String animalName;
    private String locationType;
    private int quantity;
    private String status;

    public AnimalHolding() {
    }

    public AnimalHolding(
            int holdingId,
            int animalId,
            Integer entryId,
            String animalName,
            String locationType,
            int quantity,
            String status) {

        this.holdingId = holdingId;
        this.animalId = animalId;
        this.entryId = entryId;
        this.animalName = animalName;
        this.locationType = locationType;
        this.quantity = quantity;
        this.status = status;
    }

    public int getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(int holdingId) {
        this.holdingId = holdingId;
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

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        String entryReference =
                entryId == null
                        ? "Without Entry"
                        : "Entry " + entryId;

        return entryReference
                + " - "
                + locationType
                + " - Quantity: "
                + quantity;
    }
}