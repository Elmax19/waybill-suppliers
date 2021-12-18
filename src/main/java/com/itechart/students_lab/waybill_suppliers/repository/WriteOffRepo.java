package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WriteOffRepo extends JpaRepository<WriteOff, Long> {
    @Query(value = "select * from write_off " +
            "left join car c on c.id = write_off.car_id " +
            "left join warehouse w on w.id = write_off.warehouse_id " +
            "where w.customer_id = ?1 or c.customer_id = ?1 order by date_time", nativeQuery = true)
    List<WriteOff> findAllByWarehouseCustomerIdOrCarCustomerIdOrderByDateTime(Long customerId);

    List<WriteOff> findAllByWarehouseCustomerIdAndWarehouseId(Long customerId, Long warehouseId);

    List<WriteOff> findAllByCarCustomerIdAndCreatingUserIdOrderByDateTime(Long customerId, Long userId);
}
