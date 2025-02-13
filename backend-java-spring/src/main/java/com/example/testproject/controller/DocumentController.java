package com.example.testproject.controller;

import com.example.testproject.model.Document;
import com.example.testproject.model.DocumentDTO;
import com.example.testproject.model.DocumentUpdateRequest;
import com.example.testproject.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    //POST /documents/upload — загрузить документ (multipart/form-data).
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam(value = "tags", required = false) List<String> tags  // Добавляем параметр для тегов
    ) {
        try {
            Document document = documentService.UploadDocument(file, description, tags);
            return ResponseEntity.ok(document);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload error");
        }
    }

    //GET /documents — получить список всех документов с метаданными.
    @GetMapping
    public ResponseEntity<List<Document>> GetAllDocuments() {
        List<Document> documents = documentService.GetAllDocuments();
        return ResponseEntity.ok(documents);
    }

    //GET /documents/{id} — получить информацию о документе по ID.
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> GetDocumentById(@PathVariable Long id) {
        Document document = documentService.GetDocumentById(id);
        return ResponseEntity.ok(new DocumentDTO(document));
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