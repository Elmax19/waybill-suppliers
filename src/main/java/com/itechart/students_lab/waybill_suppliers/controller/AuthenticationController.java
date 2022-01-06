package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.User;
import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import com.itechart.students_lab.waybill_suppliers.repository.UserRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseDispatcherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final WarehouseDispatcherRepo warehouseDispatcherRepo;

    @GetMapping("/user/{name}")
    public ResponseEntity<? extends User> getAuthUser(@PathVariable String name) {
        User systemAdmin = userRepo.findByLogin(name).get();
        if (!systemAdmin.getRole().equals(UserRole.ROLE_SYSTEM_ADMIN)) {
            Employee employee = employeeRepo.getById(systemAdmin.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Customer-Id", String.valueOf(employee.getCustomer().getId()));
            if (systemAdmin.getRole().equals(UserRole.ROLE_DISPATCHER)) {
                headers.add("Warehouse-Id",
                        String.valueOf(warehouseDispatcherRepo.findByDispatcherId(employee.getId()).getWarehouse().getId()));
            }
            headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(employee);
        }
        return ResponseEntity.ok(systemAdmin);
    }
}
