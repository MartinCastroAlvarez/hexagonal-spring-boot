package com.martincastroalvarez.hex.hex.adapters.entities;

import jakarta.persistence.*;
import jakarta.annotation.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meetings")
public class MeetingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_generator")
    @SequenceGenerator(name="meeting_generator", sequenceName = "meeting_id_seq", allocationSize=1)
    private Integer id;

    @Nonnull
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Nonnull
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToMany
    @JoinTable(
        name = "meeting_invitees",
        joinColumns = @JoinColumn(name = "meeting_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> invitees = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "meeting_files",
        joinColumns = @JoinColumn(name = "meeting_id"),
        inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private Set<FileEntity> files = new HashSet<>();

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

    public Set<UserEntity> getInvitees() {
        return invitees;
    }

    public void setInvitees(Set<UserEntity> invitees) {
        this.invitees = invitees;
    }

    public Set<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(Set<FileEntity> files) {
        this.files = files;
    }
}
