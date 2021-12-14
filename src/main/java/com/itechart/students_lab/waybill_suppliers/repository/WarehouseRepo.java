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
}
