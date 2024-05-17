package com.martincastroalvarez.hex.hex.domain.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Message {
    private Integer id;
    private String subject;
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime sentAt;
    private Set<User> recipients = new HashSet<>();
    private User sender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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

    public Set<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<User> recipients) {
        this.recipients = recipients;
    }

    public void addRecipient(User user) {
        this.recipients.add(user);
        user.getMessages().add(this);
    }

    public void removeRecipient(User user) {
        this.recipients.remove(user);
        user.getMessages().remove(this);
    }
}
