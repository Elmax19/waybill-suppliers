package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    void deleteByIdIn(Collection<Long> id);

    @Modifying
    @Query(value = "update warehouse set available_capacity=warehouse.available_capacity-?2 where id=?1", nativeQuery = true)
    void updateAvailableCapacity(Long id, int places);
}
