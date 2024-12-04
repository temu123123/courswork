package org.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "Request for bookings")
public class BookingRequest {

    @Schema(description = "Hotel id in database", example = "8")
    @NotNull(message = "The Hotel id can't be null!")
    private Long hotelId;

    @Schema(description = "Booking start date", example = "2024-10-07")
    @NotBlank(message = "The start date can't be blank!")
    private String startDate;

    @Schema(description = "Booking end date", example = "2024-10-09")
    @NotBlank(message = "The end date can't be blank!")
    private String endDate;

    @Schema(description = "User name in database", example = "8")
    @NotNull(message = "User name can't be null!")
    private String username;

    @Schema(description = "Room id in database", example = "21")
    @NotNull(message = "The Room id can't be null!")
    private Long roomId;
}