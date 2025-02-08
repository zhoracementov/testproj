package com.example.testproject.controller;

import com.example.testproject.repository.Document;
import com.example.testproject.repository.DocumentUpdateRequest;
import com.example.testproject.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(path = "documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    //POST /documents/upload — загрузить документ (multipart/form-data).
    @PostMapping("/upload")
    public ResponseEntity<Document> UploadDocument(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("description") String description) throws IOException
    {
        Document document = documentService.UploadDocument(file, description);
        return ResponseEntity.ok(document);
    }

    //GET /documents — получить список всех документов с метаданными.
    @GetMapping
    public ResponseEntity<List<Document>> GetAllDocuments() {
        List<Document> documents = documentService.GetAllDocuments();
        return ResponseEntity.ok(documents);
    }

    //GET /documents/{id} — получить информацию о документе по ID.
    @GetMapping("/{id}")
    public Document GetDocumentById(@PathVariable Long id){
        return documentService.GetDocumentById(id);
    }

    //GET /documents/{id}/download — скачать документ по ID.
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> DownloadDocument(@PathVariable Long id) throws IOException {
        Resource resource = documentService.DownloadDocument(id);

        Path fileName = Paths.get(documentService.GetDocumentById(id).getFileName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(fileName))
                .body(resource);
    }

    //PUT /documents/{id} — обновить метаданные документа (название, описание, теги).
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentUpdateRequest updateRequest
    ) throws IOException {
        Document document = documentService.UpdateDocument(id, updateRequest);
        return ResponseEntity.ok(document);
    }

    //DELETE /documents/{id} — удалить документ.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteDocument(@PathVariable Long id) throws IOException {
        documentService.DeleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}