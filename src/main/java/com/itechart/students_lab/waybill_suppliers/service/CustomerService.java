package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final String FAILED_GET_CUSTOMER_DEACTIVATED = "Customer with id %d deactivated";
    private static final String CUSTOMER_WITH_ID_NOT_FOUND = "Customer with id %d not found";

    private final CustomerRepo customerRepo;

    public Customer getActiveCustomer(Long id) {
        Customer customer = customerRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(CUSTOMER_WITH_ID_NOT_FOUND, id)));
        if (customer.getActiveStatus() == ActiveStatus.INACTIVE) {
            throw new ServiceException(HttpStatus.CONFLICT,
                    String.format(FAILED_GET_CUSTOMER_DEACTIVATED, id));
        }
        return customer;
    }
}
