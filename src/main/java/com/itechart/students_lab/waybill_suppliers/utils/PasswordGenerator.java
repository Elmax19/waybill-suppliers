package com.itechart.students_lab.waybill_suppliers.utils;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
public class PasswordGenerator {

    public String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(48, 90)
                .build();
        return pwdGenerator.generate(length);
    }
}
