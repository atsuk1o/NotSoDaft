package com.notsodaft.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "properties")
public class Property{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String eircode;

    private String address;
    private String county;
    private double pricePerMonth;
    private int bedrooms;
    private double lat;
    private double lng;

    @Enumerated(EnumType.STRING)
    private LeaseType leaseType;

    private boolean verified = false;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "landlord_id")
    private User landlord;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum LeaseType{
        SHORT_TERM, LONG_TERM
    }

    public Long getId(){ return id; }
    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }
    public String getEircode(){ return eircode; }
    public void setEircode(String eircode){ this.eircode = eircode; }
    public String getAddress(){ return address; }
    public void setAddress(String address){ this.address = address; }
    public String getCounty(){ return county; }
    public void setCounty(String county){ this.county = county; }
    public double getPricePerMonth(){ return pricePerMonth; }
    public void setPricePerMonth(double pricePerMonth){ this.pricePerMonth = pricePerMonth; }
    public int getBedrooms(){ return bedrooms; }
    public void setBedrooms(int bedrooms){ this.bedrooms = bedrooms; }
    public double getLat(){ return lat; }
    public void setLat(double lat){ this.lat = lat; }
    public double getLng(){ return lng; }
    public void setLng(double lng){ this.lng = lng; }
    public LeaseType getLeaseType(){ return leaseType; }
    public void setLeaseType(LeaseType leaseType){ this.leaseType = leaseType; }
    public boolean isVerified(){ return verified; }
    public void setVerified(boolean verified){ this.verified = verified; }
    public boolean isActive(){ return active; }
    public void setActive(boolean active){ this.active = active; }
    public User getLandlord(){ return landlord; }
    public void setLandlord(User landlord){ this.landlord = landlord; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}