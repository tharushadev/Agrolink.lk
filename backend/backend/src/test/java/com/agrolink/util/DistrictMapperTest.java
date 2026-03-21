package com.agrolink.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistrictMapperTest {

    // TC-U01: Valid Sub-Town Mapping
    @Test
    public void testKnownTownMapping() {
        String result = DistrictMapper.getDistrict("Dehiwala-Mount Lavinia");
        assertEquals("COLOMBO", result, "Should map a known local town to its parent district.");
    }

    // TC-U02: Unknown Location Fallback
    @Test
    public void testUnknownTownFallback() {
        String result = DistrictMapper.getDistrict("Unknown Deep Jungle Village");
        assertEquals("SRI LANKA", result, "Should safely fall back to the national average.");
    }

    // TC-U03: Exact District Match
    @Test
    public void testExactDistrictMatch() {
        String result = DistrictMapper.getDistrict("kandy");
        assertEquals("KANDY", result, "Should return the exact district in uppercase.");
    }

    // TC-U04: Null Value Handling
    @Test
    public void testNullLocationHandling() {
        String result = DistrictMapper.getDistrict(null);
        assertEquals("SRI LANKA", result, "Null inputs must not throw a NullPointerException.");
    }
}