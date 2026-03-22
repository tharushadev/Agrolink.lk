package com.example.demo.agrolink.service;

import com.example.demo.agrolink.model.FarmerProject;
import com.example.demo.agrolink.model.Investment;
import com.example.demo.agrolink.repository.FarmerProjectRepository;
import com.example.demo.agrolink.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerProjectRepository projectRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Investment purchaseUnits(String projectId, String investorId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        FarmerProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getUnitPrice() == null || project.getUnitPrice() <= 0) {
            throw new RuntimeException("Unit price is not configured for this project");
        }

        if (project.getTotalUnits() == null || project.getAvailableUnits() == null) {
            throw new RuntimeException("Unit configuration is incomplete for this project");
        }

        if (project.getStatus() != FarmerProject.ProjectStatus.APPROVED
                && project.getStatus() != FarmerProject.ProjectStatus.FUNDING) {
            throw new RuntimeException("Project is not open for unit purchase");
        }

        double investmentAmount = project.getUnitPrice() * quantity;

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(projectId));
        query.addCriteria(Criteria.where("availableUnits").gte(quantity));
        query.addCriteria(Criteria.where("status")
                .in(FarmerProject.ProjectStatus.APPROVED, FarmerProject.ProjectStatus.FUNDING));

        Update update = new Update()
                .inc("availableUnits", -quantity)
                .inc("currentFundingAmount", investmentAmount);

        FarmerProject updatedProject = mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                FarmerProject.class);

        if (updatedProject == null) {
            FarmerProject latestProject = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            if (latestProject.getAvailableUnits() == null || latestProject.getAvailableUnits() < quantity) {
                throw new RuntimeException("Not enough available units");
            }

            throw new RuntimeException("Project is not open for unit purchase");
        }

        if (updatedProject.getMinimumFundingToStart() != null
                && updatedProject.getCurrentFundingAmount() != null
                && updatedProject.getCurrentFundingAmount() >= updatedProject.getMinimumFundingToStart()) {
            if (updatedProject.getStatus() != FarmerProject.ProjectStatus.IN_PROGRESS) {
                updatedProject.setStatus(FarmerProject.ProjectStatus.IN_PROGRESS);
                projectRepository.save(updatedProject);
            }
        } else if (updatedProject.getStatus() == FarmerProject.ProjectStatus.APPROVED) {
            updatedProject.setStatus(FarmerProject.ProjectStatus.FUNDING);
            projectRepository.save(updatedProject);
        }

        Investment investment = Investment.builder()
                .projectId(projectId)
                .investorId(investorId)
                .quantity(quantity)
                .unitPrice(updatedProject.getUnitPrice())
                .amount(investmentAmount)
                .totalUnits(updatedProject.getTotalUnits())
                .availableUnits(updatedProject.getAvailableUnits())
                .build();

        return investmentRepository.save(investment);
    }
}