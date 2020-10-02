package org.jsalazar.checkoutservice.web

import org.jsalazar.checkoutservice.Exceptions.ReservationNotFound
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ExceptionResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseHandler.class)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<String> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage())
        new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationNotFound.class)
    void onMethodArgumentNotValidException(ReservationNotFound e) {
        LOGGER.error(e.getMessage())
    }
}
