package org.service.grpc.bookings;

import bookings.Booking.ProtoAddBookingRequest;
import bookings.Booking.ProtoBooking;
import bookings.Booking.ProtoDeleteBookingRequest;
import bookings.Booking.ProtoGetAllBookingsResponse;
import bookings.Booking.ProtoGetBookingByIdRequest;
import bookings.Booking.ProtoUpdateBookingRequest;
import bookings.ProtoBookingServiceGrpc.ProtoBookingServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.service.exceptions.BookingNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingClient {

    @GrpcClient("bookingService")
    private ProtoBookingServiceBlockingStub blockingStub;

    public ProtoBooking getBookingById(ProtoGetBookingByIdRequest request) {
        try {
            return blockingStub.getBookingById(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while getting booking by ID: {}", e.getStatus().getDescription());
            throw new BookingNotFoundException("Error while getting booking by ID");
        }
    }

    public ProtoGetAllBookingsResponse getAllBookings() {
        try {
            return blockingStub.getAllBookings(com.google.protobuf.Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            log.error("Error while getting all bookings: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoBooking addBooking(ProtoAddBookingRequest request) {
        try {
            return blockingStub.addBooking(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while adding booking: {}", e.getStatus().getDescription());
            throw e;
        }
    }

    public ProtoBooking updateBooking(ProtoUpdateBookingRequest request) {
        try {
            return blockingStub.updateBooking(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while updating booking: {}", e.getStatus().getDescription());
            throw new BookingNotFoundException("Error while updating booking");
        }
    }

    public void deleteBooking(ProtoDeleteBookingRequest request) {
        try {
            blockingStub.deleteBooking(request);
        } catch (StatusRuntimeException e) {
            log.error("Error while deleting booking: {}", e.getStatus().getDescription());
            throw new BookingNotFoundException("Error while deleting booking");
        }
    }

}
