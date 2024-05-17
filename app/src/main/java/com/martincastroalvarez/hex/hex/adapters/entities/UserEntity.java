package com.martincastroalvarez.hex.hex.adapters.entities;

import com.martincastroalvarez.hex.hex.domain.models.User;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.annotation.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_id_seq", allocationSize=1)
    private Integer id;

    @Nonnull
    @Column(name = "name")
    private String name;

    @Nonnull
    @Column(name = "email", unique = true)
    private String email;

    @Nonnull
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Nonnull
    @Column(name = "sign_up_date")
    private LocalDateTime signUpDate;

    @Nonnull
    @Column(name = "is_active")
    private Boolean isActive;

    @Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private User.Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(LocalDateTime signUpDate) {
        this.signUpDate = signUpDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}
