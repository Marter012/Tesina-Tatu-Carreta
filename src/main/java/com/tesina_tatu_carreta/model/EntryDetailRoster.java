package com.tesina_tatu_carreta.model;

public class EntryDetailRoster {

    private int detailId;
    private String animalName;
    private int entryId;
    private int quantity;

    public EntryDetailRoster(
            int detailId,
            String animalName,
            int entryId,
            int quantity) {

        this.detailId = detailId;
        this.animalName = animalName;
        this.entryId = entryId;
        this.quantity = quantity;
    }

    public int getDetailId() {
        return detailId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public int getEntryId() {
        return entryId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {

        return animalName
                + " - Quantity: "
                + quantity
                + " - Entry No. "
                + entryId;
    }
}