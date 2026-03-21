package com.agrolink.controller;

import com.agrolink.model.ProjectUpdate;
import com.agrolink.repository.ProjectUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/updates")
@CrossOrigin(origins = "*")
public class ProjectUpdateController {

    @Autowired
    private ProjectUpdateRepository updateRepository;

    // 1. Farmer posts an update (Defaults to PENDING_VERIFICATION)
    @PostMapping("/post")
    public ResponseEntity<ProjectUpdate> postUpdate(@RequestBody ProjectUpdate update) {
        update.setStatus(ProjectUpdate.UpdateStatus.PENDING_VERIFICATION);
        return ResponseEntity.ok(updateRepository.save(update));
    }

    // 2. Admin gets all pending updates
    @GetMapping("/admin/pending")
    public ResponseEntity<List<ProjectUpdate>> getPendingUpdates() {
        return ResponseEntity.ok(updateRepository.findByStatus(ProjectUpdate.UpdateStatus.PENDING_VERIFICATION));
    }

    // 3. Admin verifies an update
    @PostMapping("/admin/{updateId}/verify")
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

    // 4. Investor gets verified updates for a specific project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectUpdate>> getProjectUpdates(@PathVariable String projectId) {
        List<ProjectUpdate> allUpdates = updateRepository.findByProjectIdOrderByTimestampDesc(projectId);
        // Filter only verified updates for the public feed
        List<ProjectUpdate> verifiedUpdates = allUpdates.stream()
                .filter(u -> u.getStatus() == ProjectUpdate.UpdateStatus.VERIFIED)
                .toList();
        return ResponseEntity.ok(verifiedUpdates);
    }

        // 5. Farmer Manage Project: fetch all updates (pending + verified)
        @GetMapping("/project/{projectId}/manage")
        public ResponseEntity<?> getProjectUpdatesForManagement(
            @PathVariable String projectId,
            @RequestParam String farmerId
        ) {
        List<ProjectUpdate> allUpdates = updateRepository.findByProjectIdOrderByTimestampDesc(projectId);
        // Only allow the farmer who created the update stream to see pending updates
        List<ProjectUpdate> visible = allUpdates.stream()
            .filter(u -> farmerId.equals(u.getFarmerId()))
            .toList();
        return ResponseEntity.ok(Map.of(
            "projectId", projectId,
            "updates", visible
        ));
        }
}