package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import com.itechart.students_lab.waybill_suppliers.repository.UserRepo;
import com.itechart.students_lab.waybill_suppliers.validation.EmailValidator;
import com.itechart.students_lab.waybill_suppliers.validation.RegexPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Date;
import java.util.Set;

@Service
public class AuthorizationService {

    private final UserRepo userRepo;
    private final CustomerRepo customerRepo;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationService(UserRepo userRepo,
                                CustomerRepo customerRepo, EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity addNewCustomer(Customer customer){
        Employee adminUser = customer.getEmployees().stream().findFirst().get();
        if (!EmailValidator.isValid(adminUser.getContactInformation().getEmail(), RegexPattern.EMAIL)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email not valid!");
        }
        if (customerRepo.existsByNameIgnoreCase(customer.getName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Customer with this name already exists!"); // 409
        }
        if (customerRepo.existsByNameIgnoreCase(customer.getName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Customer with this name already exists!"); // 409
        }
        if (employeeRepo.existsByContactInformationEmailIgnoreCase(adminUser.getContactInformation().getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Customer with this email already exists!");
        }
        adminUser = getNewCustomerAdmin(adminUser);
        customer = getNewCustomer(customer);
        adminUser.setCustomer(customer);
        customer.setEmployees(Set.of(adminUser));
        customerRepo.save(customer);
        return ResponseEntity.created(URI.create("/customers"))
                    .body(customer);
    }

    public Employee getNewCustomerAdmin(Employee admin){
        admin.setLogin(admin.getContactInformation().getEmail());
        admin.setPassword(passwordEncoder.encode("test_password"));
        admin.setRole(UserRole.ROLE_ADMIN);
        admin.setActiveStatus(ActiveStatus.ACTIVE);
        return admin;
    }

    public Customer getNewCustomer(Customer customer){
        customer.getEmployees().clear();
        customer.setRegistrationDate(new Date());
        customer.setActiveStatus(ActiveStatus.ACTIVE);
        return customer;
    }
}
