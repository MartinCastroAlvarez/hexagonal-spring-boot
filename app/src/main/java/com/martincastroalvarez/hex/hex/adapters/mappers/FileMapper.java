package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.domain.models.File;
import com.martincastroalvarez.hex.hex.adapters.entities.FileEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.FileDTO;

public class FileMapper {
    public static File toFile(FileEntity fileEntity) {
        File file = new File();
        file.setId(fileEntity.getId());
        file.setFileName(fileEntity.getFileName());
        file.setFileHash(fileEntity.getFileHash());
        return file;
    }

    public static FileEntity toFileEntity(File file) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(file.getId());
        fileEntity.setFileName(file.getFileName());
        fileEntity.setFileHash(file.getFileHash());
        return fileEntity;
    }

    public static FileDTO toFileDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setFileName(file.getFileName());
        return fileDTO;
    }
}