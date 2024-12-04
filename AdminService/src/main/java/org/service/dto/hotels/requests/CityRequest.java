package org.service.dto.hotels.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for actions with cities")
public class CityRequest {

    @Schema(description = "City name", example = "Vitebsk")
    @Size(min = 3, max = 50, message = "The city name must be between 3 and 50 characters long!")
    @NotBlank(message = "The city name can't be blank!")
    private String name;

    @Schema(description = "Description of the city", example = "Beautiful city with wonderful architecture")
    @Size(min = 10, max = 500, message = "Description of the city must be between 30 and 500 characters long!")
    @NotBlank(message = "Description of the city can't be blank!")
    private String description;

    @Positive(message = "Country id should be positive")
    @Schema(description = "Country id in database", example = "2")
    @NotNull(message = "Country id can't be blank!")
    private Long countryId;
    
}
