package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Company.CompanyCreateRequest;
import com.nasp.skillhunterapi.dto.Company.CompanyDetailResponse;
import com.nasp.skillhunterapi.dto.Company.CompanyListItemResponse;
import com.nasp.skillhunterapi.dto.Company.CompanyUpdateRequest;
import com.nasp.skillhunterapi.model.Profile;
import com.nasp.skillhunterapi.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper implements
        OwnedEntityMapper<Company,
                CompanyDetailResponse,
                CompanyListItemResponse,
                CompanyCreateRequest,
                CompanyUpdateRequest> {
    private final AddressMapper addressMapper;
    private final LookupMapper lookupMapper;

    public CompanyMapper(AddressMapper addressMapper, LookupMapper lookupMapper) {
        this.addressMapper = addressMapper;
        this.lookupMapper = lookupMapper;
    }
    @Override
    public CompanyDetailResponse toDetailResponse(Company entity) {
        return new CompanyDetailResponse(
                entity.getId(),
                entity.getName(),
                entity.getWebsite(),
                entity.getIndustry(),
                entity.getCompanyTypes().stream().map(lookupMapper::toResponse).toList(),
                entity.getAddresses().stream().map(addressMapper::toResponse).toList()
        );
    }

    @Override
    public CompanyListItemResponse toListItemResponse(Company entity) {
        return new CompanyListItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getWebsite(),
                entity.getIndustry(),
                entity.getCompanyTypes().stream().map(lookupMapper::toResponse).toList()
        );
    }

    @Override
    public Company toEntity(CompanyCreateRequest request, Profile owner) {
        var company = new Company(
                request.name(),
                request.website(),
                request.industry(),
                request.companyTypes(),
                request.addresses().stream().map(addressMapper::toEntity).toList()
        );
        company.setOwner(owner);
        return company;
    }

    @Override
    public void updateEntity(Company entity, CompanyUpdateRequest request) {
        entity.setName(request.name());
        entity.setWebsite(request.website());
        entity.setIndustry(request.industry());
        entity.setCompanyTypes(request.companyTypes());
    }
}
