package com.example.demo.agrolink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

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
}