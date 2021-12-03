package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.service.AuthorizationService;
import com.itechart.students_lab.waybill_suppliers.validation.EmailValidator;
import com.itechart.students_lab.waybill_suppliers.validation.RegexPattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {


    private final AuthorizationService authorizationService;
    private final EmailValidator validator;

    @PostMapping("/customer")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity register(@RequestBody Customer customer) {
        if (!validator.isValid(customer.getEmployees().stream().findFirst().get().getContactInformation().getEmail()
                , RegexPattern.EMAIL)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is incorrect!");
        }
        return authorizationService.addNewCustomer(customer);
    }

}
