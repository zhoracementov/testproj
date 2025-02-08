package com.example.testproject.service;

import com.example.testproject.repository.Document;
import com.example.testproject.repository.DocumentRepository;
import com.example.testproject.repository.DocumentUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document UploadDocument(MultipartFile file, String description) throws IOException {
        String fileName = new String(Objects.requireNonNull(file.getOriginalFilename()).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        Document document = new Document();
        document.setFileName(fileName);
        document.setFileSize((long)Math.ceil(file.getSize() / (1024.0 * 1024.0)));
        document.setDescription(description);
        document.setUploadedAt(LocalDateTime.now());

        Document res = documentRepository.save(document);

        //uploads/
        //├── 1/                # Папка для документа с id = 1
        //│   └── document.pdf  # Файл с исходным именем
        //├── 2/                # Папка для документа с id = 2
        //│   └── report.docx   # Файл с исходным именем
        //└── ...

        String subFolderName = document.getId().toString();
        Path subFolderPath = Paths.get(uploadDir, subFolderName);
        Files.createDirectories(subFolderPath);

        Path filePath = subFolderPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return res;
    }

    public List<Document> GetAllDocuments() {
        return documentRepository.findAll();
    }

    public Document GetDocumentById(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public Resource DownloadDocument(Long id) throws FileNotFoundException, MalformedURLException {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Path filePath = Paths.get(uploadDir, document.getId().toString(), document.getFileName());
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("File not found: " + filePath);
        }
    }

    @Transactional
    public Document UpdateDocument(Long id, DocumentUpdateRequest updateRequest) throws IOException {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (updateRequest.getFileName() != null && !updateRequest.getFileName().equals(document.getFileName()))
        {
            Path currentFilePath = Paths.get(uploadDir, document.getId().toString(), document.getFileName());
            Path newFilePath = currentFilePath.resolveSibling(updateRequest.getFileName());
            Files.move(currentFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            document.setFileName(updateRequest.getFileName());
        }

        if (updateRequest.getDescription() != null && !updateRequest.getDescription().equals(document.getDescription())) {
            document.setDescription(updateRequest.getDescription());
        }

        return documentRepository.save(document);
    }

    public void DeleteDocument(Long id) throws IOException {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Path folderPath = Paths.get(uploadDir, document.getId().toString());
        if (Files.exists(folderPath)) {
            Files.walk(folderPath)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete file: " + path, e);
                        }
                    });
        }

        documentRepository.deleteById(id);
    }
}
