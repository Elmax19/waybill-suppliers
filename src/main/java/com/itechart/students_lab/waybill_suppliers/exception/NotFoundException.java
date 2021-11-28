package com.itechart.students_lab.waybill_suppliers.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
