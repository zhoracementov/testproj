package com.example.testproject.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "filename", nullable = false)
    private String fileName;
    @Column(name = "filesize", nullable = false)
    private Long fileSize; //mb
    @Column(name = "description")
    private String description;
    @Column(name = "uploadedat", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadedAt;

    public Document(Long id, String fileName, Long fileSize, String description, LocalDateTime uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.description = description;
        this.uploadedAt = uploadedAt;
    }

    public Document() {
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

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String GetFilePath() {
        return fileName + id.toString();
    }
}
