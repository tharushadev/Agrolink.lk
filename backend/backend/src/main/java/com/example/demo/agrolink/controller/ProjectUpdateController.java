package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.model.ProjectUpdate;
import com.example.demo.agrolink.repository.ProjectUpdateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/updates")
@CrossOrigin(origins = "*")
public class ProjectUpdateController {

    private final ProjectUpdateRepository updateRepository;

    public ProjectUpdateController(ProjectUpdateRepository updateRepository) {
        this.updateRepository = updateRepository;
    }

    // 1. Farmer posts an update (Defaults to PENDING_VERIFICATION)
    @PostMapping("/post")
    public ResponseEntity<ProjectUpdate> postUpdate(@RequestBody ProjectUpdate update) {
        update.setStatus(ProjectUpdate.UpdateStatus.PENDING_VERIFICATION);
        return ResponseEntity.ok(updateRepository.save(update));
    }

    // 2. Admin gets all pending updates
    @GetMapping("/pending")
    public ResponseEntity<List<ProjectUpdate>> getPendingUpdates() {
        return ResponseEntity.ok(updateRepository.findByStatus(ProjectUpdate.UpdateStatus.PENDING_VERIFICATION));
    }

    // 3. Admin verifies an update
    @PostMapping("/{updateId}/verify")
    public ResponseEntity<?> verifyUpdate(@PathVariable String updateId) {
        Optional<ProjectUpdate> optUpdate = updateRepository.findById(updateId);
        if (optUpdate.isPresent()) {
            ProjectUpdate update = optUpdate.get();
            update.setStatus(ProjectUpdate.UpdateStatus.VERIFIED);
            updateRepository.save(update);
            return ResponseEntity.ok(update);
        }
        return ResponseEntity.badRequest().body("Update not found");
    }

    // 4. Investor gets verified updates for a project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectUpdate>> getProjectUpdates(@PathVariable String projectId) {
        List<ProjectUpdate> allUpdates = updateRepository.findByProjectIdOrderByTimestampDesc(projectId);
        // Filter only verified updates for the public feed
        List<ProjectUpdate> verifiedUpdates = allUpdates.stream()
                .filter(u -> u.getStatus() == ProjectUpdate.UpdateStatus.VERIFIED)
                .toList();
        return ResponseEntity.ok(verifiedUpdates);
    }
}
