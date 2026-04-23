package com.sprms.system.hbmbeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_bsa_master")
public class BSA {

    @Id
    @Column(name = "bsa_id")
    private Long bsaId;

    @Column(name = "bsa_code", nullable = false, unique = true, length = 50)
    private String bsaCode;

    @Column(name = "bsa_name", nullable = false, length = 255)
    private String bsaName;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private Cities city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id")
    private College institute;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private BSAStatus status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Constructors
    public BSA() {
        this.status = BSAStatus.ACTIVE;
    }

    public BSA(Long bsaId, String bsaCode, String bsaName) {
        this.bsaId = bsaId;
        this.bsaCode = bsaCode;
        this.bsaName = bsaName;
        this.status = BSAStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getBsaId() {
        return bsaId;
    }

    public void setBsaId(Long bsaId) {
        this.bsaId = bsaId;
    }

    public String getBsaCode() {
        return bsaCode;
    }

    public void setBsaCode(String bsaCode) {
        this.bsaCode = bsaCode;
    }

    public String getBsaName() {
        return bsaName;
    }

    public void setBsaName(String bsaName) {
        this.bsaName = bsaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Cities getCity() {
        return city;
    }

    public void setCity(Cities city) {
        this.city = city;
    }

    public College getInstitute() {
        return institute;
    }

    public void setInstitute(College institute) {
        this.institute = institute;
    }

    public BSAStatus getStatus() {
        return status;
    }

    public void setStatus(BSAStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "BSA{" +
                "bsaId=" + bsaId +
                ", bsaCode='" + bsaCode + '\'' +
                ", bsaName='" + bsaName + '\'' +
                ", status=" + status +
                '}';
    }
}
