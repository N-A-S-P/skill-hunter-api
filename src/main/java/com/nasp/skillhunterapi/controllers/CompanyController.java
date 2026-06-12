package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.Company.*;
import com.nasp.skillhunterapi.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Company", description = "Endpoints to interact with Company")
@RequestMapping(value = "/api/companies", produces = MediaType.APPLICATION_JSON_VALUE)
class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "Get all companies for the current user")
    @GetMapping
    public List<CompanyListItemResponse> getAllCompanies() {
        return companyService.getCompanies();
    }

    @Operation(summary = "Get company by id")
    @GetMapping("/{id}")
    public CompanyDetailResponse getCompanyById(@PathVariable Long id) {
        return companyService.getCompany(id);
    }

    @Operation(summary = "Create new Company")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDetailResponse createCompany(@RequestBody CompanyCreateRequest request) {
        return companyService.createCompany(request);
    }

    @Operation(summary = "Update Company")
    @PutMapping("/{id}")
    public CompanyDetailResponse updateCompany(@PathVariable Long id, @RequestBody CompanyUpdateRequest request) {
        return companyService.updateCompany(id, request);
    }

    @Operation(summary = "Edit addresses for Company")
    @PutMapping("/{id}/addresses")
    public CompanyDetailResponse editCompanyAddresses(@PathVariable Long id, @RequestBody CompanyAddressRequest request) {
        return companyService.updateCompany(id, request);
    }

    @Operation(summary = "Remove Company", description = "This will cascade to addresses, contact relationships, positions, and applications")
    @DeleteMapping("/{id}")
    public void removeCompany(@PathVariable Long id) {
        companyService.removeCompany(id);
    }
}
