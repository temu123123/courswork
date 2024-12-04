package org.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookingResponse(

        @Schema(description = "Booking id in database", example = "2")
        Long id,

        @Schema(description = "Hotel id in database", example = "2")
        Long hotelId,

        @Schema(description = "Booking start date", example = "2024-10-07")
        String startDate,

        @Schema(description = "Booking end date", example = "2024-10-09")
        String endDate,

        @Schema(description = "User name", example = "John")
        String username,

        @Schema(description = "Room id in database", example = "21")
        Long roomId
) {
}