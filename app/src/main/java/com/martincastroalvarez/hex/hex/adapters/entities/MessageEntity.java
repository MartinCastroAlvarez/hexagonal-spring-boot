package com.martincastroalvarez.hex.hex.adapters.entities;

import jakarta.persistence.*;
import jakarta.annotation.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_generator")
    @SequenceGenerator(name="message_generator", sequenceName = "message_id_seq", allocationSize=1)
    private Integer id;

    @Nonnull
    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Nonnull
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Nonnull
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Nullable
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "sender")
    private UserEntity sender;

    @ManyToMany
    @JoinTable(
        name = "message_recipients",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> recipients = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Set<UserEntity> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<UserEntity> recipients) {
        this.recipients = recipients;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }
}
