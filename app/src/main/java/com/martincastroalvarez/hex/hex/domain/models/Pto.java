package com.martincastroalvarez.hex.hex.domain.models;

import java.time.LocalDate;

public class Pto {
    private Integer id;
    private LocalDate day;
    private User user;
    private Type type;

    public enum Type { VACATION, SICK, PERSONAL }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.getPto().add(this);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
