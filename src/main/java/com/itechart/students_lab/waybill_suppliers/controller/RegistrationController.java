package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.service.AuthorizationService;
import com.itechart.students_lab.waybill_suppliers.validation.EmailValidator;
import com.itechart.students_lab.waybill_suppliers.validation.RegexPattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthorizationService authorizationService;
    private final EmailValidator validator;

    @PostMapping("/customer")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity register(@RequestBody Customer customer) {
        String customerAdminEmail = customer.getEmployees().stream().findFirst()
                .orElseThrow(() -> new BadRequestException("Error when trying to register new customer!"))
                .getContactInformation().getEmail();
        if (!validator.isValid(customerAdminEmail, RegexPattern.EMAIL)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is incorrect!");
        }
        return authorizationService.addNewCustomer(customer);
    }

    @PostMapping("customer/{customer}/employee")
    @PreAuthorize("hasAuthority('employees:write')")
    public ResponseEntity createEmployee(@RequestBody Employee employee, @PathVariable Customer customer){
        if (!validator.isValid(employee.getContactInformation().getEmail(), RegexPattern.EMAIL)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is incorrect!");
        }
        return authorizationService.addNewEmployee(employee, customer);
    }
}
