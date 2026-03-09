package com.example.demo.agrolink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class FarmerDocumentStorageService {

	@Value("${app.uploads.farmer-documents-dir}")
	private String uploadDirectory;
    
	private Path getUploadDirectoryPath() {
		return Path.of(uploadDirectory).toAbsolutePath().normalize();
	}

	private Path ensureUploadDirectoryExists() throws IOException {
		Path directoryPath = getUploadDirectoryPath();
		Files.createDirectories(directoryPath);
		return directoryPath;
	}

	private void validatePdfFile(MultipartFile document) {
		String originalName = document.getOriginalFilename();
		boolean hasPdfExtension = originalName != null && originalName.toLowerCase().endsWith(".pdf");
		boolean hasPdfContentType = "application/pdf".equalsIgnoreCase(document.getContentType());

		if (document.isEmpty() || (!hasPdfExtension && !hasPdfContentType)) {
			throw new ResponseStatusException(BAD_REQUEST, "Only non-empty PDF files are allowed for farmer signup.");
		}
	}

	private String sanitizeFilename(String originalName) {
		if (originalName == null || originalName.isBlank()) {
			return "document.pdf";
		}

		return Path.of(originalName)
				.getFileName()
				.toString()
				.replaceAll("[^a-zA-Z0-9._-]", "_");
	}

	private String buildStoredFilename(String originalName) {
		return UUID.randomUUID() + "-" + sanitizeFilename(originalName);
	}
}