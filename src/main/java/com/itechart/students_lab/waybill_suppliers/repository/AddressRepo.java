package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
    Address findByStateAndCityAndFirstAddressLineAndSecondAddressLine
            (String state, String city, String firstAddressLine, String secondAddressLine);
}
