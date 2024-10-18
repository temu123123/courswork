package org.service.services;

import org.service.dto.BookingRequest;
import org.service.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse getBookingById(Long id);
    List<BookingResponse> getAllBookings();
    BookingResponse addBooking(BookingRequest bookingRequest);
    BookingResponse updateBooking(Long id, BookingRequest bookingRequest);
    void deleteBooking(Long id);
}
