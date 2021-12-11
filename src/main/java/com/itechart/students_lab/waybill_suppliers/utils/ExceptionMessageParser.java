package com.itechart.students_lab.waybill_suppliers.utils;

public class ExceptionMessageParser {
    public static String[] parseSqlDuplicateEntryMessage(String message) {
        String[] messageDetails = message.split(" ");
        String columnInTable = messageDetails[messageDetails.length - 1];
        return columnInTable
                .substring(1, columnInTable.length() - 1)
                .split("\\.");
    }
}
