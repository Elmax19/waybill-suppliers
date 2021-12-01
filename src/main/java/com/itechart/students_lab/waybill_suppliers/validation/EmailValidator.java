package com.itechart.students_lab.waybill_suppliers.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {

    public static boolean isValid(String value, String regex){
        return Pattern.compile(regex).matcher(value).matches();
    }

}
