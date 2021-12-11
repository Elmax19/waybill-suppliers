package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class AddressService {
    private static final String DUPLICATE_ADDRESS = "Address record with such details exists";

    public String processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndColumn = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("address".equals(tableAndColumn[0])) {
                return DUPLICATE_ADDRESS;
            }
        }
        return null;
    }
}
