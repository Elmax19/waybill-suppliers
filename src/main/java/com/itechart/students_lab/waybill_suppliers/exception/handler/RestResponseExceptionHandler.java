package com.itechart.students_lab.waybill_suppliers.exception.handler;

import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class RestResponseExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        LOGGER.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(SQLIntegrityConstraintViolationException e){
        LOGGER.error(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entered value is not unique");
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e){
        LOGGER.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        LOGGER.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
