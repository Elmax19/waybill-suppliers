package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    @Modifying
    @Query("DELETE FROM Warehouse w WHERE w.id IN :ids AND w.id NOT IN "
            + "(SELECT DISTINCT wi.warehouse.id FROM WarehouseItem wi WHERE wi.count <> 0)")
    int deleteEmptyByIdIn(@Param("ids") Collection<Long> ids);
    @Modifying
    @Query(value = "update warehouse set available_capacity=warehouse.available_capacity-?2 where id=?1", nativeQuery = true)
    void updateAvailableCapacity(Long id, int places);

    @Query("SELECT w FROM Warehouse w WHERE w.address.id IN "
            + "(SELECT a.id FROM Address a "
            + "WHERE a.state = :state "
            + "AND a.city = :city "
            + "AND a.firstAddressLine = :addressLine1 "
            + "AND a.secondAddressLine = :addressLine2)")
    List<Warehouse> findByAddress(@Param("state") String state,
                                  @Param("city") String city,
                                  @Param("addressLine1") String addressLine1,
                                  @Param("addressLine2") String addressLine2);
}
