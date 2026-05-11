package com.silvercare.model;

import java.time.LocalDateTime;

public class FamilyLink {
    private int id;
    private int elderId;
    private int familyId;
    private String relationship;
    private LocalDateTime createdAt;

    public FamilyLink() {}

    public FamilyLink(int elderId, int familyId, String relationship) {
        this.elderId = elderId;
        this.familyId = familyId;
        this.relationship = relationship;
    }

    public FamilyLink(int id, int elderId, int familyId,
                      String relationship, LocalDateTime createdAt) {
        this.id = id;
        this.elderId = elderId;
        this.familyId = familyId;
        this.relationship = relationship;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getElderId() {
        return elderId;
    }

    public void setElderId(int elderId) {
        this.elderId = elderId;
    }


    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }


    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}