package com.itechart.students_lab.waybill_suppliers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ServiceException extends HttpClientErrorException {
    public ServiceException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
