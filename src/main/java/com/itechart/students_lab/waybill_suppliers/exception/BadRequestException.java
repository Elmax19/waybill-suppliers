package com.itechart.students_lab.waybill_suppliers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class BadRequestException extends HttpClientErrorException {

    public BadRequestException(String statusText) {
        super(HttpStatus.BAD_REQUEST, statusText);
    }
}
