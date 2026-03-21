package com.agrolink.controller;

import com.agrolink.model.ProjectChat;
import com.agrolink.repository.ProjectChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ProjectChatController {

    @Autowired
    private ProjectChatRepository chatRepository;

    // 1. Get all messages for a project
    @GetMapping("/{projectId}")
    public ResponseEntity<List<ProjectChat>> getProjectChats(@PathVariable String projectId) {
        List<ProjectChat> messages = chatRepository.findByProjectIdOrderByTimestampAsc(projectId);
        return ResponseEntity.ok(messages);
    }

    // 2. Send a new message
    @PostMapping("/send")
    public ResponseEntity<ProjectChat> sendMessage(@RequestBody ProjectChat chat) {
        // Timestamp is automatically generated in the model!
        ProjectChat savedChat = chatRepository.save(chat);
        return ResponseEntity.ok(savedChat);
    }
}