package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.Message;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import com.martincastroalvarez.hex.hex.domain.ports.out.MessageRepository;
import com.martincastroalvarez.hex.hex.domain.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class MessageApplication extends HexagonalApplication implements MessageService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    private final List<String> MESSAGE_SORT_KEYS = List.of("id", "sender", "subject", "creationDate", "sentAt");

    @Override
    public Message getMessage(Integer messageId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting message. MessageId: %d", messageId));
        return messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public List<Message> listMessages(LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing all messages. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        if (start == null || end == null)
            return messageRepository.get(getPageRequest(page, size, sort, asc, MESSAGE_SORT_KEYS)).getContent();
        return messageRepository.get(start, end, getPageRequest(page, size, sort, asc, MESSAGE_SORT_KEYS)).getContent();
    }

    @Override
    public List<Message> listMessages(Integer userId, LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalEntityNotFoundException {
        logger.info(String.format("Listing messages for user ID: %d between %s and %s. Page: %d, Size: %d, Sort: %s, Ascending: %b", userId, start, end, page, size, sort, asc));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (start == null || end == null)
            return messageRepository.get(user, getPageRequest(page, size, sort, asc, MESSAGE_SORT_KEYS)).getContent();
        return messageRepository.get(user, start, end, getPageRequest(page, size, sort, asc, MESSAGE_SORT_KEYS)).getContent();
    }

    @Override
    public Message createNewMessage(User user, String subject, String text) throws HexagonalValidationException {
        logger.info(String.format("Creating new message. Subject: %s. Text: %s", subject, text));
        if (subject == null || subject.isBlank())
            throw new InvalidSubjectException();
        if (text == null || text.isBlank())
            throw new InvalidTextException();
        Message message = new Message();
        message.setSubject(subject);
        message.setSender(user);
        message.setText(text);
        message.setCreationDate(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessageSubject(Integer messageId, String subject) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Updating message subject. MessageId: %s, New Subject: %s", messageId, subject));
        if (subject == null || subject.isBlank())
            throw new InvalidSubjectException();
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        message.setSubject(subject);
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessageText(Integer messageId, String text) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Updating message text. MessageId: %s, New Text: %s", messageId, text));
        if (text == null || text.isBlank())
            throw new InvalidTextException();
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        message.setText(text);
        return messageRepository.save(message);
    }

    @Override
    public Set<User> addRecipient(Integer messageId, Integer userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Adding recipient to message. MessageId: %d, UserId: %d", messageId, userId));
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        if (message.sentAt != null)
            throw new MessageAlreadySentException();
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        message.addRecipient(user);
        messageRepository.save(message);
        return message.getRecipients();
    }

    @Override
    public Set<User> listRecipients(Integer messageId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing message recipients. MessageId: %d, Page: %d, Size: %d, Sort: %s, Ascending: %b", messageId, page, size, sort, asc));
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        return message.getRecipients().stream()
            .sorted(Comparator.comparing(User::getId))
            .skip(page * size)
            .limit(size)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<User> removeRecipient(Integer messageId, Integer userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Removing recipient from message. MessageId: %d, UserId: %d", messageId, userId));
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (message.getRecipients().contains(user)) {
            message.removeRecipient(user);
            messageRepository.save(message);
        }
        return message.getRecipients();
    }

    @Override
    public Message sendMessage(Integer messageId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Sending message. MessageId: %d", messageId));
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        if (message.getSentAt() != null)
            throw new MessageAlreadySentException();
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public void deleteUnsentMessage(Integer messageId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Deleting unsent message. MessageId: %d", messageId));
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        if (message.getSentAt() != null)
            throw new MessageAlreadySentException();
        messageRepository.delete(message.getId());
    }
}
