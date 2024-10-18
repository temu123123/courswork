package org.service.advices;

import io.swagger.models.Response;
import org.service.exceptions.CountryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CountryAdvice {

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<Response> handleCountryNotFoundException(CountryNotFoundException e) {
        var response = new Response();
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
