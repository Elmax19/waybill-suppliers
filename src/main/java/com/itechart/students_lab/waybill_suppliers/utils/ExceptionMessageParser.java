package com.itechart.students_lab.waybill_suppliers.utils;

public class ExceptionMessageParser {
    public static String[] parseSqlDuplicateEntryMessage(String message) {
        String[] messageDetails = message.split(" ");
        String tableAndKeyString = messageDetails[messageDetails.length - 1];
        return tableAndKeyString
                .substring(1, tableAndKeyString.length() - 1)
                .split("\\.");
    }
}
