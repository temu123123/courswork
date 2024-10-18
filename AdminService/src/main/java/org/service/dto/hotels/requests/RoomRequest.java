package org.service.dto.hotels.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for actions with rooms")
public class RoomRequest {

    @Schema(description = "Room type", example = "Lux")
    @Size(min = 3, max = 50, message = "The room type must be between 3 and 50 characters long!")
    @NotBlank(message = "The room type can't be blank!")
    private String type;

    @Schema(description = "Room price", example = "100")
    @NotBlank(message = "The room price can't be blank!")
    private Long price;

    @Schema(description = "Is room available for booking", example = "true")
    @NotBlank(message = "The room price can't be blank!")
    private boolean isAvailable;

}
