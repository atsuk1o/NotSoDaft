package com.notsodaft.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User author;

    private String guestName;

    @Column(length = 1000)
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId(){ return id; }
    public Review getReview(){ return review; }
    public void setReview(Review review){ this.review = review; }
    public User getAuthor(){ return author; }
    public void setAuthor(User author){ this.author = author; }
    public String getGuestName(){ return guestName; }
    public void setGuestName(String guestName){ this.guestName = guestName; }
    public String getText(){ return text; }
    public void setText(String text){ this.text = text; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}