package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.Company.*;
import com.nasp.skillhunterapi.mapping.BaseEntityMapper;
import com.nasp.skillhunterapi.mapping.OwnedEntityMapper;
import com.nasp.skillhunterapi.model.Address;
import com.nasp.skillhunterapi.model.Company;
import com.nasp.skillhunterapi.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdForOwnerNotFoundMessage;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ProfileService profileService;
    private final OwnedEntityMapper<Company, CompanyDetailResponse, CompanyListItemResponse, CompanyCreateRequest, CompanyUpdateRequest> mapper;
    private final BaseEntityMapper<Address, AddressResponse, AddressCreateRequest, AddressUpdateRequest> addressMapper;

    private Long currentUserId() {
        return profileService.getCurrentUserId();
    }

    public CompanyDetailResponse getCompany(Long id) {
        return mapper.toDetailResponse(getCompanyByIdAndUserId(id));
    }

    public List<CompanyListItemResponse> getCompanies() {
        var currentUserId = currentUserId();
        return companyRepository.findAllByOwnerId(currentUserId)
                .stream()
                .map(mapper::toListItemResponse)
                .toList();
    }

    public CompanyDetailResponse createCompany(CompanyCreateRequest request) {
        var entity = mapper.toEntity(request, profileService.getCurrentUser());
        companyRepository.save(entity);
        return mapper.toDetailResponse(entity);
    }

    public CompanyDetailResponse updateCompany(Long id, CompanyUpdateRequest request) {
        var entity = getCompanyByIdAndUserId(id);
        mapper.updateEntity(entity, request);

        var saved = companyRepository.save(entity);
        return mapper.toDetailResponse(saved);
    }

    public CompanyDetailResponse updateCompany(Long id, CompanyAddressRequest request) {
        var entity = getCompanyByIdAndUserId(id);

        if (request.hasConflictingAddressIds())
            throw new IllegalArgumentException("Address cannot be removed and updated in the same request");

        for (var removeId : request.removeAddressIds()) {
            entity.removeAddressById(removeId);
        }

        for (var updateRequest : request.updateAddresses()) {
            var existing = entity.getAddressById(updateRequest.id())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Address %d does not exist for company %d"
                                    .formatted(updateRequest.id(), id)));
            addressMapper.updateEntity(existing, updateRequest);
        }

        for (var createRequest : request.createAddresses()) {
            var newAddress = addressMapper.toEntity(createRequest);
            entity.addAddress(newAddress);
        }

        var saved = companyRepository.save(entity);
        return mapper.toDetailResponse(saved);
    }

    public void removeCompany(Long id) {
        var company = getCompanyByIdAndUserId(id);
        company.removeRelationships();
        companyRepository.delete(company);
    }

    private Company getCompanyByIdAndUserId(Long id) {
        var currentUserId = currentUserId();
        return companyRepository.findByIdAndOwnerId(id, currentUserId)
                .orElseThrow(() ->
                        new EntityNotFoundException(getEntityIdForOwnerNotFoundMessage(Company.class, id, currentUserId)));
    }
}
