package com.itechart.students_lab.waybill_suppliers.entity;

public enum Permission {
    CUSTOMERS_WRITE("customers:write"),
    CUSTOMERS_READ("customers:read"),
    ITEMS_WRITE("items:write"),
    ITEMS_READ("items:read"),
    WAREHOUSE_ITEMS_WRITE("warehouseItems:write"),
    WAREHOUSE_ITEMS_READ("warehouseItems:read"),
    EMPLOYEES_READ("employees:read"),
    EMPLOYEES_WRITE("employees:write"),
    WAREHOUSES_WRITE("warehouses:write"),
    WAREHOUSES_READ("warehouses:read"),
    DISPATCHING_APPLICATIONS_READ("applications.dispatching:read"),
    DISPATCHING_APPLICATIONS_WRITE("applications.dispatching:write"),
    ALL_APPLICATIONS_READ("applications.all:read"),
    ALL_APPLICATIONS_WRITE("applications.all:write"),
    ACCOUNT_READ("account:read"),
    ACCOUNT_WRITE("account:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
