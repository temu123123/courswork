package org.service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.service.dao.BookingRepository;
import org.service.dto.BookingRequest;
import org.service.dto.BookingResponse;
import org.service.entities.Booking;
import org.service.exceptions.BookingNotFoundException;
import org.service.kafka.BookingProducer;
import org.service.mappers.BookingMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultBookingService implements BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final BookingProducer producer;

    @Override
    public BookingResponse getBookingById(Long id) {
        var bookingEntity = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: id = " + id));

        return mapper.EntityToResponse(bookingEntity);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        Iterable<Booking> bookings = repository.findAll();
        return StreamSupport.stream(bookings.spliterator(), false)
                .map(mapper::EntityToResponse)
                .toList();
    }

    @Override
    public BookingResponse addBooking(BookingRequest bookingRequest) {
        var bookingEntity = mapper.RequestToEntity(bookingRequest);
        System.out.println("Booking request save: " + bookingRequest);
        System.out.println("Booking entity before save: " + bookingEntity);
        repository.save(bookingEntity);
        producer.sendMessage("bookingTopic", bookingEntity.getHotelId(), bookingEntity.getRoomId());

        return mapper.EntityToResponse(bookingEntity);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest bookingRequest) {
        var existingBooking = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: id = " + id));

        mapper.updateBookingFromRequest(bookingRequest, existingBooking);

        var updatedBooking = repository.save(existingBooking);
        return mapper.EntityToResponse(updatedBooking);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!repository.existsById(id)) {
            throw new BookingNotFoundException("Booking not found with ID: " + id);
        }

        var bookingEntity = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: id = " + id));

        producer.sendMessage("bookingTopicDelete", bookingEntity.getHotelId(), bookingEntity.getRoomId());

        repository.deleteById(id);
    }
}