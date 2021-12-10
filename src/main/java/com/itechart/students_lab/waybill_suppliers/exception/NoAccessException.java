package com.itechart.students_lab.waybill_suppliers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class NoAccessException extends HttpClientErrorException {
    public NoAccessException(String msg) {
        super(HttpStatus.FORBIDDEN, msg);
    }
}