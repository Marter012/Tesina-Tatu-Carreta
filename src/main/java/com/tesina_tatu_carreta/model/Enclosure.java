package com.tesina_tatu_carreta.model;

public class Enclosure {

    private int enclosureId;
    private String name;
    private String sector;
    private Integer capacity;
    private String status;
    private String observations;

    public Enclosure() {
    }

    public Enclosure(
            int enclosureId,
            String name,
            String sector,
            Integer capacity,
            String status,
            String observations) {

        this.enclosureId = enclosureId;
        this.name = name;
        this.sector = sector;
        this.capacity = capacity;
        this.status = status;
        this.observations = observations;
    }

    public int getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(int enclosureId) {
        this.enclosureId = enclosureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    // This will be displayed in the ComboBox
    @Override
    public String toString() {
        return name + " - " + sector;
    }
}