package com.itechart.students_lab.waybill_suppliers.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    ROLE_SYSTEM_ADMIN(Set.of(Permission.CUSTOMERS_WRITE, Permission.CUSTOMERS_READ)),
    ROLE_ADMIN(Set.of(Permission.WAREHOUSES_WRITE, Permission.WAREHOUSES_READ)),
    ROLE_DRIVER(Set.of()),
    ROLE_LOGISTICS_SPECIALIST(Set.of()),
    ROLE_DISPATCHER(Set.of(Permission.ITEMS_WRITE, Permission.ITEMS_READ, Permission.WAREHOUSE_ITEMS_WRITE, Permission.WAREHOUSE_ITEMS_READ)),
    ROLE_DIRECTOR(Set.of());

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
