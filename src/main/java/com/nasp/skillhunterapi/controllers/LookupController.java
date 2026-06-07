package com.nasp.skillhunterapi.controllers;

import com.nasp.skillhunterapi.dto.ApiError;
import com.nasp.skillhunterapi.dto.LookupDto;
import com.nasp.skillhunterapi.service.LookupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Lookup", description = "Get lookup values based on internal data")
@RequestMapping(value = "/api/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
class LookupController {
    private final LookupService service;

    @Operation(summary = "Get lookup values", description = """
            Returns the availale values for a given lookup type.""")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Lookup values returned successfully",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LookupDto.class))
                    )
            ),
            @ApiResponse(
                    description = "Lookup type not found",
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    description = "Invalid lookup type",
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @GetMapping("/{lookupType}")
    public List<LookupDto> getLookup(
            @PathVariable
            String lookupType
    ) {
        return service.getLookup(lookupType);
    }

}
