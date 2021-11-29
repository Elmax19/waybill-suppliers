package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/customer")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity<Customer> register(@RequestBody Customer customer){
        authorizationService.addNewCustomer(customer);
        return ResponseEntity.ok(customer);
    }

}
