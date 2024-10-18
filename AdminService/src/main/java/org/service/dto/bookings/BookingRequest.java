package org.service.dto.bookings;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for bookings")
public class BookingRequest {

    @Schema(description = "Hotel id in database", example = "8")
    @NotBlank(message = "The Hotel id can't be blank!")
    private Long hotelId;

    @Schema(description = "Booking start date", example = "13.03.1999")
    @NotBlank(message = "The start date can't be blank!")
    private String startDate;

    @Schema(description = "Booking end date", example = "14.05.2024")
    @NotBlank(message = "The end date can't be blank!")
    private String endDate;

    @Schema(description = "Room id in database", example = "21")
    @NotBlank(message = "The Room id can't be blank!")
    private Long roomId;

}
