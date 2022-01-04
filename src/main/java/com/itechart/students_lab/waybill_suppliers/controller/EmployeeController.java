package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.dto.EmployeeDto;
import com.itechart.students_lab.waybill_suppliers.exception.AccountNotMatchException;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.mapper.EmployeeMapper;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins={ "http://localhost:3000" })
@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/customer/{id}/employees")
    @PreAuthorize("hasAuthority('employees:read')")
    public List<Employee> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int count,
                                  @PathVariable Long id) {
        return employeeRepo.findAllByCustomerId(id, PageRequest.of(page, count)).getContent();
    }

    @GetMapping("/customer/{id}/employees/total")
    @PreAuthorize("hasAuthority('employees:read')")
    public Integer getTotal(@PathVariable Long id) {
        return employeeRepo.countByCustomerId(id);
    }

    @GetMapping("/employee/{employeeName}/customer")
    public Customer getCustomer(@PathVariable String employeeName){
        Employee employee = employeeRepo.findByLogin(employeeName);
        return employee.getCustomer();
    }

    @GetMapping("/customer/{id}/employees/enabled")
    @PreAuthorize("hasAuthority('employees:read')")
    public List<Employee> findAllFilterOutDisabled(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int count,
                                                   @PathVariable Long id) {
        return employeeRepo.findAllByActiveStatus(id, PageRequest.of(page, count)).getContent();
    }

    @PostMapping("/employees/disable")
    @PreAuthorize("hasAuthority('employees:write')")
    public ResponseEntity<String> disable(@RequestBody List<Employee> employees) {
        List<Long> ids = employees.stream().map(e -> e.getId()).collect(Collectors.toList());
        employeeRepo.setEmployeeActiveStatus(ActiveStatus.INACTIVE.toString(), ids);
        return ResponseEntity.ok("Selected employees successfully disabled!");
    }

    @PostMapping("/employees/enable")
    @PreAuthorize("hasAuthority('employees:write')")
    public ResponseEntity<String> enable(@RequestBody List<Employee> employees) {
        List<Long> ids = employees.stream().map(e -> e.getId()).collect(Collectors.toList());
        employeeRepo.setEmployeeActiveStatus(ActiveStatus.ACTIVE.toString(), ids);
        return ResponseEntity.ok("Selected employees successfully enabled!");
    }

    @GetMapping("/employee/{employee}")
    @PreAuthorize("hasAuthority('account:read')")
    public Employee getEmployee(@PathVariable Employee employee,
                                @AuthenticationPrincipal UserDetails user) {
        if (!user.getUsername().equals(employee.getLogin())) {
            throw new AccountNotMatchException("Unauthorized request");
        }
        return employee;
    }

    @PutMapping("/employee")
    @PreAuthorize("hasAuthority('account:write')")
    public ResponseEntity updateEmployee(@RequestBody Employee employee,
                                         @RequestParam(defaultValue = "false") boolean isPasswordChanged) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee = employeeRepo.save(employee);
        if (isPasswordChanged) {
            return ResponseEntity.ok(URI.create("/logout"));
        }
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/customer/{id}/free-drivers")
    @PreAuthorize("hasAuthority('employees:read')")
    public ResponseEntity<List<EmployeeDto>> findFreeDrivers(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        if (page < 0) {
            throw new BadRequestException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new BadRequestException("Page size must not be less than one!");
        }
        int offset = page * size;
        List<EmployeeDto> drivers = employeeMapper.employeeListToEmployeeRecordDtoList(
                employeeRepo.findFreeDrivers(id, offset, size));
        return drivers.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(drivers, HttpStatus.OK);
    }
}
