package org.service.advices;

import io.swagger.models.Response;
import org.service.exceptions.HotelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HotelAdvice {

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<Response> handleHotelNotFoundException(HotelNotFoundException e) {
        var response = new Response();
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
