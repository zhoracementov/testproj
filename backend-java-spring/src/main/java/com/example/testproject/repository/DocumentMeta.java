package com.example.testproject.repository;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "documentmeta")
public class DocumentMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "filename")
    private String fileName;
    @Column(name = "filesize")
    private Long fileSize; //mb
    @Column(name = "description")
    private String description;
    @Column(name = "uploadedat")
    private LocalDate uploadedAt;

    public DocumentMeta(Long id, String fileName, Long fileSize, String description, LocalDate uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.description = description;
        this.uploadedAt = uploadedAt;
    }

    public DocumentMeta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDate uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
