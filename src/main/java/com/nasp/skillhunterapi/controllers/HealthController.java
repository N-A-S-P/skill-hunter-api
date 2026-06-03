package com.nasp.skillhunterapi.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
class HealthController {

    @GetMapping
    public String healthCheck() {
        return "hello";
    }
}
