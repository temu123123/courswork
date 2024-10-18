package org.service.services.interfaces;

import org.service.dto.requests.CityRequest;
import org.service.dto.responses.CityResponse;

import java.util.List;

public interface CityService {
    CityResponse getCityById(Long id);
    List<CityResponse> getAllCities();
    CityResponse addCity(CityRequest cityRequest);
    CityResponse updateCity(Long id, CityRequest cityRequest);
    void deleteCity(Long id);
}
