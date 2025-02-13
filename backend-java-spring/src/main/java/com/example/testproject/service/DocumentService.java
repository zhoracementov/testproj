package com.example.testproject.service;

import com.example.testproject.model.Document;
import com.example.testproject.model.DocumentUpdateRequest;
import com.example.testproject.model.Tag;
import com.example.testproject.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DocumentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final List<String> SUPPORTED_FILE_TYPES = List.of("text/plain", "application/pdf");

    private final DocumentRepository documentRepository;
    private final TagRepository tagRepository;

    public DocumentService(DocumentRepository documentRepository, TagRepository tagRepository) {
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Document CreateDocument(String fileName, Long fileSize, String description, List<String> tags) {
        Document document = new Document();
        document.setFileName(fileName);
        document.setFileSize(fileSize / (1024.0d * 1024.0d));
        document.setDescription(description);
        document.setUploadedAt(LocalDateTime.now());

        if (tags != null && !tags.isEmpty()) {
            Set<Tag> tagSet = new HashSet<>();
            for (String tagName : tags) {
                if (tagName == null || tagName.trim().isEmpty()) {
                    continue;
                }
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                tagSet.add(tag);
                }

            document.setTags(tagSet);
        }
        
        return documentRepository.save(document);
    }

    @Transactional
    public Document UploadDocument(MultipartFile file, String description, List<String> tags) throws IOException {
        String fileType = file.getContentType();

        if (!SUPPORTED_FILE_TYPES.contains(fileType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Неподдерживаемый тип файла: " + fileType);
        }

        String fileName = file.getOriginalFilename();
        Document document = CreateDocument(fileName, file.getSize(), description, tags);

        Document res = documentRepository.save(document);

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
            Path newFilePath = Paths.get(uploadDir, document.getId().toString(), updateRequest.getFileName());
            Files.move(currentFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            document.setFileName(updateRequest.getFileName());
        }

        if (updateRequest.getDescription() != null && !updateRequest.getDescription().equals(document.getDescription())) {
            document.setDescription(updateRequest.getDescription());
        }

        if (updateRequest.getTags() != null) {
            document.setTags(updateRequest.getTags());
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

    public List<String> getSupportedFileTypes() {
        return SUPPORTED_FILE_TYPES;
    }
}
