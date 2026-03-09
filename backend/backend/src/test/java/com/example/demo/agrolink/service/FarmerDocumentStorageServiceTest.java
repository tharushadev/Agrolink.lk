package com.example.demo.agrolink.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FarmerDocumentStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void storesPdfDocuments() {
        FarmerDocumentStorageService service = new FarmerDocumentStorageService();
        ReflectionTestUtils.setField(service, "uploadDirectory", tempDir.toString());

        MockMultipartFile document = new MockMultipartFile(
                "documents",
                "nic-proof.pdf",
                "application/pdf",
                "pdf-content".getBytes());

        var storedDocument = service.storeDocument(document);

        assertThat(storedDocument.originalName()).isEqualTo("nic-proof.pdf");
        assertThat(Files.exists(Path.of(storedDocument.storedPath()))).isTrue();
    }
}