package com.example.testproject.service;

import com.example.testproject.repository.DocumentMeta;
import com.example.testproject.repository.DocumentMetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentMetaService {
    private final DocumentMetaRepository documentMetaRepository;

    public DocumentMetaService(DocumentMetaRepository documentMetaRepository) {
        this.documentMetaRepository = documentMetaRepository;
    }

    public List<DocumentMeta> TestMethod() {
        return documentMetaRepository.findAll();
    }
}
