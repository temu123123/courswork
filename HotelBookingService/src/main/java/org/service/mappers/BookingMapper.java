package org.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.service.dto.BookingRequest;
import org.service.dto.BookingResponse;
import org.service.entities.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking RequestToEntity(BookingRequest request);

    BookingRequest EntityToRequest(Booking booking);

    Booking ResponseToEntity(BookingResponse response);

    BookingResponse EntityToResponse(Booking booking);

    void updateBookingFromRequest(BookingRequest request, @MappingTarget Booking booking);
}