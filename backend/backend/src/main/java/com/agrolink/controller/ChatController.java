package com.agrolink.controller;

import com.agrolink.model.ChatConversation;
import com.agrolink.model.ChatMessage;
import com.agrolink.repository.ChatConversationRepository;
import com.agrolink.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatConversationRepository conversationRepository;

    @Autowired
    private ChatMessageRepository messageRepository;

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<?> listConversations(@PathVariable String userId) {
        return ResponseEntity.ok(conversationRepository.findByParticipantIdsContainsOrderByLastUpdatedDesc(userId));
    }

    @PostMapping("/conversations")
    public ResponseEntity<?> createConversation(@RequestBody Map<String, Object> request) {
        String projectId = request.get("projectId") == null ? null : request.get("projectId").toString();
        @SuppressWarnings("unchecked")
        List<String> participantIds = (List<String>) request.get("participantIds");

        if (participantIds == null || participantIds.size() < 2) {
            return ResponseEntity.badRequest().body("participantIds must include at least 2 users");
        }

        String key = buildConversationKey(projectId, participantIds);
        Optional<ChatConversation> existing = conversationRepository.findByConversationKey(key);
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        ChatConversation conversation = new ChatConversation();
        conversation.setProjectId(projectId);
        conversation.setParticipantIds(new ArrayList<>(new HashSet<>(participantIds)));
        conversation.setConversationKey(key);
        conversation.setLastUpdated(new Date());

        return ResponseEntity.ok(conversationRepository.save(conversation));
    }

    @PostMapping("/conversations/project")
    public ResponseEntity<?> createProjectConversation(@RequestBody Map<String, String> request) {
        String projectId = request.get("projectId");
        String farmerId = request.get("farmerId");
        String investorId = request.get("investorId");

        if (projectId == null || projectId.isBlank()) return ResponseEntity.badRequest().body("projectId is required");
        if (farmerId == null || farmerId.isBlank()) return ResponseEntity.badRequest().body("farmerId is required");
        if (investorId == null || investorId.isBlank()) return ResponseEntity.badRequest().body("investorId is required");

        List<String> participantIds = List.of(farmerId, investorId);
        String key = buildConversationKey(projectId, participantIds);

        Optional<ChatConversation> existing = conversationRepository.findByConversationKey(key);
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        ChatConversation conversation = new ChatConversation();
        conversation.setProjectId(projectId);
        conversation.setParticipantIds(new ArrayList<>(participantIds));
        conversation.setConversationKey(key);
        conversation.setLastUpdated(new Date());

        return ResponseEntity.ok(conversationRepository.save(conversation));
    }

    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<?> listMessages(@PathVariable String conversationId) {
        return ResponseEntity.ok(messageRepository.findByConversationIdOrderByTimestampAsc(conversationId));
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        String senderId = request.get("senderId");
        String text = request.get("text");
        String mediaUrl = request.get("mediaUrl");

        if (conversationId == null || conversationId.isBlank()) {
            return ResponseEntity.badRequest().body("conversationId is required");
        }
        if (senderId == null || senderId.isBlank()) {
            return ResponseEntity.badRequest().body("senderId is required");
        }
        boolean hasText = text != null && !text.isBlank();
        boolean hasMedia = mediaUrl != null && !mediaUrl.isBlank();
        if (!hasText && !hasMedia) {
            return ResponseEntity.badRequest().body("text or mediaUrl is required");
        }

        Optional<ChatConversation> convOpt = conversationRepository.findById(conversationId);
        if (convOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Conversation not found");
        }

        ChatMessage message = new ChatMessage();
        message.setConversationId(conversationId);
        message.setSenderId(senderId);
        message.setText(text);
        message.setMediaUrl(mediaUrl);
        ChatMessage saved = messageRepository.save(message);

        ChatConversation conv = convOpt.get();
        conv.setLastUpdated(new Date());
        if (hasText) {
            conv.setLastMessage(text.length() > 80 ? text.substring(0, 80) : text);
        } else {
            conv.setLastMessage("[media]");
        }
        conversationRepository.save(conv);

        return ResponseEntity.ok(saved);
    }

    private String buildConversationKey(String projectId, List<String> participantIds) {
        List<String> normalized = participantIds.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .sorted()
                .toList();

        String prefix = projectId == null || projectId.isBlank() ? "global" : projectId.trim();
        return prefix + ":" + String.join(",", normalized);
    }
}
