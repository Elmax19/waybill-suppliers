package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepo employeeRepo;

    @GetMapping("/customer/{id}/employees")
    @PreAuthorize("hasAuthority('employees:read')")
    public List<Employee> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int count,
                                  @PathVariable Long id){
        return employeeRepo.findAllByCustomerId(id, PageRequest.of(page, count)).getContent();
    }

    @GetMapping("/customer/{id}/employees/enabled")
    @PreAuthorize("hasAuthority('employees:read')")
    public List<Employee> findAllFilterOutDisabled(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int count,
                                                   @PathVariable Long id){
        return employeeRepo.findAllByActiveStatus(id, PageRequest.of(page, count)).getContent();
    }

    @PostMapping("/employees/disable")
    @PreAuthorize("hasAuthority('employees:write')")
    public ResponseEntity<String> disable(@RequestBody List<Employee> employees){
        List<Long> ids = employees.stream().map(e -> e.getId()).collect(Collectors.toList());
        employeeRepo.setEmployeeActiveStatus(ActiveStatus.INACTIVE.toString(), ids);
        return ResponseEntity.ok("Selected employees successfully disabled!");
    }

    @PostMapping("/employees/enable")
    @PreAuthorize("hasAuthority('employees:write')")
    public ResponseEntity<String> enable(@RequestBody List<Employee> employees){
        List<Long> ids = employees.stream().map(e -> e.getId()).collect(Collectors.toList());
        employeeRepo.setEmployeeActiveStatus(ActiveStatus.ACTIVE.toString(), ids);
        return ResponseEntity.ok("Selected employees successfully enabled!");
    }

}
