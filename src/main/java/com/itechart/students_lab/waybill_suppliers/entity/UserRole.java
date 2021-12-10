package com.itechart.students_lab.waybill_suppliers.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    ROLE_SYSTEM_ADMIN(Set.of(Permission.CUSTOMERS_WRITE, Permission.CUSTOMERS_READ)),
    ROLE_ADMIN(Set.of(Permission.ACCOUNT_READ, Permission.ACCOUNT_WRITE, Permission.EMPLOYEES_READ, Permission.EMPLOYEES_WRITE,
            Permission.WAREHOUSES_WRITE, Permission.WAREHOUSES_READ)),
    ROLE_DRIVER(Set.of(Permission.ACCOUNT_READ, Permission.ACCOUNT_WRITE)),
    ROLE_LOGISTICS_SPECIALIST(Set.of(Permission.DISPATCHING_APPLICATIONS_READ, Permission.ACCOUNT_READ, Permission.ACCOUNT_WRITE)),
    ROLE_DISPATCHER(Set.of(Permission.ACCOUNT_READ, Permission.ACCOUNT_WRITE, Permission.ITEMS_WRITE, Permission.ITEMS_READ,
            Permission.WAREHOUSE_ITEMS_WRITE, Permission.WAREHOUSE_ITEMS_READ, Permission.ALL_APPLICATIONS_READ, Permission.DISPATCHING_APPLICATIONS_READ)),
    ROLE_DIRECTOR(Set.of(Permission.ACCOUNT_READ, Permission.ACCOUNT_WRITE));

    private final Set<Permission> permissions;


    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.getPermission())
        ).collect(Collectors.toSet());
    }
}
