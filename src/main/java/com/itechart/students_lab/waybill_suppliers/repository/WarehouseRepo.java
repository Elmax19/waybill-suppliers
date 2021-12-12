package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Collection;
import java.util.List;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    void deleteByIdIn(Collection<Long> id);

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
