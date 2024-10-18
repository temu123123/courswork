package org.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.service.dto.requests.CityRequest;
import org.service.dto.responses.CityResponse;
import org.service.entities.City;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City RequestToEntity(CityRequest request);

    CityRequest EntityToRequest(City city);

    City ResponseToEntity(CityResponse response);

    CityResponse EntityToResponse(City city);

    void updateCityFromRequest(CityRequest request, @MappingTarget City city);
}
