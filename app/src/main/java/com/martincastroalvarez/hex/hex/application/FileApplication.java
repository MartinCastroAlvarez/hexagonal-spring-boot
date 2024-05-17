package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.File;
import com.martincastroalvarez.hex.hex.domain.ports.out.FileRepository;
import com.martincastroalvarez.hex.hex.domain.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Service
public class FileApplication extends HexagonalApplication implements FileService {
    @Value("${files.path}")
    private String filesPath;

    @Autowired
    private FileRepository fileRepository;

    private String generateFileHash(byte[] fileBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hashBytes = digest.digest(fileBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private Path generateFilePath(String fileHash) {
        return Path.of(filesPath, fileHash);
    }

    private String saveFileToStorage(byte[] fileBytes, String fileHash) throws IOException {
        Path filePath = generateFilePath(fileHash);
        Files.write(filePath, fileBytes);
        return filePath.toString();
    }

    @Override
    public File uploadFile(String fileName, InputStream fileStream) throws NoSuchAlgorithmException, IOException {
        logger.info(String.format("Uploading file. FileName: %s", fileName));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = fileStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] fileBytes = byteArrayOutputStream.toByteArray();
        String fileHash = generateFileHash(fileBytes);
        logger.info(String.format("Saving file to storage. FileHash: %s", fileHash));
        saveFileToStorage(fileBytes, fileHash);
        File file = new File();
        file.setFileName(fileName);
        file.setFileHash(fileHash);
        return fileRepository.save(file);
    }

    @Override
    public File downloadFile(Set<File> files, Integer fileId) throws HexagonalEntityNotFoundException, IOException {
        logger.info(String.format("Retrieving file. FileId: %d", fileId));
        File file = files.stream().filter(f -> f.getId().equals(fileId)).findFirst().orElseThrow(() -> new FileNotFoundException());
        Path filePath = generateFilePath(file.getFileHash());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        file.setOutputStream(outputStream);
        return file;
    }
}
