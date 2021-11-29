package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import com.itechart.students_lab.waybill_suppliers.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorizationService {

    private final UserRepo userRepo;
    private final Validator validator;
    private final CustomerRepo customerRepo;

    public AuthorizationService(UserRepo userRepo, Validator validator,
                                CustomerRepo customerRepo) {
        this.userRepo = userRepo;
        this.validator = validator;
        this.customerRepo = customerRepo;
    }

    public String addNewCustomer(Customer customer){

        customer.setActiveStatus(ActiveStatus.ACTIVE);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        customer.setRegistrationDate(new Date());
        Set<Employee> employees = customer.getEmployees();
        Optional<Employee> first = employees.stream().filter(p -> !p.getContactInformation().getEmail().isEmpty()).findFirst();
        if (first.isPresent()){
            first.get().setActiveStatus(ActiveStatus.ACTIVE);
            first.get().setRole(UserRole.ROLE_ADMIN);
            first.get().setPassword("customer_admin_password");
            first.get().setLogin("customer_admin_login");
        }
        customerRepo.save(customer);
        return "save";
    }
}
