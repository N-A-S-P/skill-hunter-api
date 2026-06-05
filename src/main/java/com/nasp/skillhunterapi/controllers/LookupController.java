package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.LookupDto;
import com.nasp.skillhunterapi.service.LookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/lookup")
@RequiredArgsConstructor
class LookupController {
    private final LookupService service;

    @GetMapping("/{lookupType}")
    public List<LookupDto> getLookup(
            @PathVariable
            String lookupType
    ) {
        return service.getLookup(lookupType);
    }

}
