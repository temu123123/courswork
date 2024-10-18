package org.service.dto.hotels.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record CityResponse(
        @Schema(description = "City id in database", example = "2")
        Long id,

        @Schema(description = "City name", example = "Vitebsk")
        String name,

        @Schema(description = "Description of the city", example = "Beautiful city with wonderful architecture")
        String description,

        @Schema(description = "Country id in database", example = "2")
        Long countryId
){
}
