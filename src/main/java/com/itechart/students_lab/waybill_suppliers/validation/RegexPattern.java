package com.itechart.students_lab.waybill_suppliers.validation;

public class RegexPattern {

    public static final String EMAIL = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,10})$";
    public static final String EMAIL_MESSAGE = "Your account was successfully activated.\nYour password: %s";
    public static final String EMAIL_TITLE = "Welcome, %s!";
}
