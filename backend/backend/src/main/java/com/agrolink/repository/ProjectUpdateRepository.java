package com.agrolink.repository;

import com.agrolink.model.ProjectUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProjectUpdateRepository extends MongoRepository<ProjectUpdate, String> {
    List<ProjectUpdate> findByProjectIdOrderByTimestampDesc(String projectId);
    List<ProjectUpdate> findByStatus(ProjectUpdate.UpdateStatus status);
}