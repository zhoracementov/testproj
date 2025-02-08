package com.example.testproject.service;

import com.example.testproject.contoller.DocumentMetaController;
import com.example.testproject.repository.DocumentMeta;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentMetaService {
    public List<DocumentMeta> TestMethod() {
        return List.of(new DocumentMeta(1L, "bible", 123L, "the important book", LocalDate.now()));
    }
}
