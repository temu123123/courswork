package org.service.dto.hotels.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record HotelResponse(
        @Schema(description = "Hotel id in database", example = "2")
        Long id,

        @Schema(description = "Hotel name", example = "Eleon")
        String name,

        @Schema(description = "City id in database", example = "2")
        Long cityId
){
}
