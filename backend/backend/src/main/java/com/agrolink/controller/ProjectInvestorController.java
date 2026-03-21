package com.agrolink.controller;

import com.agrolink.model.Investment;
import com.agrolink.model.User;
import com.agrolink.repository.InvestmentRepository;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin(origins = "*")
public class ProjectInvestorController {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/project/{projectId}/investors")
    public ResponseEntity<List<Map<String, Object>>> getProjectInvestors(@PathVariable String projectId) {
        // 1. Find all investments for this specific project
        List<Investment> investments = investmentRepository.findByProjectId(projectId);
        List<Map<String, Object>> investorList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        // 2. Loop through them and find the Investor's Name from the Users table
        for (Investment inv : investments) {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", inv.getAmount());

            // Format date if you have one, otherwise default to "Recent"
            map.put("date", inv.getInvestmentDate() != null ? sdf.format(inv.getInvestmentDate()) : "Recent Investment");

            // Look up the user to get their first and last name
            Optional<User> userOpt = userRepository.findById(inv.getInvestorId());
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                map.put("name", u.getFirstName() + " " + u.getLastName());
            } else {
                map.put("name", "Anonymous Investor");
            }

            investorList.add(map);
        }

        return ResponseEntity.ok(investorList);
    }
}