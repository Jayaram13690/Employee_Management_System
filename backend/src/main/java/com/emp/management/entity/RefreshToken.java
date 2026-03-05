package com.emp.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Refresh token entity for JWT token renewal.
 * Stored in database to allow invalidation on logout.
 */
@Entity
@Table(name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "token"})
        },
        indexes = {
                @Index(name = "idx_token", columnList = "token"),
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_expiry_date", columnList = "expiry_date")
        })
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ===== Constructors =====

    public RefreshToken() {
    }

    public RefreshToken(Long id, User user, String token, LocalDateTime expiryDate, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Check if the token is still valid (not expired)
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
