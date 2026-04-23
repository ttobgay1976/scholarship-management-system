package com.sprms.system.hbmbeans;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bsa_requests")
public class BSARequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;
    
    @Column(name = "bsa_name", nullable = false, length = 255)
    private String bsaName;
    
    @Column(name = "country_id")
    private Long countryId;
    
    @Column(name = "state_id")
    private Long stateId;
    
    @Column(name = "city_id")
    private Long cityId;
    
    @Column(name = "president_cid", nullable = false, length = 20)
    private String presidentCid;
    
    @Column(name = "vice_president_cid", nullable = false, length = 20)
    private String vicePresidentCid;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "supporting_documents", length = 500)
    private String supportingDocuments;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private RequestStatus requestStatus = RequestStatus.PENDING;
    
    @Column(name = "submitted_by", nullable = false, length = 100)
    private String submittedBy;
    
    @Column(name = "submitted_date", nullable = false)
    private LocalDateTime submittedDate;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "remarks", length = 500)
    private String remarks;
    
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private Country country;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", insertable = false, updatable = false)
    private State state;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private Cities city;
    
    // Constructors
    public BSARequest() {
        this.submittedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    
    public String getBsaName() {
        return bsaName;
    }
    
    public void setBsaName(String bsaName) {
        this.bsaName = bsaName;
    }
    
    public Long getCountryId() {
        return countryId;
    }
    
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
    public Long getStateId() {
        return stateId;
    }
    
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    
    public Long getCityId() {
        return cityId;
    }
    
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    
    public String getPresidentCid() {
        return presidentCid;
    }
    
    public void setPresidentCid(String presidentCid) {
        this.presidentCid = presidentCid;
    }
    
    public String getVicePresidentCid() {
        return vicePresidentCid;
    }
    
    public void setVicePresidentCid(String vicePresidentCid) {
        this.vicePresidentCid = vicePresidentCid;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSupportingDocuments() {
        return supportingDocuments;
    }
    
    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }
    
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
    
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    public String getSubmittedBy() {
        return submittedBy;
    }
    
    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }
    
    public LocalDateTime getSubmittedDate() {
        return submittedDate;
    }
    
    public void setSubmittedDate(LocalDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
