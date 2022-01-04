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
import java.util.stream.Collectors;

@CrossOrigin(origins={ "http://localhost:3000" })
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

    @GetMapping("/customers/total")
    @PreAuthorize("hasAuthority('customers:read')")
    public Integer getTotal(){
        return customerRepo.findAll().size();
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
        List<Long> ids = customers.stream().map(c -> c.getId()).collect(Collectors.toList());
        customerRepo.setCustomerActiveStatus(ActiveStatus.INACTIVE.toString(), ids);
        return ResponseEntity.ok("Selected customers successfully disabled!");
    }

    @PostMapping("/customers/enable")
    @PreAuthorize("hasAuthority('customers:write')")
    public ResponseEntity<String> enable(@RequestBody List<Customer> customers){
        List<Long> ids = customers.stream().map(c -> c.getId()).collect(Collectors.toList());
        customerRepo.setCustomerActiveStatus(ActiveStatus.ACTIVE.toString(), ids);
        return ResponseEntity.ok("Selected customers successfully enabled!");
    }
}
