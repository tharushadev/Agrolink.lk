package com.example.demo.agrolink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.file.Path;

@Service
public class FarmerDocumentStorageService {

	@Value("${app.uploads.farmer-documents-dir}")
	private String uploadDirectory;
    
	private Path getUploadDirectoryPath() {
		return Path.of(uploadDirectory).toAbsolutePath().normalize();
	}
}