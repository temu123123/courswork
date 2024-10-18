package org.service.advices;

import io.swagger.models.Response;
import org.service.exceptions.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoomAdvice {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Response> handleRoomNotFoundException(RoomNotFoundException e) {
        var response = new Response();
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}

