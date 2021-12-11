package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final String FAILED_GET_CUSTOMER_DEACTIVATED = "Customer with id %d deactivated";
    private static final String CUSTOMER_WITH_ID_NOT_FOUND = "Customer with id %d not found";

    private final CustomerRepo customerRepo;

    public Customer getActiveCustomer(Long id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            if (customer.get().getActiveStatus() == ActiveStatus.INACTIVE) {
                throw new ServiceException(HttpStatus.CONFLICT,
                        String.format(FAILED_GET_CUSTOMER_DEACTIVATED, id));
            }
            return customer.get();
        } else {
            throw new EntityNotFoundException(
                    String.format(CUSTOMER_WITH_ID_NOT_FOUND, id));
        }
    }
}
