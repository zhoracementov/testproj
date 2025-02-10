package com.example.testproject.repository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "name", nullable = false, unique = true, length = 15)
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "tags")
    private Set<Document> documents = new HashSet<>();

    public Tag(String name, Set<Document> documents) {
        this.name = name;
        this.documents = documents;
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}