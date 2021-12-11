package com.itechart.students_lab.waybill_suppliers.exception.handler;

import com.itechart.students_lab.waybill_suppliers.exception.AccountNotMatchException;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.exception.NoAccessException;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.service.CarService;
import com.itechart.students_lab.waybill_suppliers.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseExceptionHandler {
    private final WarehouseService warehouseService;
    private final CarService carService;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error(e.getLocalizedMessage());
        Optional<String> message = warehouseService.processSQLIntegrityConstraintViolationException(e)
                .or(() -> carService.processSQLIntegrityConstraintViolationException(e));
        return new ResponseEntity<>(message.orElse(e.getLocalizedMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExistsException(EntityExistsException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(MethodArgumentNotValidException e) {
        log.error(e.getLocalizedMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage().split(": ")[1], HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getLocalizedMessage());
        Optional<String> message = carService.processHttpMessageNotReadableException(e);
        return new ResponseEntity<>(message.orElse(e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<String> handleNoAccessException(NoAccessException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }

    @ExceptionHandler(AccountNotMatchException.class)
    public ResponseEntity<String> handleAccountNotMatchException(AccountNotMatchException e) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), e.getStatusCode());
    }
}
