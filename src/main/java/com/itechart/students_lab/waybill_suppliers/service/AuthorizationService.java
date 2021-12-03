package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import com.itechart.students_lab.waybill_suppliers.utils.PasswordGenerator;
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

    @Transactional
    public ResponseEntity addNewCustomer(Customer customer) {
        Employee adminUser = customer.getEmployees().stream().findFirst().get();
        adminUser = getNewCustomerAdmin(adminUser);
        customer = getNewCustomer(customer);
        adminUser.setCustomer(customer);
        customer.setEmployees(Set.of(adminUser));
        customerRepo.save(customer);
        return ResponseEntity.created(URI.create("/customers"))
                .body(customer);
    }

    public Employee getNewCustomerAdmin(Employee admin) {
        admin.setLogin(admin.getContactInformation().getEmail());
        admin.setPassword(passwordEncoder.encode(passwordGenerator.generateRandomSpecialCharacters(15)));
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
