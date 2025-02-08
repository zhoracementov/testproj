package com.example.testproject.contoller;

import com.example.testproject.repository.DocumentMeta;
import com.example.testproject.service.DocumentMetaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocumentMetaController {

    private final DocumentMetaService documentMetaService;

    public  DocumentMetaController(DocumentMetaService documentMetaService) {
        this.documentMetaService = documentMetaService;
    }

    @GetMapping
    public List<DocumentMeta> TestHelloWorld() {
        return documentMetaService.TestMethod();
    }
}
