package org.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for actions with countries")
public class CountryRequest {

    @Schema(description = "Country name", example = "Georgia")
    @Size(min = 3, max = 500, message = "Country name must be between 3 and 100 characters long!")
    @NotBlank(message = "Country name can't be blankC")
    private String name;

    @Schema(description = "Description of the country", example = "Beautiful country with wonderful architecture")
    @Size(min = 10, max = 500, message = "Description of the country must be between 10 and 500 characters long!")
    @NotBlank(message = "Description of the country can't be blank!")
    private String description;

}
