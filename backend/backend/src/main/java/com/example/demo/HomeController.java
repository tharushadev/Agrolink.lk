package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Agrolink backend is running",
                "predictEndpoint", "/api/risk/predict?farmerId=123&district=KANDY&season=Yala"
        );
    }
}