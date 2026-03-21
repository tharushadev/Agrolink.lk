package com.agrolink.util;

public class DistrictMapper {

    public static String getDistrict(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return "SRI LANKA"; // Fallback to national average
        }

        String loc = locationName.toLowerCase();

        // 1. COLOMBO
        if (loc.contains("colombo") || loc.contains("dehiwala") || loc.contains("mount lavinia") || loc.contains("moratuwa") || loc.contains("padukka") || loc.contains("homagama")) {
            return "COLOMBO";
        }
        // 2. GAMPAHA
        if (loc.contains("gampaha") || loc.contains("negombo") || loc.contains("kelaniya") || loc.contains("wattala") || loc.contains("minuwangoda")) {
            return "GAMPAHA";
        }
        // 3. KANDY
        if (loc.contains("kandy") || loc.contains("peradeniya") || loc.contains("katugastota") || loc.contains("gampola") || loc.contains("nawalapitiya")) {
            return "KANDY";
        }
        // 4. ANURADHAPURA
        if (loc.contains("anuradhapura") || loc.contains("kekirawa") || loc.contains("tambuttegama") || loc.contains("galenbindunuwewa")) {
            return "ANURADHAPURA";
        }
        // 5. GALLE
        if (loc.contains("galle") || loc.contains("hikkaduwa") || loc.contains("karapitiya") || loc.contains("elpitiya")) {
            return "GALLE";
        }
        // 6. MATARA
        if (loc.contains("matara") || loc.contains("weligama") || loc.contains("dikwella") || loc.contains("hakmana")) {
            return "MATARA";
        }
        // 7. HAMBANTOTA
        if (loc.contains("hambantota") || loc.contains("tangalle") || loc.contains("tissamaharama") || loc.contains("beliatta")) {
            return "HAMBANTOTA";
        }
        // 8. KURUNEGALA
        if (loc.contains("kurunegala") || loc.contains("kuliyapitiya") || loc.contains("polgahawela") || loc.contains("pannala")) {
            return "KURUNEGALA";
        }

        // Add more towns here as your MVP grows!

        // If the town exactly matches a district name already, just return it uppercase
        String[] districts = {"AMPARA", "BADULLA", "BATTICALOA", "JAFFNA", "KALUTARA", "KEGALLE", "KILINOCHCHI", "MANNAR", "MATALE", "MONARAGALA", "MULLATIVU", "NUWARA ELIYA", "POLONNARUWA", "PUTTALAM", "RATNAPURA", "TRINCOMALEE", "VAVUNIYA"};
        for (String d : districts) {
            if (loc.contains(d.toLowerCase())) {
                return d;
            }
        }

        // PERFECT FALLBACK: If the town isn't in the list, use the national average from your JSON!
        return "SRI LANKA";
    }
}