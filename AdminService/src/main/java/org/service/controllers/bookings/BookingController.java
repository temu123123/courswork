package org.service.controllers.bookings;

import bookings.Booking.ProtoAddBookingRequest;
import bookings.Booking.ProtoBooking;
import bookings.Booking.ProtoBookingRequest;
import bookings.Booking.ProtoDeleteBookingRequest;
import bookings.Booking.ProtoGetAllBookingsResponse;
import bookings.Booking.ProtoGetBookingByIdRequest;
import bookings.Booking.ProtoUpdateBookingRequest;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.service.dto.bookings.BookingRequest;
import org.service.dto.bookings.BookingResponse;
import org.service.grpc.bookings.BookingClient;
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

@RestController
@RequestMapping("/api/v1/admin/hotels/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        ProtoGetBookingByIdRequest request = ProtoGetBookingByIdRequest.newBuilder()
                .setId(id)
                .build();
        ProtoBooking protoBooking = bookingClient.getBookingById(request);

        BookingResponse response = new BookingResponse(
                protoBooking.getId(),
                protoBooking.getHotelId(),
                protoBooking.getStartDate(),
                protoBooking.getEndDate(),
                protoBooking.getRoomId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        ProtoGetAllBookingsResponse protoResponse = bookingClient.getAllBookings();
        List<BookingResponse> response = protoResponse.getBookingsList().stream()
                .map(protoBooking -> new BookingResponse(
                        protoBooking.getId(),
                        protoBooking.getHotelId(),
                        protoBooking.getStartDate(),
                        protoBooking.getEndDate(),
                        protoBooking.getRoomId()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PostMapping
    public ResponseEntity<BookingResponse> addBooking(@RequestBody BookingRequest request) {
        ProtoAddBookingRequest protoRequest = ProtoAddBookingRequest.newBuilder()
                .setRequest(ProtoBookingRequest.newBuilder()
                        .setHotelId(request.getHotelId())
                        .setStartDate(request.getStartDate())
                        .setEndDate(request.getEndDate())
                        .setRoomId(request.getRoomId())
                        .build())
                .build();

        ProtoBooking protoBooking = bookingClient.addBooking(protoRequest);

        BookingResponse response = new BookingResponse(
                protoBooking.getId(),
                protoBooking.getHotelId(),
                protoBooking.getStartDate(),
                protoBooking.getEndDate(),
                protoBooking.getRoomId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the booking"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @RequestBody BookingRequest request) {
        ProtoUpdateBookingRequest protoRequest = ProtoUpdateBookingRequest.newBuilder()
                .setId(id)
                .setBooking(ProtoBookingRequest.newBuilder()
                        .setHotelId(request.getHotelId())
                        .setStartDate(request.getStartDate())
                        .setEndDate(request.getEndDate())
                        .setRoomId(request.getRoomId())
                        .build())
                .build();

        ProtoBooking protoBooking = bookingClient.updateBooking(protoRequest);

        BookingResponse response = new BookingResponse(
                protoBooking.getId(),
                protoBooking.getHotelId(),
                protoBooking.getStartDate(),
                protoBooking.getEndDate(),
                protoBooking.getRoomId()
        );

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted the booking"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        ProtoDeleteBookingRequest protoRequest = ProtoDeleteBookingRequest.newBuilder()
                .setId(id)
                .build();
        bookingClient.deleteBooking(protoRequest);
        return ResponseEntity.noContent().build();
    }
}