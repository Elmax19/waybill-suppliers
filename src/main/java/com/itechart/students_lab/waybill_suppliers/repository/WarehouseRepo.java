package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    void deleteByIdIn(Collection<Long> id);
}
