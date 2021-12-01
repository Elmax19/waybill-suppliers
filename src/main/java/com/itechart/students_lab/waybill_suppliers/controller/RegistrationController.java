package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.service.AuthorizationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/customer")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity register(@RequestBody Customer customer) {
        System.out.println(customer);
        return authorizationService.addNewCustomer(customer);
    }

}
