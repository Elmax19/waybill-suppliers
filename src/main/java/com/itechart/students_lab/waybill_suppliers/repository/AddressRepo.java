package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AddressRepo extends JpaRepository<Address, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into address(id, state, city, address_line_1, address_line_2) value (?1, ?2, ?3, ?4, ?5) on duplicate key update state=?2, city=?3, address_line_1=?4, address_line_2=?5", nativeQuery = true)
    void saveOrUpdate(Long id, String state, String city, String addressLine1, String addressLine2);
}
