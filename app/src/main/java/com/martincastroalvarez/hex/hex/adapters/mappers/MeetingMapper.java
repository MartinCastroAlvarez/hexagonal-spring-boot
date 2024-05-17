package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.MeetingEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.MeetingDTO;
import com.martincastroalvarez.hex.hex.domain.models.Meeting;

import java.util.stream.Collectors;

public class MeetingMapper {
    public static Meeting toMeeting(MeetingEntity meetingEntity) {
        Meeting meeting = new Meeting();
        meeting.setId(meetingEntity.getId());
        meeting.setTitle(meetingEntity.getTitle());
        meeting.setDate(meetingEntity.getDate());
        meeting.setInvitees(meetingEntity.getInvitees().stream().map(UserMapper::toUser).collect(Collectors.toSet()));
        meeting.setFiles(meetingEntity.getFiles().stream().map(FileMapper::toFile).collect(Collectors.toSet()));
        return meeting;
    }

    public static MeetingEntity toMeetingEntity(Meeting meeting) {
        MeetingEntity meetingEntity = new MeetingEntity();
        meetingEntity.setId(meeting.getId());
        meetingEntity.setTitle(meeting.getTitle());
        meetingEntity.setDate(meeting.getDate());
        meetingEntity.setInvitees(meeting.getInvitees().stream().map(UserMapper::toUserEntity).collect(Collectors.toSet()));
        meetingEntity.setFiles(meeting.getFiles().stream().map(FileMapper::toFileEntity).collect(Collectors.toSet()));
        if (meetingEntity.getDate() == null)
            throw new IllegalArgumentException("2 date is missing! " + meeting.getDate().toString());
        return meetingEntity;
    }

    public static MeetingDTO toMeetingDTO(Meeting meeting) {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(meeting.getId());
        meetingDTO.setTitle(meeting.getTitle());
        meetingDTO.setDate(meeting.getDate());
        return meetingDTO;
    }
}
