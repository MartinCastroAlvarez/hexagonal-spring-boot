package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.MessageEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.MessageMapper;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.models.Message;
import com.martincastroalvarez.hex.hex.domain.ports.out.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaMessageRepository extends JpaRepository<MessageEntity, Integer>, MessageRepository {
    @Query("SELECT m FROM MessageEntity m JOIN m.recipients r WHERE :recipient MEMBER OF m.recipients AND m.sentAt IS NOT NULL")
    Page<MessageEntity> findByUser(UserEntity recipient, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m JOIN m.recipients r WHERE :recipient MEMBER OF m.recipients AND m.creationDate >= :start AND m.sentAt IS NOT NULL")
    Page<MessageEntity> findByRecipientAndStartDate(UserEntity recipient, LocalDateTime start, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m JOIN m.recipients r WHERE :recipient MEMBER OF m.recipients AND m.creationDate >= :start AND m.creationDate <= :end AND m.sentAt IS NOT NULL")
    Page<MessageEntity> findByRecipientAndStartEndDate(UserEntity recipient, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.creationDate >= :start AND m.sentAt IS NOT NULL")
    Page<MessageEntity> findByStartDate(LocalDateTime start, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m WHERE m.creationDate >= :start AND m.creationDate <= :end")
    Page<MessageEntity> findByStartEndDate(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT m FROM MessageEntity m")
    Page<MessageEntity> find(Pageable pageable);

    @Override
    default Optional<Message> get(Integer id) {
        return findById(id).map(MessageMapper::toMessage);
    } 

    default Page<Message> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(MessageMapper::toMessage);
    }

    @Override
    default Page<Message> get(User recipient, LocalDateTime start, Pageable pageable) {
        return findByRecipientAndStartDate(UserMapper.toUserEntity(recipient), start, pageable).map(MessageMapper::toMessage);
    }

    @Override
    default Page<Message> get(Pageable pageable) {
        return find(pageable).map(MessageMapper::toMessage);
    }

    @Override
    default Page<Message> get(User recipient, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByRecipientAndStartEndDate(UserMapper.toUserEntity(recipient), start, end, pageable).map(MessageMapper::toMessage);
    }

    @Override
    default Page<Message> get(LocalDateTime start, Pageable pageable) {
        return findByStartDate(start, pageable).map(MessageMapper::toMessage);
    }

    @Override
    default Page<Message> get(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByStartEndDate(start, end, pageable).map(MessageMapper::toMessage);
    }

    @Override
    default Message save(Message message) {
        return MessageMapper.toMessage(save(MessageMapper.toMessageEntity(message)));
    }

    @Override
    default void delete(Integer messageId) {
        deleteById(messageId);
    }
}
