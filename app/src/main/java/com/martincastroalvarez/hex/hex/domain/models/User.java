package com.martincastroalvarez.hex.hex.domain.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User implements UserDetails {
    private Integer id;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime lastLoginDate;
    private LocalDateTime signUpDate;
    private Boolean isActive;
    private Role role;
    private List<Meeting> meetings = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Pto> pto = new ArrayList<>();
    private List<Schedule> schedule = new ArrayList<>();
    private List<Work> work = new ArrayList<>();

    protected static final Logger logger = Logger.getLogger(User.class.getName());

    public enum Role { USER, ADMIN, MANAGER, SALESMAN, PROVIDER }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Pto> getPto() {
        return pto;
    }

    public void setPto(List<Pto> pto) {
        this.pto = pto;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public List<Work> getWork() {
        return work;
    }

    public void setWork(List<Work> work) {
        this.work = work;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
        if (this.getRole() == Role.ADMIN) {
            for (Role role : Role.values()) {
                if (role != Role.ADMIN)
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            }
        } else if (this.getRole() != Role.USER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.USER.name()));
        }
        logger.info(String.format("User %s has authorities: %s", this.getEmail(), authorities));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive();
    }
}
