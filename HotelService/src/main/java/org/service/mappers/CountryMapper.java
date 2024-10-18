package org.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.service.dto.requests.CountryRequest;
import org.service.dto.responses.CountryResponse;
import org.service.entities.Country;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country RequestToEntity(CountryRequest request);

    CountryRequest EntityToRequest(Country country);

    Country ResponseToEntity(CountryResponse response);

    CountryResponse EntityToResponse(Country country);

    void updateCountryFromRequest(CountryRequest request, @MappingTarget Country country);
}
