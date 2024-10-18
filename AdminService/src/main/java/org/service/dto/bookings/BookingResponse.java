package org.service.dto.bookings;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookingResponse(

        @Schema(description = "Booking id in database", example = "2")
        Long id,

        @Schema(description = "Hotel id in database", example = "2")
        Long hotelId,

        @Schema(description = "Booking start date", example = "13.03.1999")
        String startDate,

        @Schema(description = "Booking end date", example = "14.05.2024")
        String endDate,

        @Schema(description = "Room id in database", example = "21")
        Long roomId
){
}
