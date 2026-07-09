package com.notsodaft.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "review_photos")
public class ReviewPhoto{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    @JsonBackReference
    private Review review;

    private String fileName;
    private String filePath;

    public Long getId(){ return id; }
    public Review getReview(){ return review; }
    public void setReview(Review review){ this.review = review; }
    public String getFileName(){ return fileName; }
    public void setFileName(String fileName){ this.fileName = fileName; }
    public String getFilePath(){ return filePath; }
    public void setFilePath(String filePath){ this.filePath = filePath; }
}