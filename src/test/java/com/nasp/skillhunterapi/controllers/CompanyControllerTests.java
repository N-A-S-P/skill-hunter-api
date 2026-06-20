package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.config.SecurityConfig;
import com.nasp.skillhunterapi.dto.Company.*;
import com.nasp.skillhunterapi.dto.LookupResponse;
import com.nasp.skillhunterapi.enums.AddressType;
import com.nasp.skillhunterapi.enums.CompanyType;
import com.nasp.skillhunterapi.model.Company;
import com.nasp.skillhunterapi.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static com.nasp.skillhunterapi.testutils.JsonSerializer.stringify;
import static com.nasp.skillhunterapi.util.ExceptionMessages.getEntityIdForOwnerNotFoundMessage;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CompanyController.class, // sut type
        excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class
        // Use SecurityConfig instead of OAuth2 auto-configuration.
        // Controller tests provide a mocked JwtDecoder.
        // Removing any of them will result in PAIN
        // Ask Spring why. I chose life.
)
@Import(SecurityConfig.class)
public class CompanyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @MockitoBean
    private JwtDecoder jwtDecoder; // DO NOT REMOVE OR SPRING WILL SCREAM

    private final CompanyDetailResponse detailResponse = new CompanyDetailResponse(
            1L,
            "Bumblebee Debuggers",
            "http://www.de-bumbler.org",
            "Technology",
            List.of(
                    new LookupResponse(CompanyType.STAFFING_FIRM.name(), CompanyType.STAFFING_FIRM.getDisplay())
            ),
            List.of(
                    new AddressResponse(
                            1L,
                            "867 La Brea Blvd",
                            "#5",
                            "Parasaur Palms",
                            "MT",
                            "75309",
                            List.of(
                                    new LookupResponse(AddressType.MAILING.name(), AddressType.MAILING.getDisplay())
                            )
                    )
            )
    );

    @Nested
    @DisplayName("GET /api/companies")
    class GetCompanies {
        @Test
        @DisplayName("should return companies for the current user")
        void happyPath() throws Exception {
            var company = new CompanyListItemResponse(
                    1L,
                    "Bruisers Builders",
                    "https://www.buildingbruises.com",
                    "Construction",
                    List.of(new LookupResponse(
                            CompanyType.HIRER.name(),
                            CompanyType.HIRER.getDisplay()
                    )));
            when(companyService.getCompanies()).thenReturn(List.of(company));

            mockMvc.perform(get("/api/companies")
                            .with(jwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(1)))
                    .andExpectAll(
                            jsonPath("$[0].id").value(1),
                            jsonPath("$[0].name").value(company.name()),
                            jsonPath("$[0].website").value(company.website()),
                            jsonPath("$[0].industry").value(company.industry()),
                            jsonPath("$[0].companyTypes").isArray(),
                            jsonPath("$[0].companyTypes[0].value").value(CompanyType.HIRER.name()),
                            jsonPath("$[0].companyTypes[0].display").value(CompanyType.HIRER.getDisplay())
                    );
        }

        @Test
        @DisplayName("should return 401 when no current user")
        void unauthorized() throws Exception {
            mockMvc.perform(get("/api/companies"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /api/companies/{id}")
    class GetCompanyById {
        @Test
        @DisplayName("should return mapped detail response for given id")
        void happyPath() throws Exception {
            when(companyService.getCompany(1L))
                    .thenReturn(detailResponse);

            var address = detailResponse.addresses().getFirst();
            var addressType = address.addressTypes().getFirst();

            mockMvc.perform(get("/api/companies/1").with(jwt()))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.id").value(detailResponse.id()),
                            jsonPath("$.name").value(detailResponse.name()),
                            jsonPath("$.website").value(detailResponse.website()),
                            jsonPath("$.industry").value(detailResponse.industry()),
                            jsonPath("$.companyTypes[0].value").value(CompanyType.STAFFING_FIRM.name()),
                            jsonPath("$.companyTypes[0].display").value(CompanyType.STAFFING_FIRM.getDisplay()),
                            jsonPath("$.addresses[0].id").value(address.id()),
                            jsonPath("$.addresses[0].line1").value(address.line1()),
                            jsonPath("$.addresses[0].line2").value(address.line2()),
                            jsonPath("$.addresses[0].city").value(address.city()),
                            jsonPath("$.addresses[0].state").value(address.state()),
                            jsonPath("$.addresses[0].postalCode").value(address.postalCode()),
                            jsonPath("$.addresses[0].addressTypes[0].value").value(addressType.value()),
                            jsonPath("$.addresses[0].addressTypes[0].display").value(addressType.display())
                    );
        }

        @Test
        @DisplayName("should return 404 when missing")
        void notFound() throws Exception {
            when(companyService.getCompany(999L))
                    .thenThrow(new EntityNotFoundException(
                            getEntityIdForOwnerNotFoundMessage(Company.class, 999L, 1L)
                    ));

            mockMvc.perform(get("/api/companies/999").with(jwt()))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(404),
                            jsonPath("$.error").value("Not Found"),
                            jsonPath("$.message").value("Could not find Company with id 999 for user with id 1"),
                            jsonPath("$.path").value("/api/companies/999")
                    );
        }

        @Test
        @DisplayName("should return 401 when no current user")
        void unauthorized() throws Exception {
            mockMvc.perform(get("/api/companies/1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("POST /api/companies")
    class CreateCompany {
        @Test
        @DisplayName("should return created company")
        void happyPath() throws Exception {
            var request = new CompanyCreateRequest(
                    detailResponse.name(),
                    detailResponse.website(),
                    detailResponse.industry(),
                    Set.of(CompanyType.STAFFING_FIRM),
                    List.of(new AddressCreateRequest(
                            detailResponse.addresses().getFirst().line1(),
                            detailResponse.addresses().getFirst().line2(),
                            detailResponse.addresses().getFirst().city(),
                            detailResponse.addresses().getFirst().state(),
                            detailResponse.addresses().getFirst().postalCode(),
                            Set.of(AddressType.MAILING)
                    ))
            );
            when(companyService.createCompany(any(CompanyCreateRequest.class)))
                    .thenReturn(detailResponse);

            var address = detailResponse.addresses().getFirst();
            var addressType = address.addressTypes().getFirst();

            mockMvc.perform(post("/api/companies")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.id").value(detailResponse.id()),
                            jsonPath("$.name").value(detailResponse.name()),
                            jsonPath("$.website").value(detailResponse.website()),
                            jsonPath("$.industry").value(detailResponse.industry()),
                            jsonPath("$.companyTypes[0].value").value(CompanyType.STAFFING_FIRM.name()),
                            jsonPath("$.companyTypes[0].display").value(CompanyType.STAFFING_FIRM.getDisplay()),
                            jsonPath("$.addresses[0].id").value(address.id()),
                            jsonPath("$.addresses[0].line1").value(address.line1()),
                            jsonPath("$.addresses[0].line2").value(address.line2()),
                            jsonPath("$.addresses[0].city").value(address.city()),
                            jsonPath("$.addresses[0].state").value(address.state()),
                            jsonPath("$.addresses[0].postalCode").value(address.postalCode()),
                            jsonPath("$.addresses[0].addressTypes[0].value").value(addressType.value()),
                            jsonPath("$.addresses[0].addressTypes[0].display").value(addressType.display())
                    );
        }

        @Test
        @DisplayName("should return 400 for request missing name")
        void illegalArgument() throws Exception {
            var request = new CompanyCreateRequest("", "", "", Set.of(), List.of());

            mockMvc.perform(post("/api/companies")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value(400),
                            jsonPath("$.error").value("Bad Request"),
                            jsonPath("$.message").value("name: must not be blank"),
                            jsonPath("$.path").value("/api/companies")
                    );
        }

        @Test
        @DisplayName("should return 401 when no current user")
        void unauthorized() throws Exception {
            mockMvc.perform(post("/api/companies"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("PUT /api/companies/{id}")
    class UpdateCompany {
        @Test
        @DisplayName("should return 200 and updated company")
        void happyPath() throws Exception {
            when(companyService.updateCompany(eq(1L), any(CompanyUpdateRequest.class)))
                    .thenReturn(detailResponse);

            var request = new CompanyUpdateRequest(detailResponse.name(), detailResponse.website(), detailResponse.industry(), Set.of(CompanyType.STAFFING_FIRM));

            mockMvc.perform(put("/api/companies/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.name").value(detailResponse.name()),
                            jsonPath("$.website").value(detailResponse.website()),
                            jsonPath("$.industry").value(detailResponse.industry()),
                            jsonPath("$.companyTypes[0].value").value(detailResponse.companyTypes().getFirst().value()),
                            jsonPath("$.companyTypes[0].display").value(detailResponse.companyTypes().getFirst().display())
                    );
        }

        @Test
        @DisplayName("should return 400 if no company name supplied")
        void illegalArgument() throws Exception {
            var request = new CompanyUpdateRequest("", "", "", Set.of());

            mockMvc.perform(put("/api/companies/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value(400),
                            jsonPath("$.error").value("Bad Request"),
                            jsonPath("$.message").value("name: must not be blank"),
                            jsonPath("$.path").value("/api/companies/1")
                    );
        }

        @Test
        @DisplayName("should return 404 when company not found for user")
        void notFound() throws Exception {
            when(companyService.updateCompany(eq(1L), any(CompanyUpdateRequest.class)))
                    .thenThrow(new EntityNotFoundException(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L)));

            var request = new CompanyUpdateRequest(detailResponse.name(), "", "", Set.of());

            mockMvc.perform(put("/api/companies/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(404),
                            jsonPath("$.error").value("Not Found"),
                            jsonPath("$.message").value(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L)),
                            jsonPath("$.path").value("/api/companies/1")
                    );
        }

        @Test
        @DisplayName("should return 401 when no current user")
        void unauthorized() throws Exception {
            mockMvc.perform(put("/api/companies/1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("PUT /api/companies/{id}/addresses")
    class UpdateCompanyAddresses {

        private final AddressResponse address = detailResponse.addresses().getFirst();

        @Test
        @DisplayName("should return 200 when update is successful")
        void happyPath() throws Exception {
            when(companyService.updateCompany(eq(1L), any(CompanyAddressRequest.class)))
                    .thenReturn(detailResponse);

            var request = new CompanyAddressRequest(List.of(),
                    List.of(new AddressUpdateRequest(
                            1L,
                            address.line1(),
                            address.line2(),
                            address.city(),
                            address.state(),
                            address.postalCode(),
                            Set.of(AddressType.MAILING)
                    )),
                    List.of(2L, 3L));

            mockMvc.perform(put("/api/companies/1/addresses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("should return 400 when trying to update and delete same address")
        void illegalArgument() throws Exception {
            when(companyService.updateCompany(eq(1L), any(CompanyAddressRequest.class)))
                    .thenThrow(new IllegalArgumentException("Address cannot be removed and updated in the same request"));

            var request = new CompanyAddressRequest(List.of(), List.of(
                    new AddressUpdateRequest(
                            1L,
                            address.line1(),
                            address.line2(),
                            address.city(),
                            address.state(),
                            address.postalCode(),
                            Set.of(AddressType.BILLING)
                    )
            ), List.of(1L));

            mockMvc.perform(put("/api/companies/1/addresses")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value(400),
                            jsonPath("$.error").value("Bad Request"),
                            jsonPath("$.message").value("Address cannot be removed and updated in the same request"),
                            jsonPath("$.path").value("/api/companies/1/addresses")
                    );
        }

        @Test
        @DisplayName("should return 404 when company not found")
        void notFound() throws Exception {
            when(companyService.updateCompany(eq(1L), any(CompanyAddressRequest.class)))
                    .thenThrow(new EntityNotFoundException(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L)));

            var request = new CompanyAddressRequest(List.of(),
                    List.of(new AddressUpdateRequest(
                            1L,
                            address.line1(),
                            address.line2(),
                            address.city(),
                            address.state(),
                            address.postalCode(),
                            Set.of(AddressType.MAILING)
                    )),
                    List.of(2L, 3L));

            mockMvc.perform(put("/api/companies/1/addresses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringify(request))
                            .with(jwt()))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(404),
                            jsonPath("$.error").value("Not Found"),
                            jsonPath("$.message").value(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L)),
                            jsonPath("$.path").value("/api/companies/1/addresses")
                    );
        }

        @Test
        @DisplayName("should return 401 when no current user")
        void unauthorized() throws Exception {
            mockMvc.perform(put("/api/companies/1/addresses"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("DELETE /api/companies/{id}")
    class DeleteCompany {
        @Test
        @DisplayName("should return 204 when successful")
        void happyPath() throws Exception {
            mockMvc.perform(delete("/api/companies/1")
                            .with(jwt()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 404 when no company found")
        void notFound() throws Exception {
            doThrow(new EntityNotFoundException(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L)))
                    .when(companyService).removeCompany(1L);

            mockMvc.perform(delete("/api/companies/1").with(jwt()))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(404),
                            jsonPath("$.error").value("Not Found"),
                            jsonPath("$.message").value(getEntityIdForOwnerNotFoundMessage(Company.class, 1L, 1L)),
                            jsonPath("$.path").value("/api/companies/1")
                    );
        }

        @Test
        @DisplayName("should return 401 when no current user")
        void unauthorized() throws Exception {
            mockMvc.perform(delete("/api/companies/1"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
