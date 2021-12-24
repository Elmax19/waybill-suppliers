package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@NoArgsConstructor
public class EmployeeService {
    private static final String EMPLOYEE_WITH_ID_NOT_FOUND
            = "Employee with id %d not found";
    private static final String EMPLOYEE_CUSTOMER_DEACTIVATED
            = "Employee's customer is deactivated";

    private EmployeeRepo employeeRepo;

    @Autowired
    public void setEmployeeRepo(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Employee getActiveCustomerEmployee(Long id) {
        if (id == null) {
            return null;
        }

        Employee employee = employeeRepo.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format(EMPLOYEE_WITH_ID_NOT_FOUND, id)));
        if (employee.getCustomer().getActiveStatus() == ActiveStatus.INACTIVE) {
            throw new ServiceException(HttpStatus.CONFLICT, EMPLOYEE_CUSTOMER_DEACTIVATED);
        }
        return employee;
    }
}
