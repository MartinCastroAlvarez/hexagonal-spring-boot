package com.martincastroalvarez.hex.hex.domain.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Meeting {
    private Integer id;
    private String title;
    private LocalDateTime date;
    private Set<User> invitees = new HashSet<>();
    private Set<File> files = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<User> getInvitees() {
        return invitees;
    }

    public void setInvitees(Set<User> invitees) {
        this.invitees = invitees;
    }

    public void addInvitee(User user) {
        this.invitees.add(user);
        user.getMeetings().add(this);
    }

    public void removeInvitee(User user) {
        this.invitees.remove(user);
        user.getMeetings().remove(this);
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public void addFile(File file) {
        this.files.add(file);
        file.getMeetings().add(this);
    }

    public void removeFile(File file) {
        this.files.remove(file);
        file.getMeetings().remove(this);
    }
}
