package com.example.testproject.repository;

public class DocumentUpdateRequest {
    private String fileName;
    private String description;

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
