package org.service.services.interfaces;

import org.service.dto.requests.CountryRequest;
import org.service.dto.responses.CountryResponse;

import java.util.List;

public interface CountryService {
    CountryResponse getCountryById(Long id);
    List<CountryResponse> getAllCountries();
    CountryResponse addCountry(CountryRequest countryRequest);
    CountryResponse updateCountry(Long id, CountryRequest countryRequest);
    void deleteCountry(Long id);
}
