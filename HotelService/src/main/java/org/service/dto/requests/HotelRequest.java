package org.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for actions with hotels")
public class HotelRequest {

    @Schema(description = "Hotel name", example = "Eleon")
    @Size(min = 3, max = 50, message = "The hotel name must be between 3 and 50 characters long!")
    @NotBlank(message = "The hotel name can't be blank!")
    private String name;

    @Schema(description = "City id in database", example = "2")
    @NotNull(message = "City id can't be blank!")
    private Long cityId;

}
