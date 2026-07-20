package com.notsodaft.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiresAt;

    public Long getId(){ return id; }
    public String getToken(){ return token; }
    public void setToken(String token){ this.token = token; }
    public User getUser(){ return user; }
    public void setUser(User user){ this.user = user; }
    public LocalDateTime getExpiresAt(){ return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt){ this.expiresAt = expiresAt; }
    public boolean isExpired(){ return LocalDateTime.now().isAfter(expiresAt); }
}