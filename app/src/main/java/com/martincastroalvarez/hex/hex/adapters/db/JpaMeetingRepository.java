package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.MeetingEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.MeetingMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.Meeting;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.MeetingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaMeetingRepository extends JpaRepository<MeetingEntity, Integer>, MeetingRepository {
    @Query("SELECT m FROM MeetingEntity m JOIN m.invitees i WHERE :user MEMBER OF m.invitees AND m.date >= :start")
    Page<MeetingEntity> findByInviteeAndStartDate(UserEntity user, LocalDateTime start, Pageable pageable);

    @Query("SELECT m FROM MeetingEntity m JOIN m.invitees i WHERE :user MEMBER OF m.invitees")
    Page<MeetingEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT m FROM MeetingEntity m JOIN m.invitees i WHERE :user MEMBER OF m.invitees AND m.date >= :start AND m.date <= :end")
    Page<MeetingEntity> findByInviteeAndStartEndDate(UserEntity user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT m FROM MeetingEntity m WHERE m.date >= :start")
    Page<MeetingEntity> findByStartDate(LocalDateTime start, Pageable pageable);

    @Query("SELECT m FROM MeetingEntity m WHERE m.date >= :start AND m.date <= :end")
    Page<MeetingEntity> findByStartEndDate(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT m FROM MeetingEntity m")
    Page<MeetingEntity> find(Pageable pageable);
    
    default Page<Meeting> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(MeetingMapper::toMeeting);
    }

    default Optional<Meeting> get(Integer id) {
        return findById(id).map(MeetingMapper::toMeeting);
    }

    default Page<Meeting> get(User user, LocalDateTime start, Pageable pageable) {
        return findByInviteeAndStartDate(UserMapper.toUserEntity(user), start, pageable).map(MeetingMapper::toMeeting);
    }

    default Page<Meeting> get(Pageable pageable) {
        return find(pageable).map(MeetingMapper::toMeeting);
    }

    default Page<Meeting> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByInviteeAndStartEndDate(UserMapper.toUserEntity(user), start, end, pageable).map(MeetingMapper::toMeeting);
    }

    default Page<Meeting> get(LocalDateTime start, Pageable pageable) {
        return findByStartDate(start, pageable).map(MeetingMapper::toMeeting);
    }

    default Page<Meeting> get(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByStartEndDate(start, end, pageable).map(MeetingMapper::toMeeting);
    }

    @Override
    default Meeting save(Meeting meeting) {
        return MeetingMapper.toMeeting(save(MeetingMapper.toMeetingEntity(meeting)));
    }

    @Override
    default void delete(Integer meetingId) {
        deleteById(meetingId);
    }
}
