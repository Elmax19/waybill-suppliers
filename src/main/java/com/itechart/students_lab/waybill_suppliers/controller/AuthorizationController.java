package com.itechart.students_lab.waybill_suppliers.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @GetMapping("/")
    public String greeting(){
        return "hello";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('customers:read', 'customers:write')")
    public String afterLogin(){
        return "admin";
    }

}
