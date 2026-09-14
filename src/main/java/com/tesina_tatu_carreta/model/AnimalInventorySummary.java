package com.tesina_tatu_carreta.model;

public class AnimalInventorySummary {

    private int animalId;
    private String commonName;
    private String scientificName;
    private int permanentQuantity;
    private int quarantineQuantity;
    private String status;

    public AnimalInventorySummary() {
    }

    public AnimalInventorySummary(
            int animalId,
            String commonName,
            String scientificName,
            int permanentQuantity,
            int quarantineQuantity,
            String status) {

        this.animalId = animalId;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.permanentQuantity = permanentQuantity;
        this.quarantineQuantity = quarantineQuantity;
        this.status = status;
    }

    public int getAnimalId() {
        return animalId;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public int getPermanentQuantity() {
        return permanentQuantity;
    }

    public int getQuarantineQuantity() {
        return quarantineQuantity;
    }

    public int getTotalQuantity() {
        return permanentQuantity + quarantineQuantity;
    }

    public String getStatus() {
        return status;
    }
}