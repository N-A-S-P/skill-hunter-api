package com.nasp.skillhunterapi.mapping;

import com.nasp.skillhunterapi.dto.Company.AddressCreateRequest;
import com.nasp.skillhunterapi.dto.Company.AddressUpdateRequest;
import com.nasp.skillhunterapi.enums.AddressType;
import com.nasp.skillhunterapi.model.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressMapperTests {

    @Test
    @DisplayName("should map Address to AddressResponse")
    void toResponseTest() {
        var sut = new AddressMapper(new LookupMapper());
        var address = new Address(
                "123 Westworth Dr",
                "",
                "Anytown",
                "IA",
                "99999",
                Set.of(AddressType.HQ));
        address.setId(1L);

        var result = sut.toResponse(address);

        assertThat(result.id()).isEqualTo(address.getId());
        assertThat(result.line1()).isEqualTo(address.getLine1());
        assertThat(result.line2()).isEqualTo(address.getLine2());
        assertThat(result.city()).isEqualTo(address.getCity());
        assertThat(result.state()).isEqualTo(address.getState());
        assertThat(result.postalCode()).isEqualTo(address.getZipCode());
        assertThat(result.addressTypes()).singleElement()
                        .satisfies(type -> {
                            assertThat(type.value()).isEqualTo("HQ");
                            assertThat(type.display()).isEqualTo("Headquarters");
                        });
    }

    @Test
    @DisplayName("should map AddressCreateModel to Address")
    void toEntityTest() {
        var sut = new AddressMapper(new LookupMapper());
        var request = new AddressCreateRequest("456 Creature Ave", "Ste 1313", "Salem", "MA", "00000", Set.of(AddressType.BILLING));

        var result = sut.toEntity(request);

        assertThat(result.getLine1()).isEqualTo(request.line1());
        assertThat(result.getLine2()).isEqualTo(request.line2());
        assertThat(result.getCity()).isEqualTo(request.city());
        assertThat(result.getState()).isEqualTo(request.state());
        assertThat(result.getZipCode()).isEqualTo(request.postalCode());
        assertThat(result.getAddressTypes()).hasSize(1)
            .containsExactly(AddressType.BILLING);
    }

    @Test
    @DisplayName("should update Address from AddressUpdateModel")
    void updateEntityTest() {
        var sut = new AddressMapper(new LookupMapper());
        var address = new Address(
                "123 Westworth Dr",
                "",
                "Anytown",
                "IA",
                "99999",
                Set.of(AddressType.HQ));
        address.setId(1L);
        var request = new AddressUpdateRequest(1L,"456 Creature Ave", "Ste 1313", "Salem", "MA", "00000", Set.of(AddressType.BILLING, AddressType.MAILING));

        sut.updateEntity(address, request);

        assertThat(address.getLine1()).isEqualTo(request.line1());
        assertThat(address.getLine2()).isEqualTo(request.line2());
        assertThat(address.getCity()).isEqualTo(request.city());
        assertThat(address.getState()).isEqualTo(request.state());
        assertThat(address.getZipCode()).isEqualTo(request.postalCode());
        assertThat(address.getAddressTypes()).hasSize(2)
                .containsExactlyInAnyOrder(AddressType.BILLING, AddressType.MAILING);
    }
}
