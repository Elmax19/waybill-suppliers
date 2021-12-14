package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.repository.AddressRepo;
import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private static final String DUPLICATE_ADDRESS = "Address with the same details exists";

    private final AddressRepo addressRepo;

    public Optional<String> processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndKey = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("address".equals(tableAndKey[0])) {
                return Optional.of(DUPLICATE_ADDRESS);
            }
        }
        return Optional.empty();
    }

    public Optional<Address> findByAddress(Address address) {
        return addressRepo.findByStateAndCityAndFirstAddressLineAndSecondAddressLine(
                address.getState(),
                address.getCity(),
                address.getFirstAddressLine(),
                address.getSecondAddressLine()
        );
    }
}
