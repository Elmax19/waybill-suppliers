package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WriteOffRepo extends JpaRepository<WriteOff, Long> {
    List<WriteOff> findAllByWarehouseCustomerIdOrderByDateTime(Long customerId);

    List<WriteOff> findAllByWarehouseCustomerIdAndWarehouseId(Long customerId, Long warehouseId);

    List<WriteOff> findAllByWarehouseCustomerIdAndCreatingUserIdOrderByDateTime(Long customerId, Long userId);
}
