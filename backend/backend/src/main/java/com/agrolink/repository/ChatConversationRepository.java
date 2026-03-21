package com.agrolink.repository;

import com.agrolink.model.ChatConversation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatConversationRepository extends MongoRepository<ChatConversation, String> {
    List<ChatConversation> findByParticipantIdsContainsOrderByLastUpdatedDesc(String userId);

    Optional<ChatConversation> findByConversationKey(String conversationKey);
}
