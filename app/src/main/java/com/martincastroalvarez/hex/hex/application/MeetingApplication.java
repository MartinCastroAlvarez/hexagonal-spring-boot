package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.File;
import com.martincastroalvarez.hex.hex.domain.models.Meeting;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.MeetingRepository;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import com.martincastroalvarez.hex.hex.domain.services.MeetingService;
import com.martincastroalvarez.hex.hex.domain.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingApplication extends HexagonalApplication implements MeetingService {
    @Value("${files.path}")
    private String filesPath;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    private final List<String> MEETING_SORT_KEYS = List.of("id", "title", "sender", "date");

    @Override
    public Meeting getMeeting(Integer meetingId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting meeting. MeetingId: %d", meetingId));
        return meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
    }

    @Override
    public List<Meeting> listMeetings(LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing all meetings. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        if (start == null || end == null)
            return meetingRepository.get(getPageRequest(page, size, sort, asc, MEETING_SORT_KEYS)).getContent();
        return meetingRepository.get(start, end, getPageRequest(page, size, sort, asc, MEETING_SORT_KEYS)).getContent();
    }

    @Override
    public List<Meeting> listMeetings(Integer userId, LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing meetings for user ID: %d between %s and %s. Page: %d, Size: %d, Sort: %s, Ascending: %b", userId, start, end, page, size, sort, asc));
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        if (start == null || end == null)
            return meetingRepository.get(user, getPageRequest(page, size, sort, asc, MEETING_SORT_KEYS)).getContent();
        return meetingRepository.get(user, start, end, getPageRequest(page, size, sort, asc, MEETING_SORT_KEYS)).getContent();
    }

    @Override
    public Meeting createNewMeeting(String title, LocalDateTime day) throws HexagonalValidationException {
        logger.info(String.format("Creating new meeting. Title: %s, Day: %s", title, day));
        if (title == null || title.isBlank())
            throw new InvalidNameException();
        Meeting meeting = new Meeting();
        meeting.setTitle(title);
        meeting.setDate(day);
        return meetingRepository.save(meeting);
    }

    @Override
    public void deleteMeeting(Integer meetingId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Deleting meeting. MeetingId: %d", meetingId));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        meetingRepository.delete(meeting.getId());
    }

    @Override
    public Meeting updateMeetingTitle(Integer meetingId, String newTitle) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Updating meeting title. MeetingId: %s, New Title: %s", meetingId, newTitle));
        if (newTitle == null || newTitle.isBlank())
            throw new InvalidNameException();
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        meeting.setTitle(newTitle);
        return meetingRepository.save(meeting);
    }

    @Override
    public List<User> listInvitedUsers(Integer meetingId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing invited users to meeting. MeetingId: %d, Page: %d, Size: %d, Sort: %s, Ascending: %b", meetingId, page, size, sort, asc));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        return meeting.getInvitees().stream()
            .sorted(Comparator.comparing(User::getId))
            .skip(page * size)
            .limit(size)
            .collect(Collectors.toList());
    }

    @Override
    public User inviteUserToMeeting(Integer meetingId, Integer userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Inviting user to meeting. MeetingId: %d, UserId: %d", meetingId, userId));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        meeting.addInvitee(user);
        meetingRepository.save(meeting);
        return user;
    }

    @Override
    public void removeUserFromMeeting(Integer meetingId, Integer userId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Removing user from meeting. MeetingId: %d, UserId: %d", meetingId, userId));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        User user = userRepository.get(userId).orElseThrow(UserNotFoundException::new);
        meeting.removeInvitee(user);
        meetingRepository.save(meeting);
    }

    @Override
    public File uploadFileToMeeting(Integer meetingId, String fileName, InputStream fileStream) throws HexagonalEntityNotFoundException, HexagonalValidationException, NoSuchAlgorithmException, IOException  {
        logger.info(String.format("Uploading file to meeting. MeetingId: %d", meetingId));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        File file = fileService.uploadFile(fileName, fileStream);
        meeting.addFile(file);
        meetingRepository.save(meeting);
        return file;
    }

    @Override
    public void deleteFileFromMeeting(Integer meetingId, Integer fileId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Deleting file from meeting. MeetingId: %d, FileId: %d", meetingId, fileId));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        File fileToRemove = meeting.getFiles().stream().filter(file -> file.getId().equals(fileId)).findFirst().orElseThrow(() -> new FileNotFoundException());
        meeting.removeFile(fileToRemove);
        meetingRepository.save(meeting);
    }

    @Override
    public List<File> listMeetingFiles(Integer meetingId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Listing files for meeting. MeetingId: %d, Page: %d, Size: %d, Sort: %s, Ascending: %b", meetingId, page, size, sort, asc));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        return meeting.getFiles().stream()
            .sorted(Comparator.comparing(File::getId))
            .skip(page * size)
            .limit(size)
            .collect(Collectors.toList());
    }

    @Override
    public File downloadFileFromMeeting(Integer meetingId, Integer fileId) throws HexagonalEntityNotFoundException, IOException {
        logger.info(String.format("Retrieving file bytes. FileId: %d", fileId));
        Meeting meeting = meetingRepository.get(meetingId).orElseThrow(MeetingNotFoundException::new);
        return fileService.downloadFile(meeting.getFiles(), fileId);
    }
}
