package com.nasp.skillhunterapi.service;

import com.nasp.skillhunterapi.dto.LookupDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class LookupServiceTests {
    @Test
    @DisplayName("getLookup should return mapped dtos")
    void getLookupHappyPath() {
        var sut = new LookupService();

        var result = sut.getLookup("workLocation");

        assertThat(result).hasSize(3);
        assertThat(result).extracting(LookupDto::value).containsAll(List.of("ON_SITE", "HYBRID", "REMOTE"));
        assertThat(result).extracting(LookupDto::display).containsAll(List.of("On-site", "Hybrid", "Remote"));
    }

    @Test
    @DisplayName("getLookup should throw not found")
    void notFound() {
        var sut = new LookupService();

        assertThatThrownBy(() -> sut.getLookup("junk"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("junk lookup type does not exist");
    }

    @Test
    @DisplayName("getLookup should throw illegal argument when lookupType is null")
    void lookupTypeNull() {
        var sut = new LookupService();

        assertThatThrownBy(() -> sut.getLookup(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("lookup type must not be null, empty or all whitespace");
    }

    @Test
    @DisplayName("getLookup should throw illegal argument when lookupType is empty")
    void lookupTypeEmpty() {
        var sut = new LookupService();

        assertThatThrownBy(() -> sut.getLookup(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("lookup type must not be null, empty or all whitespace");
    }

    @Test
    @DisplayName("getLookup should throw illegal argument when lookupType is whitespace")
    void lookupTypeAllWhitespace() {
        var sut = new LookupService();

        assertThatThrownBy(() -> sut.getLookup("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("lookup type must not be null, empty or all whitespace");
    }
}
