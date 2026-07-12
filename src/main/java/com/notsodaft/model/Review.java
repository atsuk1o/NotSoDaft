package com.notsodaft.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private double lat;
    private double lng;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User author;

    private int dampnessScore;
    private int heatingScore;
    private int maintenanceScore;
    private int overallScore;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ReviewPhoto> photos = new ArrayList<>();

    @Column(length = 2000)
    private String reviewText;
    private String proofFileName;
    private String proofFilePath;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ReviewStatus{
        PENDING, APPROVED, REJECTED
    }

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    public enum PropertyType{
        STUDIO, ONE_BED, TWO_BED, THREE_PLUS
    }

    public Long getId(){ return id; }
    public String getEircode(){ return eircode; }
    public void setEircode(String eircode){ this.eircode = eircode; }
    public String getAddress(){ return address; }
    public void setAddress(String address){ this.address = address; }
    public String getCounty(){ return county; }
    public void setCounty(String county){ this.county = county; }
    public double getLat(){ return lat; }
    public void setLat(double lat){ this.lat = lat; }
    public double getLng(){ return lng; }
    public void setLng(double lng){ this.lng = lng; }
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
    public List<ReviewPhoto> getPhotos(){ return photos; }
    public void setPhotos(List<ReviewPhoto> photos){ this.photos = photos; }
    public String getProofFileName(){ return proofFileName; }
    public void setProofFileName(String proofFileName){ this.proofFileName = proofFileName; }
    public String getProofFilePath(){ return proofFilePath; }
    public void setProofFilePath(String proofFilePath){ this.proofFilePath = proofFilePath; }
    public PropertyType getPropertyType(){ return propertyType; }
    public void setPropertyType(PropertyType propertyType){ this.propertyType = propertyType; }
}