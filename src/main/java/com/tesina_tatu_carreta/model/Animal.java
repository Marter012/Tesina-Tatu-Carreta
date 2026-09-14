package com.tesina_tatu_carreta.model;

public class Animal {

    private int animalId;
    private int speciesId;
    private String commonName;
    private String scientificName;
    private int currentQuantity;
    private String origin;
    private String status;

    public Animal() {
    }

    public Animal(
            int animalId,
            int speciesId,
            String commonName,
            String scientificName,
            int currentQuantity,
            String origin,
            String status) {

        this.animalId = animalId;
        this.speciesId = speciesId;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.currentQuantity = currentQuantity;
        this.origin = origin;
        this.status = status;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return commonName;
    }
}