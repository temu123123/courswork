package org.service.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record CountryResponse(
        @Schema(description = "Country id in database", example = "2")
        Long id,

        @Schema(description = "Country name", example = "Georgia")
        String name,

        @Schema(description = "Description of the country", example = "Beautiful country with wonderful architecture")
        String description
){
}
