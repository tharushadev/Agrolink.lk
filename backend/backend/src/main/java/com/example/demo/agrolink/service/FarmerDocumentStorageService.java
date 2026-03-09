package com.example.demo.agrolink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FarmerDocumentStorageService {

	@Value("${app.uploads.farmer-documents-dir}")
	private String uploadDirectory;
}