package com.agrolink.repository;

import com.agrolink.model.ProjectChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProjectChatRepository extends MongoRepository<ProjectChat, String> {
    // Finds all messages for a project and sorts them from oldest to newest
    List<ProjectChat> findByProjectIdOrderByTimestampAsc(String projectId);
}