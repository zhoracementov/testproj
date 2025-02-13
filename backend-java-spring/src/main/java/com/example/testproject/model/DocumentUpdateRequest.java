package com.example.testproject.model;

import java.util.Set;

public class DocumentUpdateRequest {
    private String fileName;
    private String description;
    private Set<Tag> tags;

    public DocumentUpdateRequest(String fileName, String description, Set<Tag> tags) {
        this.fileName = fileName;
        this.description = description;
        this.tags = tags;
    }

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
