package com.tesina_tatu_carreta.model;

public class PermanentEnclosure {

    private int permanentEnclosureId;

    private int animalId;

    // Used to display the animal name in tables
    private String animalName;

    private int quantity;

    private String locationType;

    private Integer enclosureId;

    // Used to display the enclosure name in tables
    private String enclosureName;

    private String enclosureEntryDate;

    private String status;

    private String observations;


    // =========================
    // EMPTY CONSTRUCTOR
    // =========================

    public PermanentEnclosure() {
    }


    // =========================
    // CONSTRUCTOR
    // =========================

    public PermanentEnclosure(
            int permanentEnclosureId,
            int animalId,
            String animalName,
            int quantity,
            String locationType,
            Integer enclosureId,
            String enclosureName,
            String enclosureEntryDate,
            String status,
            String observations) {

        this.permanentEnclosureId = permanentEnclosureId;
        this.animalId = animalId;
        this.animalName = animalName;
        this.quantity = quantity;
        this.locationType = locationType;
        this.enclosureId = enclosureId;
        this.enclosureName = enclosureName;
        this.enclosureEntryDate = enclosureEntryDate;
        this.status = status;
        this.observations = observations;
    }


    // =========================
    // PERMANENT ENCLOSURE ID
    // =========================

    public int getPermanentEnclosureId() {
        return permanentEnclosureId;
    }

    public void setPermanentEnclosureId(int permanentEnclosureId) {
        this.permanentEnclosureId = permanentEnclosureId;
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
    // ANIMAL NAME
    // =========================

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
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
    // LOCATION TYPE
    // =========================

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }


    // =========================
    // ENCLOSURE ID
    // =========================

    public Integer getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(Integer enclosureId) {
        this.enclosureId = enclosureId;
    }


    // =========================
    // ENCLOSURE NAME
    // =========================

    public String getEnclosureName() {
        return enclosureName;
    }

    public void setEnclosureName(String enclosureName) {
        this.enclosureName = enclosureName;
    }


    // =========================
    // ENCLOSURE ENTRY DATE
    // =========================

    public String getEnclosureEntryDate() {
        return enclosureEntryDate;
    }

    public void setEnclosureEntryDate(
            String enclosureEntryDate) {

        this.enclosureEntryDate =
                enclosureEntryDate;
    }


    // =========================
    // STATUS
    // =========================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    // =========================
    // OBSERVATIONS
    // =========================

    public String getObservations() {
        return observations;
    }

    public void setObservations(
            String observations) {

        this.observations = observations;
    }
}