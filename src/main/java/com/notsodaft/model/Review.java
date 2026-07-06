package com.notsodaft.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eircode;

    private String address;
    private String county;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private int dampnessScore;
    private int heatingScore;
    private int maintenanceScore;
    private int overallScore;

    @Column(length = 2000)
    private String reviewText;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ReviewStatus{
        PENDING, APPROVED, REJECTED
    }

    public Long getId(){ return id; }
    public String getEircode(){ return eircode; }
    public void setEircode(String eircode){ this.eircode = eircode; }
    public String getAddress(){ return address; }
    public void setAddress(String address){ this.address = address; }
    public String getCounty(){ return county; }
    public void setCounty(String county){ this.county = county; }
    public User getAuthor(){ return author; }
    public void setAuthor(User author){ this.author = author; }
    public int getDampnessScore(){ return dampnessScore; }
    public void setDampnessScore(int s){ this.dampnessScore = s; }
    public int getHeatingScore(){ return heatingScore; }
    public void setHeatingScore(int s){ this.heatingScore = s; }
    public int getMaintenanceScore(){ return maintenanceScore; }
    public void setMaintenanceScore(int s){ this.maintenanceScore = s; }
    public int getOverallScore(){ return overallScore; }
    public void setOverallScore(int s){ this.overallScore = s; }
    public String getReviewText(){ return reviewText; }
    public void setReviewText(String t){ this.reviewText = t; }
    public ReviewStatus getStatus(){ return status; }
    public void setStatus(ReviewStatus status){ this.status = status; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}