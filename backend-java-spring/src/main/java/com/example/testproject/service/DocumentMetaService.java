package com.example.testproject.service;

import com.example.testproject.repository.DocumentMeta;
import com.example.testproject.repository.DocumentMetaRepository;
import com.example.testproject.repository.DocumentMetaUpdateRequest;
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
public class DocumentMetaService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final DocumentMetaRepository documentMetaRepository;

    public DocumentMetaService(DocumentMetaRepository documentMetaRepository) {
        this.documentMetaRepository = documentMetaRepository;
    }

    public DocumentMeta UploadDocument(MultipartFile file, String description) throws IOException {
        String fileName = new String(Objects.requireNonNull(file.getOriginalFilename()).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        DocumentMeta document = new DocumentMeta();
        document.setFileName(fileName);
        document.setFileSize((long)Math.ceil(file.getSize() / (1024.0 * 1024.0)));
        document.setDescription(description);
        document.setUploadedAt(LocalDateTime.now());

        DocumentMeta res = documentMetaRepository.save(document);

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

    public List<DocumentMeta> GetAllDocuments() {
        return documentMetaRepository.findAll();
    }

    public DocumentMeta GetDocumentById(Long id) {
        return documentMetaRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public Resource DownloadDocument(Long id) throws FileNotFoundException, MalformedURLException {
        DocumentMeta document = documentMetaRepository.findById(id)
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
    public DocumentMeta UpdateDocument(Long id, DocumentMetaUpdateRequest updateRequest) throws IOException {
        DocumentMeta document = documentMetaRepository.findById(id)
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

        return documentMetaRepository.save(document);
    }

    public void DeleteDocument(Long id) throws IOException {
        DocumentMeta document = documentMetaRepository.findById(id)
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

        documentMetaRepository.deleteById(id);
    }
}
