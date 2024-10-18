package org.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.service.dto.requests.HotelRequest;
import org.service.dto.responses.HotelResponse;
import org.service.entities.Hotel;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    Hotel RequestToEntity(HotelRequest request);

    HotelRequest EntityToRequest(Hotel hotel);

    Hotel ResponseToEntity(HotelResponse response);

    HotelResponse EntityToResponse(Hotel hotel);

    void updateHotelFromRequest(HotelRequest request, @MappingTarget Hotel hotel);
}
