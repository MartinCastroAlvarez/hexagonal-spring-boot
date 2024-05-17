package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.domain.exceptions.MessageNotFoundException;
import com.martincastroalvarez.hex.hex.domain.ports.out.MessageRepository;
import com.martincastroalvarez.hex.hex.domain.models.Message;
import com.martincastroalvarez.hex.hex.adapters.dto.MessageDTO;
import com.martincastroalvarez.hex.hex.domain.services.MessageService;
import com.martincastroalvarez.hex.hex.adapters.dto.UserDTO;
import com.martincastroalvarez.hex.hex.adapters.mappers.MessageMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RestController
public class MessageController extends HexagonalController {
    @Autowired
    private MessageService messageApplication;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/auth/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        logger.info(String.format("Getting user messages: %s", user.getUsername()));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Message> messages = messageApplication.listMessages(user.getId(), startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s messages", messages.size()));
        return ResponseEntity.ok(messages.stream().map(MessageMapper::toMessageDTO).collect(Collectors.toList()));
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable Integer messageId) {
        logger.info(String.format("Getting message with id=%s", messageId));
        Message message = messageApplication.getMessage(messageId);
        logger.info(String.format("Message found with id=%s", message.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(MessageMapper.toMessageDTO(message));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> listMessages(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing messages with start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", start, end, page, size, sort, asc));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Message> messages = messageApplication.listMessages(startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s messages", messages.size()));
        return ResponseEntity.status(HttpStatus.OK).body(messages.stream().map(MessageMapper::toMessageDTO).collect(Collectors.toList()));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageRequest request) {
        logger.info(String.format("Creating message with subject=%s, text=%s", request.getSubject(), request.getText()));
        User user = getAuthenticatedUser();
        Message message = messageApplication.createNewMessage(user, request.getSubject(), request.getText());
        logger.info(String.format("Message created with id=%s", message.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageMapper.toMessageDTO(message));
    }

    @PutMapping("/messages/{messageId}")
    public ResponseEntity<MessageDTO> updateMessage(@PathVariable Integer messageId, @RequestBody MessageRequest request) {
        logger.info(String.format("Updating message with id=%s", messageId));
        if (request.getSubject() != null && !request.getSubject().isEmpty())
            messageApplication.updateMessageSubject(messageId, request.getSubject());
        if (request.getText() != null && !request.getText().isEmpty())
            messageApplication.updateMessageText(messageId, request.getText());
        Message message = messageRepository.get(messageId).orElseThrow(MessageNotFoundException::new);
        logger.info(String.format("Message updated with id=%s", message.getId()));
        return ResponseEntity.ok(MessageMapper.toMessageDTO(message));
    }

    @PostMapping("/messages/{messageId}")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable Integer messageId) {
        logger.info(String.format("Sending message with id=%s", messageId));
        Message message = messageApplication.sendMessage(messageId);
        logger.info(String.format("Message sent with id=%s", message.getId()));
        return ResponseEntity.ok(MessageMapper.toMessageDTO(message));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer messageId) {
        logger.info(String.format("Deleting message with id=%s", messageId));
        messageApplication.deleteUnsentMessage(messageId);
        logger.info(String.format("Message deleted with id=%s", messageId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/messages/{messageId}/users")
    public ResponseEntity<Set<UserDTO>> listRecipients(
        @PathVariable Integer messageId,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "true") Boolean asc
    ) {
        logger.info(String.format("Listing recipients of message with id=%s, page=%s, size=%s, sort=%s, asc=%s", messageId, page, size, sort, asc));
        Set<User> users = messageApplication.listRecipients(messageId, page, size, sort, asc);
        logger.info(String.format("Found %s recipients", users.size()));
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(UserMapper::toUserDTO).collect(Collectors.toSet()));
    }

    @PostMapping("/messages/{messageId}/users/{userId}")
    public ResponseEntity<Set<UserDTO>> addUserToMessage(@PathVariable Integer messageId, @PathVariable Integer userId) {
        logger.info(String.format("Adding user with id=%s to message with id=%s", userId, messageId));
        Set<User> users = messageApplication.addRecipient(messageId, userId);
        logger.info(String.format("User added to message with id=%s", messageId));
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(UserMapper::toUserDTO).collect(Collectors.toSet()));
    }

    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<List<MessageDTO>> listMessagesForUser(
        @PathVariable Integer userId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing messages for user with id=%s, start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", userId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Message> messages = messageApplication.listMessages(userId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s messages", messages.size()));
        return ResponseEntity.status(HttpStatus.OK).body(messages.stream().map(MessageMapper::toMessageDTO).collect(Collectors.toList()));
    }

    @DeleteMapping("/messages/{messageId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromMessage(@PathVariable Integer messageId, @PathVariable Integer userId) {
        logger.info(String.format("Removing user with id=%s from message with id=%s", userId, messageId));
        messageApplication.removeRecipient(messageId, userId);
        logger.info(String.format("User removed from message with id=%s", messageId));
        return ResponseEntity.noContent().build();
    }
}

class MessageRequest {
    private String subject;
    private String text;

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
}
