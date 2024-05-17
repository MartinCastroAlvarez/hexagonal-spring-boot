package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.File;
import com.martincastroalvarez.hex.hex.domain.models.Meeting;
import com.martincastroalvarez.hex.hex.domain.models.User;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.InputStream;
import java.util.List;

public interface MeetingService {
    Meeting getMeeting(Integer meetingId) throws HexagonalEntityNotFoundException;
    List<Meeting> listMeetings(LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<Meeting> listMeetings(Integer userId, LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    Meeting createNewMeeting(String title, LocalDateTime time) throws HexagonalValidationException;
    void deleteMeeting(Integer meetingId) throws HexagonalEntityNotFoundException;
    Meeting updateMeetingTitle(Integer meetingId, String newTitle) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    List<User> listInvitedUsers(Integer meetingId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    User inviteUserToMeeting(Integer meetingId, Integer userId) throws HexagonalEntityNotFoundException;
    void removeUserFromMeeting(Integer meetingId, Integer userId) throws HexagonalEntityNotFoundException;
    void deleteFileFromMeeting(Integer meetingId, Integer fileId) throws HexagonalEntityNotFoundException;
    List<File> listMeetingFiles(Integer meetingId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException;
    File uploadFileToMeeting(Integer meetingId, String fileName, InputStream fileStream) throws HexagonalEntityNotFoundException, HexagonalValidationException, NoSuchAlgorithmException, IOException;
    File downloadFileFromMeeting(Integer meetingId, Integer fileId) throws HexagonalEntityNotFoundException, IOException;
}