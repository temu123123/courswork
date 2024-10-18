package org.service.controllers.hotels;

import hotels.City;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.service.dto.hotels.responses.CityResponse;
import org.service.dto.hotels.requests.CityRequest;
import org.service.grpc.hotels.CityClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/hotels/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityClient cityClient;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long id) {
        City.ProtoGetCityByIdRequest request = City.ProtoGetCityByIdRequest.newBuilder()
                .setId(id)
                .build();
        City.ProtoCity protoCity = cityClient.getCityById(request);

        CityResponse response = new CityResponse(
                protoCity.getId(),
                protoCity.getName(),
                protoCity.getDescription(),
                protoCity.getCountryId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        City.ProtoGetAllCitiesResponse protoResponse = cityClient.getAllCities();
        List<CityResponse> response = protoResponse.getCitiesList().stream()
                .map(protoCity -> new CityResponse(
                        protoCity.getId(),
                        protoCity.getName(),
                        protoCity.getDescription(),
                        protoCity.getCountryId()
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
    public ResponseEntity<CityResponse> addCity(@RequestBody CityRequest request) {
        City.ProtoAddCityRequest protoRequest = City.ProtoAddCityRequest.newBuilder()
                .setRequest(City.ProtoCityRequest.newBuilder()
                        .setCountryId(request.getCountryId())
                        .setName(request.getName())
                        .setDescription(request.getDescription())
                        .build())
                .build();

        City.ProtoCity protoCity = cityClient.addCity(protoRequest);

        CityResponse response = new CityResponse(
                protoCity.getId(),
                protoCity.getName(),
                protoCity.getDescription(),
                protoCity.getCountryId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the city"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(@PathVariable Long id, @RequestBody CityRequest request) {
        City.ProtoUpdateCityRequest protoRequest = City.ProtoUpdateCityRequest.newBuilder()
                .setCity(City.ProtoCityRequest.newBuilder()
                        .setName(request.getName())
                        .setCountryId(request.getCountryId())
                        .setDescription(request.getDescription())
                        .build())
                .setId(id)
                .build();

        City.ProtoCity protoCity = cityClient.updateCity(protoRequest);

        CityResponse response = new CityResponse(
                protoCity.getId(),
                protoCity.getName(),
                protoCity.getDescription(),
                protoCity.getCountryId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the city"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        City.ProtoDeleteCityRequest protoRequest = City.ProtoDeleteCityRequest.newBuilder()
                .setId(id)
                .build();
        cityClient.deleteCity(protoRequest);
        return ResponseEntity.noContent().build();
    }
}
