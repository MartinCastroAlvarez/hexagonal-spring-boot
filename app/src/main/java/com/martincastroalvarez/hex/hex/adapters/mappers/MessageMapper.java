package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.MessageEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.MessageDTO;
import com.martincastroalvarez.hex.hex.domain.models.Message;

import java.util.stream.Collectors;

public class MessageMapper {
    public static Message toMessage(MessageEntity messageEntity) {
        Message message = new Message();
        message.setId(messageEntity.getId());
        message.setSubject(messageEntity.getSubject());
        message.setText(messageEntity.getText());
        message.setSender(UserMapper.toUser(messageEntity.getSender()));
        message.setCreationDate(messageEntity.getCreationDate());
        message.setSentAt(messageEntity.getSentAt());
        message.setRecipients(messageEntity.getRecipients().stream().map(UserMapper::toUser).collect(Collectors.toSet()));
        return message;
    }

    public static MessageEntity toMessageEntity(Message message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(message.getId());
        messageEntity.setSubject(message.getSubject());
        messageEntity.setSender(UserMapper.toUserEntity(message.getSender()));
        messageEntity.setText(message.getText());
        messageEntity.setCreationDate(message.getCreationDate());
        messageEntity.setSentAt(message.getSentAt());
        messageEntity.setRecipients(message.getRecipients().stream().map(UserMapper::toUserEntity).collect(Collectors.toSet()));
        return messageEntity;
    }

    public static MessageDTO toMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSubject(message.getSubject());
        messageDTO.setText(message.getText());
        messageDTO.setCreationDate(message.getCreationDate());
        messageDTO.setSentAt(message.getSentAt());
        messageDTO.setSenderId(message.getSender().getId());
        return messageDTO;
    }
}
