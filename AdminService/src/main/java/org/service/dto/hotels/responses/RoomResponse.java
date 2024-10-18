package org.service.dto.hotels.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomResponse(
        @Schema(description = "Room id in database", example = "1")
        Long Id,

        @Schema(description = "Hotel id in database", example = "1")
        Long hotelId,

        @Schema(description = "Room type", example = "Lux")
        String type,

        @Schema(description = "Room price", example = "100")
        Long price,

        @Schema(description = "Is room available for booking", example = "true")
        boolean isAvailable
){
}
