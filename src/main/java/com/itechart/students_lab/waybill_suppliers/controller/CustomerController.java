package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepo customerRepo;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('customers:read')")
    public List<Customer> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int count){
        return customerRepo.findAll(PageRequest.of(page, count)).getContent();
    }

    @GetMapping("/customers/enabled")
    @PreAuthorize("hasAuthority('customers:read')")
    public List<Customer> findAllFilterOutDisabled(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int count){
        return customerRepo.findAllByActiveStatus(PageRequest.of(page, count)).getContent();
    }

    @PostMapping("/customers/disable")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity<String> disable(@RequestBody List<Customer> customers){
        for (Customer c : customers) {
            customerRepo.setCustomerActiveStatus(ActiveStatus.INACTIVE.toString(), c.getId());
        }
        return ResponseEntity.ok("Selected customers successfully disabled!");
    }

    @PostMapping("/customers/enable")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity<String> enable(@RequestBody List<Customer> customers){
        for (Customer c : customers) {
            customerRepo.setCustomerActiveStatus(ActiveStatus.ACTIVE.toString(), c.getId());
        }
        return ResponseEntity.ok("Selected customers successfully enabled!");
    }
}
