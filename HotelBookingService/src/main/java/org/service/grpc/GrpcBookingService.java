package org.service.grpc;

import bookings.Booking.ProtoAddBookingRequest;
import bookings.Booking.ProtoBooking;
import bookings.Booking.ProtoDeleteBookingRequest;
import bookings.Booking.ProtoGetAllBookingsResponse;
import bookings.Booking.ProtoGetBookingByIdRequest;
import bookings.Booking.ProtoUpdateBookingRequest;
import bookings.ProtoBookingServiceGrpc.ProtoBookingServiceImplBase;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.service.dto.BookingRequest;
import org.service.dto.BookingResponse;
import org.service.exceptions.BookingNotFoundException;
import org.service.exceptions.RoomAlreadyBookedException;
import org.service.services.BookingService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@Transactional
public class GrpcBookingService extends ProtoBookingServiceImplBase {

    private final BookingService service;

    private ProtoBooking newResponse(BookingResponse booking){
        return ProtoBooking.newBuilder()
                .setId(booking.id())
                .setHotelId(booking.hotelId())
                .setStartDate(booking.startDate())
                .setEndDate(booking.endDate())
                .setUsername(booking.username())
                .setRoomId(booking.roomId())
                .build();
    }

    @Override
    public void getBookingById(ProtoGetBookingByIdRequest request, StreamObserver<ProtoBooking> responseObserver) {
        try {
            BookingResponse booking = service.getBookingById(request.getId());
            ProtoBooking response = newResponse(booking);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (BookingNotFoundException e) {
            log.error("Country not found: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("Booking not found").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while getting booking by ID: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllBookings(Empty request, StreamObserver<ProtoGetAllBookingsResponse> responseObserver) {
        try {
            List<BookingResponse> bookings = service.getAllBookings();
            List<ProtoBooking> protoCountries = bookings.stream()
                    .map(this::newResponse)
                    .toList();
            ProtoGetAllBookingsResponse response = ProtoGetAllBookingsResponse.newBuilder()
                    .addAllBookings(protoCountries)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error while getting all bookings: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void addBooking(ProtoAddBookingRequest request, StreamObserver<ProtoBooking> responseObserver) {
        try {
            BookingRequest bookingRequest = new BookingRequest();
            bookingRequest.setHotelId(request.getRequest().getHotelId());
            bookingRequest.setStartDate(request.getRequest().getStartDate());
            bookingRequest.setEndDate(request.getRequest().getEndDate());
            bookingRequest.setUsername(request.getRequest().getUsername());
            bookingRequest.setRoomId(request.getRequest().getRoomId());
            BookingResponse newBooking = service.addBooking(bookingRequest);
            ProtoBooking response = newResponse(newBooking);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RoomAlreadyBookedException e) {
            log.error("Room already booked: {}", e.getMessage());
            responseObserver.onError(Status.fromCode(Status.Code.CANCELLED).withDescription("Room already booked").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while adding booking: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateBooking(ProtoUpdateBookingRequest request, StreamObserver<ProtoBooking> responseObserver) {
        try {
            BookingRequest bookingRequest = new BookingRequest();
            bookingRequest.setHotelId(request.getBooking().getHotelId());
            bookingRequest.setStartDate(request.getBooking().getStartDate());
            bookingRequest.setEndDate(request.getBooking().getEndDate());
            bookingRequest.setUsername(request.getBooking().getUsername());
            bookingRequest.setRoomId(request.getBooking().getRoomId());
            BookingResponse updatedBooking = service.updateBooking(request.getId(), bookingRequest);
            ProtoBooking response = newResponse(updatedBooking);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (BookingNotFoundException e) {
            log.error("Booking not found for update: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("Booking not found for update").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while updating booking: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteBooking(ProtoDeleteBookingRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.deleteBooking(request.getId());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BookingNotFoundException e) {
            log.error("Country not found for deletion: {}", e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription("Booking not found for deletion").asRuntimeException());
        } catch (Exception e) {
            log.error("Error while deleting booking: {}", e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
