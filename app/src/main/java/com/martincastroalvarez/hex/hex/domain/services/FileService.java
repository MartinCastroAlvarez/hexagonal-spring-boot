package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.File;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface FileService {
    File uploadFile(String fileName, InputStream fileStream) throws NoSuchAlgorithmException, IOException;
    File downloadFile(Set<File> files, Integer fileId) throws HexagonalEntityNotFoundException, IOException;
}