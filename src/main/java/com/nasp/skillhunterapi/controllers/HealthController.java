package com.nasp.skillhunterapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Health")
@RequestMapping(value =  "/api/health")
class HealthController {

    @Operation(summary = "Check API health")
    @GetMapping
    public String healthCheck() {
        return "hello";
    }
}
