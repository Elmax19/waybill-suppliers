package com.itechart.students_lab.waybill_suppliers.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    ROLE_SYSTEM_ADMIN(Set.of(Permission.CUSTOMERS_WRITE)),
    ROLE_ADMIN(Set.of()),
    ROLE_DRIVER(Set.of()),
    ROLE_LOGISTICS_SPECIALIST(Set.of()),
    ROLE_DISPATCHER(Set.of()),
    ROLE_DIRECTOR(Set.of());

    private final Set<Permission> permissions;


    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.getPermission())
        ).collect(Collectors.toSet());
    }
}
