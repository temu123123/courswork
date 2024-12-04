package org.service.advices;

import io.swagger.models.Response;
import org.service.exceptions.BookingNotFoundException;
import org.service.exceptions.RoomAlreadyBookedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookingAdvice {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Response> handleBookingNotFoundException(BookingNotFoundException e) {
        var response = new Response();
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomAlreadyBookedException.class)
    public ResponseEntity<Response> handleRoomAlreadyBookedException(RoomAlreadyBookedException e) {
        var response = new Response();
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
