package org.service.controllers.hotels;

import hotels.Country;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.service.dto.hotels.requests.CountryRequest;
import org.service.dto.hotels.responses.CountryResponse;
import org.service.grpc.hotels.CountryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin/hotels/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryClient countryClient;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getCountryById(@PathVariable Long id) {
        Country.ProtoGetCountryByIdRequest request = Country.ProtoGetCountryByIdRequest.newBuilder()
                .setId(id)
                .build();
        Country.ProtoCountry protoCountry = countryClient.getCountryById(request);

        CountryResponse response = new CountryResponse(
                protoCountry.getId(),
                protoCountry.getName(),
                protoCountry.getDescription()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAllCountries() {
        Country.ProtoGetAllCountriesResponse protoResponse = countryClient.getAllCountries();
        List<CountryResponse> response = protoResponse.getCountriesList().stream()
                .map(protoCountry -> new CountryResponse(
                        protoCountry.getId(),
                        protoCountry.getName(),
                        protoCountry.getDescription()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PostMapping
    public ResponseEntity<CountryResponse> addCountry(@RequestBody CountryRequest request) {
        Country.ProtoAddCountryRequest protoRequest = Country.ProtoAddCountryRequest.newBuilder()
                .setRequest(Country.ProtoCountryRequest.newBuilder()
                        .setName(request.getName())
                        .setDescription(request.getDescription())
                        .build())
                .build();

        Country.ProtoCountry protoCountry = countryClient.addCountry(protoRequest);

        CountryResponse response = new CountryResponse(
                protoCountry.getId(),
                protoCountry.getName(),
                protoCountry.getDescription()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the country"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> updateCountry(@PathVariable Long id, @RequestBody CountryRequest request) {
        Country.ProtoUpdateCountryRequest protoRequest = Country.ProtoUpdateCountryRequest.newBuilder()
                .setCountry(Country.ProtoCountry.newBuilder()
                        .setId(id)
                        .setName(request.getName())
                        .setDescription(request.getDescription())
                        .build())
                .setId(id)
                .build();

        Country.ProtoCountry protoCountry = countryClient.updateCountry(protoRequest);

        CountryResponse response = new CountryResponse(
                protoCountry.getId(),
                protoCountry.getName(),
                protoCountry.getDescription()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the country"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        Country.ProtoDeleteCountryRequest protoRequest = Country.ProtoDeleteCountryRequest.newBuilder()
                .setId(id)
                .build();
        countryClient.deleteCountry(protoRequest);
        return ResponseEntity.noContent().build();
    }
}
