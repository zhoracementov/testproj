package com.example.testproject.repository;

public class DocumentMetaUpdateRequest {
    private String fileName;
    private String description;
    private String tags;

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
