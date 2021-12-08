package com.itechart.students_lab.waybill_suppliers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class AccountNotMatchException extends HttpClientErrorException {

    public AccountNotMatchException( String statusText) {
        super(HttpStatus.NON_AUTHORITATIVE_INFORMATION, statusText);
    }
}
