package com.nasp.skillhunterapi.repository;

import com.nasp.skillhunterapi.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends OwnedEntityRepository<Company> {
}
