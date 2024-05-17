package com.martincastroalvarez.hex.hex.adapters.web;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ContentDisposition;
import com.martincastroalvarez.hex.hex.adapters.dto.FileDTO;
import com.martincastroalvarez.hex.hex.adapters.dto.MeetingDTO;
import com.martincastroalvarez.hex.hex.adapters.dto.UserDTO;
import com.martincastroalvarez.hex.hex.adapters.mappers.FileMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.MeetingMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.File;
import com.martincastroalvarez.hex.hex.domain.models.Meeting;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayOutputStream;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;


@RestController
public class MeetingController extends HexagonalController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping("/auth/meetings")
    public ResponseEntity<List<MeetingDTO>> getMessages(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting user meetings: %s", user.getUsername()));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.plusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Meeting> meetings = meetingService.listMeetings(user.getId(), startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s meetings", meetings.size()));
        return ResponseEntity.ok(meetings.stream().map(MeetingMapper::toMeetingDTO).collect(Collectors.toList()));
    }

    @GetMapping("/meetings/{meetingId}")
    public ResponseEntity<MeetingDTO> getMeeting(@PathVariable Integer meetingId) {
        logger.info(String.format("Getting meeting with id=%s", meetingId));
        Meeting meeting = meetingService.getMeeting(meetingId);
        logger.info(String.format("Meeting found with id=%s", meeting.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(MeetingMapper.toMeetingDTO(meeting));
    }

    @GetMapping("/meetings")
    public ResponseEntity<List<MeetingDTO>> listMeetings(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing meetings with start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", start, end, page, size, sort, asc));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Meeting> meetings = meetingService.listMeetings(startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s meetings", meetings.size()));
        return ResponseEntity.status(HttpStatus.OK).body(meetings.stream().map(MeetingMapper::toMeetingDTO).collect(Collectors.toList()));
    }

    @PostMapping("/meetings")
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody MeetingRequest request) {
        logger.info(String.format("Creating meeting with title=%s, time=%s", request.getDay(), request.getDay()));
        Meeting meeting = meetingService.createNewMeeting(request.getTitle(), request.getDayAsDateTime());
        logger.info(String.format("Meeting created with id=%s", meeting.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(MeetingMapper.toMeetingDTO(meeting));
    }

    @DeleteMapping("/meetings/{meetingId}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Integer meetingId) {
        logger.info(String.format("Deleting meeting with id=%s", meetingId));
        meetingService.deleteMeeting(meetingId);
        logger.info(String.format("Meeting deleted with id=%s", meetingId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/meetings/{meetingId}")
    public ResponseEntity<MeetingDTO> updateMeeting(@PathVariable Integer meetingId, @RequestBody MeetingRequest request) {
        logger.info(String.format("Updating meeting with id=%s, title=%s, time=%s", meetingId, request.getTitle(), request.getDay()));
        Meeting meeting = meetingService.updateMeetingTitle(meetingId, request.getTitle());
        logger.info(String.format("Meeting updated with id=%s", meeting.getId()));
        return ResponseEntity.ok(MeetingMapper.toMeetingDTO(meeting));
    }

    @GetMapping("/meetings/{meetingId}/files")
    public ResponseEntity<List<FileDTO>> listMeetingFiles(
        @PathVariable Integer meetingId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing files of meeting with id=%s, page=%s, size=%s, sort=%s, asc=%s", meetingId, page, size, sort, asc));
        List<File> files = meetingService.listMeetingFiles(meetingId, page, size, sort, asc);
        logger.info(String.format("Found %s files", files.size()));
        return ResponseEntity.status(HttpStatus.OK).body(files.stream().map(FileMapper::toFileDTO).collect(Collectors.toList()));
    }

    @PostMapping("/meetings/{meetingId}/files")
    public ResponseEntity<FileDTO> uploadFileToMeeting(
        @PathVariable Integer meetingId,
        @RequestParam String fileName,
        @RequestParam("file") MultipartFile uploadedFile
    ) throws NoSuchAlgorithmException, IOException {
        logger.info(String.format("Uploading file to meeting with id=%s, name=%s", meetingId, fileName));
        File file = meetingService.uploadFileToMeeting(meetingId, fileName, uploadedFile.getInputStream());
        logger.info(String.format("File uploaded with id=%s", file.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(FileMapper.toFileDTO(file));
    }

    @GetMapping("/meetings/{meetingId}/files/{fileId}")
    public ResponseEntity<byte[]> downloadFile(
        @PathVariable Integer meetingId,
        @PathVariable Integer fileId
    ) throws NoSuchAlgorithmException, IOException {
        logger.info(String.format("Downloading file with id=%s from meeting with id=%s", fileId, meetingId));
        File file = meetingService.downloadFileFromMeeting(meetingId, fileId);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) file.getOutputStream();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("HexFileMetadata", "attachment; filename=\"" + file.getFileName() + "\"");
        logger.info(String.format("File downloaded with id=%s", file.getId()));
        return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
    }

    @DeleteMapping("/meetings/{meetingId}/files/{fileId}")
    public ResponseEntity<Void> deleteFileFromMeeting(
        @PathVariable Integer meetingId,
        @PathVariable Integer fileId
    ) {
        logger.info(String.format("Deleting file with id=%s from meeting with id=%s", fileId, meetingId));
        meetingService.deleteFileFromMeeting(meetingId, fileId);
        logger.info(String.format("File deleted with id=%s", fileId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/meetings")
    public ResponseEntity<List<MeetingDTO>> listUserMeetings(
        @PathVariable Integer userId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing meetings of user with id=%s, start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", userId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.plusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Meeting> meetings = meetingService.listMeetings(userId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s meetings", meetings.size()));
        return ResponseEntity.status(HttpStatus.OK).body(meetings.stream().map(MeetingMapper::toMeetingDTO).collect(Collectors.toList()));
    }

    @GetMapping("/meetings/{meetingId}/users")
    public ResponseEntity<List<UserDTO>> listInvitedUsers(
        @PathVariable Integer meetingId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing invited users of meeting with id=%s, page=%s, size=%s, sort=%s, asc=%s", meetingId, page, size, sort, asc));
        List<User> users = meetingService.listInvitedUsers(meetingId, page, size, sort, asc);
        logger.info(String.format("Found %s users", users.size()));
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(UserMapper::toUserDTO).collect(Collectors.toList()));
    }

    @PostMapping("/meetings/{meetingId}/users/{userId}")
    public ResponseEntity<UserDTO> inviteUserToMeeting(
        @PathVariable Integer meetingId,
        @PathVariable Integer userId
    ) {
        logger.info(String.format("Inviting user with id=%s to meeting with id=%s", userId, meetingId));
        User user = meetingService.inviteUserToMeeting(meetingId, userId);
        logger.info(String.format("User invited with id=%s", userId));
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserDTO(user));
    }

    @DeleteMapping("/meetings/{meetingId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromMeeting(
        @PathVariable Integer meetingId,
        @PathVariable Integer userId
    ) {
        logger.info(String.format("Removing user with id=%s from meeting with id=%s", userId, meetingId));
        meetingService.removeUserFromMeeting(meetingId, userId);
        logger.info(String.format("User removed with id=%s", userId));
        return ResponseEntity.noContent().build();
    }
}

class MeetingRequest {
    private String title;
    private String day;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalDateTime getDayAsDateTime() {
        LocalDate localDate = LocalDate.parse(this.getDay());
        return localDate.atStartOfDay();
    }
}
