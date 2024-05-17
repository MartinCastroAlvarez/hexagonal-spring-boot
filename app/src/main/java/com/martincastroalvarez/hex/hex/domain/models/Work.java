package com.martincastroalvarez.hex.hex.domain.models;

import java.time.LocalDateTime;

public class Work {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User user;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
        user.getWork().add(this);
    }
}
