package com.example.demo.agrolink.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled("Legacy test suite: active backend APIs are under com.agrolink")
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

    @Test
    void rejectsNonPdfDocuments() {
        FarmerDocumentStorageService service = new FarmerDocumentStorageService();
        ReflectionTestUtils.setField(service, "uploadDirectory", tempDir.toString());

        MockMultipartFile document = new MockMultipartFile(
                "documents",
                "photo.png",
                "image/png",
                "not-a-pdf".getBytes());

        assertThatThrownBy(() -> service.storeDocument(document))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Only non-empty PDF files are allowed");
    }
}
