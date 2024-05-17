package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.Message;
import com.martincastroalvarez.hex.hex.domain.models.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MessageService {
    Message getMessage(Integer messageId) throws HexagonalEntityNotFoundException;
    List<Message> listMessages(LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<Message> listMessages(Integer userId, LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalEntityNotFoundException;
    Message createNewMessage(User user, String subject, String text) throws HexagonalValidationException;
    Message updateMessageSubject(Integer messageId, String subject) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    Message updateMessageText(Integer messageId, String text) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    Set<User> addRecipient(Integer messageId, Integer userId) throws HexagonalEntityNotFoundException;
    Set<User> removeRecipient(Integer messageId, Integer userId) throws HexagonalEntityNotFoundException;
    Set<User> listRecipients(Integer messageId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    Message sendMessage(Integer messageId) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    void deleteUnsentMessage(Integer messageId) throws HexagonalEntityNotFoundException, HexagonalValidationException;
}