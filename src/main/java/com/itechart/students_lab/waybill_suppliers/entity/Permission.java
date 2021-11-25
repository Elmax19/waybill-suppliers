package com.itechart.students_lab.waybill_suppliers.entity;

public enum Permission {
    CUSTOMERS_WRITE("customers:write"),
    CUSTOMERS_READ("customers:read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
