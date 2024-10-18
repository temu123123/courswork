package org.service.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.service.dto.requests.CityRequest;
import org.service.dto.responses.CityResponse;
import org.service.services.interfaces.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService service;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityResponse getCity(@PathVariable Long id){
        return service.getCityById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CityResponse> getAllCities() {
        return service.getAllCities();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityResponse addCity(@RequestBody @Valid CityRequest request) {
        return service.addCity(request);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the city"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityResponse updateCity(@PathVariable Long id, @RequestBody @Valid CityRequest request) {
        return service.updateCity(id, request);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the city"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        service.deleteCity(id);
    }
}
