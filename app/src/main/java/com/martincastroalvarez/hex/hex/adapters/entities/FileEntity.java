package com.martincastroalvarez.hex.hex.adapters.entities;

import jakarta.persistence.*;
import jakarta.annotation.*;

@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_generator")
    @SequenceGenerator(name="file_generator", sequenceName = "file_id_seq", allocationSize=1)
    private Integer id;

    @Nonnull
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Nonnull
    @Column(name = "file_hash", nullable = false, length = 255)
    private String fileHash;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
