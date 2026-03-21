package com.agrolink.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AgreementService {

    // Generates the PDF and returns the file path where it was saved
    public String generateInvestmentAgreement(String investorName, String farmerName, String projectName, double amount, String investmentId) {

        // Define where to temporarily save the PDF on your server/laptop
        String fileName = "Agreement_" + investmentId + ".pdf";
        String filePath = System.getProperty("user.dir") + "/uploads/" + fileName;

        try {
            // Ensure directory exists
            new File(System.getProperty("user.dir") + "/uploads/").mkdirs();

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // --- HEADER ---
            document.add(new Paragraph("AGROLINK TRIPARTITE INVESTMENT AGREEMENT")
                    .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Transaction ID: " + investmentId)
                    .setFontSize(10).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setFontSize(10).setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

            // --- PARTIES ---
            document.add(new Paragraph("1. THE PARTIES").setBold().setFontSize(12));
            document.add(new Paragraph("This Agreement is entered into by and between:"));
            document.add(new Paragraph("Investor: " + investorName).setMarginLeft(20));
            document.add(new Paragraph("Farmer: " + farmerName).setMarginLeft(20));
            document.add(new Paragraph("Platform: AgroLink (Pvt) Ltd, Sri Lanka").setMarginLeft(20).setMarginBottom(15));

            // --- TERMS ---
            document.add(new Paragraph("2. FINANCIAL TERMS").setBold().setFontSize(12));
            document.add(new Paragraph("The Investor agrees to fund LKR " + amount + " towards the project titled '" + projectName + "'. The Farmer agrees to utilize these funds strictly for agricultural purposes related to this project."));

            // --- LEGAL CLAUSES (Sri Lankan Context) ---
            document.add(new Paragraph("3. FORCE MAJEURE & RISK").setBold().setFontSize(12).setMarginTop(15));
            document.add(new Paragraph("Agriculture inherently carries natural risks. In the event of an Act of God (severe drought, flooding, or natural disasters recognized by the Government of Sri Lanka), the Farmer is granted a grace period. AgroLink provides an AI Risk Assessment but holds zero financial liability for crop failure."));

            document.add(new Paragraph("4. BREACH OF CONTRACT").setBold().setFontSize(12).setMarginTop(15));
            document.add(new Paragraph("If the Farmer misuses the allocated funds or fails to cultivate the land without a valid Force Majeure reason, it constitutes a breach of contract under Sri Lankan law. AgroLink reserves the right to suspend the Farmer's account and provide KYC details to the Investor for legal recovery."));

            // --- SIGNATURES ---
            document.add(new Paragraph("5. DIGITAL AUTHORIZATION").setBold().setFontSize(12).setMarginTop(15));
            document.add(new Paragraph("By completing the payment via PayHere on the AgroLink platform, both the Investor and Farmer accept these terms electronically in accordance with the Electronic Transactions Act No. 19 of 2006 of Sri Lanka."));

            document.close();
            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}