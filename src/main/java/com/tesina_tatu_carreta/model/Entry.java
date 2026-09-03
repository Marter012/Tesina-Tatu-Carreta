package com.tesina_tatu_carreta.model;

public class Entry {

    private int entryId;
    private String recordNumber;
    private String entryDate;
    private String sourceOrganization;
    private String deliveryResponsible;
    private String origin;
    private String entryReason;
    private String documentation;
    private String observations;

    public Entry() {
    }

    public Entry(
            int entryId,
            String recordNumber,
            String entryDate,
            String sourceOrganization,
            String deliveryResponsible,
            String origin,
            String entryReason,
            String documentation,
            String observations) {

        this.entryId = entryId;
        this.recordNumber = recordNumber;
        this.entryDate = entryDate;
        this.sourceOrganization = sourceOrganization;
        this.deliveryResponsible = deliveryResponsible;
        this.origin = origin;
        this.entryReason = entryReason;
        this.documentation = documentation;
        this.observations = observations;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getSourceOrganization() {
        return sourceOrganization;
    }

    public void setSourceOrganization(String sourceOrganization) {
        this.sourceOrganization = sourceOrganization;
    }

    public String getDeliveryResponsible() {
        return deliveryResponsible;
    }

    public void setDeliveryResponsible(String deliveryResponsible) {
        this.deliveryResponsible = deliveryResponsible;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getEntryReason() {
        return entryReason;
    }

    public void setEntryReason(String entryReason) {
        this.entryReason = entryReason;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}