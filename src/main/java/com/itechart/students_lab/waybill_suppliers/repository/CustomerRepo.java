package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c")
    Page<Customer> findAll(Pageable pageable);
    @Query("select c from Customer c where c.activeStatus='ACTIVE'")
    Page<Customer> findAllByActiveStatus(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update customer, user" +
            " set customer.is_active = :status," +
            " user.is_active = :status" +
            " where user.customer_id = customer.id" +
            " and customer.id IN :ids", nativeQuery = true)
    void setCustomerActiveStatus(@Param("status") String newStatus,
                                      @Param("ids") List<Long> id);
}
