package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import com.itechart.students_lab.waybill_suppliers.utils.MailService;
import com.itechart.students_lab.waybill_suppliers.utils.PasswordGenerator;
import com.itechart.students_lab.waybill_suppliers.validation.RegexPattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final EmployeeRepo employeeRepo;
    private final MailService mailService;

    @Transactional
    public ResponseEntity addNewCustomer(Customer customer) {
        Employee adminUser = customer.getEmployees().stream().findFirst().get();
        String password = passwordGenerator.generateRandomSpecialCharacters(15);
        adminUser = getNewCustomerAdmin(adminUser, password);
        customer = getNewCustomer(customer);
        adminUser.setCustomer(customer);
        customer.setEmployees(Set.of(adminUser));
        customer = customerRepo.save(customer);
        mailService.send(adminUser.getContactInformation().getEmail(),
                String.format(RegexPattern.EMAIL_TITLE, adminUser.getLogin()),
                String.format(RegexPattern.EMAIL_MESSAGE, password));
        return ResponseEntity.created(URI.create("/customers"))
                .body(customer);
    }

    @Transactional
    public ResponseEntity addNewEmployee(Employee employee, Customer customer) {
        String password = passwordGenerator.generateRandomSpecialCharacters(15);
        employee.setActiveStatus(ActiveStatus.ACTIVE);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setCustomer(customer);
        employee = employeeRepo.save(employee);
        mailService.send(employee.getContactInformation().getEmail(),
                String.format(RegexPattern.EMAIL_TITLE, customer.getName()),
                String.format(RegexPattern.EMAIL_MESSAGE, password));
        return ResponseEntity.created(URI.create("/customer/" + customer.getId() + "/employees"))
                .body(employee);
    }

    public Employee getNewCustomerAdmin(Employee admin, String password) {
        admin.setLogin(admin.getContactInformation().getEmail());
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(UserRole.ROLE_ADMIN);
        admin.setActiveStatus(ActiveStatus.ACTIVE);
        return admin;
    }

    public Customer getNewCustomer(Customer customer) {
        customer.getEmployees().clear();
        customer.setRegistrationDate(new Date());
        customer.setActiveStatus(ActiveStatus.ACTIVE);
        return customer;
    }
}
