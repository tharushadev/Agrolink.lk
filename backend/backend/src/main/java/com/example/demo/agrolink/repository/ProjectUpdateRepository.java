package com.example.demo.agrolink.repository;

import com.example.demo.agrolink.model.ProjectUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProjectUpdateRepository extends MongoRepository<ProjectUpdate, String> {
    List<ProjectUpdate> findByProjectIdOrderByTimestampDesc(String projectId);

    List<ProjectUpdate> findByStatus(ProjectUpdate.UpdateStatus status);

    long countByProjectIdAndStatus(String projectId, ProjectUpdate.UpdateStatus status);
}
