package com.example.testproject.repository;

import java.util.List;

public class DocumentDTO {
    private Long id;
    private String fileName;
    private String description;
    private List<String> tags;

    public DocumentDTO(Document document) {
        this.id = document.getId();
        this.fileName = document.getFileName();
        this.description = document.getDescription();
        this.tags = document.getTags().stream().map(Tag::getName).toList();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
