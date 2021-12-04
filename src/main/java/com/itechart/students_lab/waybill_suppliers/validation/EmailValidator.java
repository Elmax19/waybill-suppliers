package com.itechart.students_lab.waybill_suppliers.validation;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidator {

    public boolean isValid(String value, String regex){
        return Pattern.compile(regex).matcher(value).matches();
    }

}
