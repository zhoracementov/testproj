package com.example.testproject.contoller;

import com.example.testproject.repository.DocumentMeta;
import com.example.testproject.repository.DocumentMetaUpdateRequest;
import com.example.testproject.service.DocumentMetaService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "documents")
public class DocumentMetaController {

    private final DocumentMetaService documentMetaService;

    public  DocumentMetaController(DocumentMetaService documentMetaService) {
        this.documentMetaService = documentMetaService;
    }

    //POST /documents/upload — загрузить документ (multipart/form-data).
    @PostMapping("/upload")
    public ResponseEntity<DocumentMeta> UploadDocument(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("description") String description) throws IOException
    {
        DocumentMeta document = documentMetaService.UploadDocument(file, description);
        return ResponseEntity.ok(document);
    }

    //GET /documents — получить список всех документов с метаданными.
    @GetMapping
    public ResponseEntity<List<DocumentMeta>> GetAllDocuments() {
        List<DocumentMeta> documents = documentMetaService.GetAllDocuments();
        return ResponseEntity.ok(documents);
    }

    //GET /documents/{id} — получить информацию о документе по ID.
    @GetMapping("/{id}")
    public DocumentMeta GetDocumentById(@PathVariable Long id){
        return documentMetaService.GetDocumentById(id);
    }

    //GET /documents/{id}/download — скачать документ по ID.
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> DownloadDocument(@PathVariable Long id) throws IOException {
        Resource resource = documentMetaService.DownloadDocument(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //PUT /documents/{id} — обновить метаданные документа (название, описание, теги).
    @PutMapping("/{id}")
    public ResponseEntity<DocumentMeta> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentMetaUpdateRequest updateRequest
    ) throws IOException {
        DocumentMeta document = documentMetaService.UpdateDocument(id, updateRequest);
        return ResponseEntity.ok(document);
    }

    //DELETE /documents/{id} — удалить документ.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteDocument(@PathVariable Long id) throws IOException {
        documentMetaService.DeleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}