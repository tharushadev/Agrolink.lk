package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.dto.StoredFarmerDocument;
import com.example.demo.agrolink.model.User;
import com.example.demo.agrolink.repository.UserRepository;
import com.example.demo.agrolink.service.FarmerDocumentStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FarmerDocumentStorageService farmerDocumentStorageService;

    @Test
    void registersFarmerWithPdfDocuments() throws Exception {
        MockMultipartFile document = new MockMultipartFile(
                "documents",
                "nic-proof.pdf",
                "application/pdf",
                "pdf-content".getBytes());

        given(userRepository.findByUsername("farmer@example.com")).willReturn(Optional.empty());
        given(farmerDocumentStorageService.storeDocuments(any())).willReturn(
                List.of(new StoredFarmerDocument("uploads/farmer-documents/file.pdf", "nic-proof.pdf")));
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("farmer-1");
            return user;
        });

        mockMvc.perform(multipart("/api/auth/register/farmer")
                        .file(document)
                        .param("username", "farmer@example.com")
                        .param("password", "secret")
                        .param("nic", "123456789V"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Farmer registered successfully!"))
                .andExpect(jsonPath("$.role").value("FARMER"))
                .andExpect(jsonPath("$.username").value("farmer@example.com"))
                .andExpect(jsonPath("$.documentCount").value(1));
    }

    @Test
    void rejectsDuplicateUsernameForFarmerSignup() throws Exception {
        MockMultipartFile document = new MockMultipartFile(
                "documents",
                "nic-proof.pdf",
                "application/pdf",
                "pdf-content".getBytes());

        given(userRepository.findByUsername("farmer@example.com")).willReturn(Optional.of(new User()));

        mockMvc.perform(multipart("/api/auth/register/farmer")
                        .file(document)
                        .param("username", "farmer@example.com")
                        .param("password", "secret")
                        .param("nic", "123456789V"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken!"));
    }
}