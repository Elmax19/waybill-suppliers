package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.WarehouseDispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseDispatcherRepo extends JpaRepository<WarehouseDispatcher, Long> {
    boolean existsByWarehouseIdAndDispatcherLogin(Long warehouseId, String dispatcherLogin);
}
