package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    @Query("SELECT w FROM Warehouse w WHERE (w.address.state = :addressState "
            + "AND w.address.city = :addressCity "
            + "AND w.address.firstAddressLine = :addressLine1 "
            + "AND w.address.secondAddressLine = :addressLine2) "
            + "OR (w.name = :whName AND w.customer.name = :whCustomer)")
    List<Warehouse> findByAddressOrCustomerAndName(@Param("addressState") String addressState,
                                                   @Param("addressCity") String addressCity,
                                                   @Param("addressLine1") String addressLine1,
                                                   @Param("addressLine2") String addressLine2,
                                                   @Param("whName") String warehouseName,
                                                   @Param("whCustomer") String customerName);

    void deleteByIdIn(Collection<Long> id);
}
