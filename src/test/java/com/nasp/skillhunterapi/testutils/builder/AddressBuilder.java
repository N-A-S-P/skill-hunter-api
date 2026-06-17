package com.nasp.skillhunterapi.testutils.builder;

import com.nasp.skillhunterapi.enums.AddressType;
import com.nasp.skillhunterapi.model.Address;

import java.util.HashSet;
import java.util.List;

public class AddressBuilder {
    private Long id = 1L;
    private String line1 = "867 La Brea Blvd";
    private String line2 = "#5";
    private String city = "Parasaur Palms";
    private String state = "MT";
    private String zipCode = "75309";
    private HashSet<AddressType> addressTypes;

    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }

    public AddressBuilder withId(Long value) {
        id = value;
        return this;
    }

    public AddressBuilder withLine1(String value) {
        line1 = value;
        return this;
    }

    public AddressBuilder withLine2(String value) {
        line2 = value;
        return this;
    }

    public AddressBuilder withAddressTypes(AddressType... value) {
        addressTypes = new HashSet<>(List.of(value));
        return this;
    }

    public Address build() {
        var address = new Address(line1, line2, city, state, zipCode, addressTypes);
        address.setId(id);

        return address;
    }
}
