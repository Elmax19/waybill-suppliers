package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WriteOffRepo extends JpaRepository<WriteOff, Long> {
    @Query(value = "select count(write_off.id) from write_off " +
            "left join car c on c.id = write_off.car_id " +
            "left join warehouse w on w.id = write_off.warehouse_id " +
            "where w.customer_id = ?1 or c.customer_id = ?1 order by date_time", nativeQuery = true)
    Integer countByWarehouseCustomerIdOrCarCustomerIdOrderByDateTime(Long customerId);

    Integer countByWarehouseCustomerIdAndWarehouseId(Long customerId, Long warehouseId);

    Integer countByCarCustomerIdAndCreatingUserIdOrderByDateTime(Long customerId, Long userId);

    @Query(value = "select * from write_off " +
            "left join car c on c.id = write_off.car_id " +
            "left join warehouse w on w.id = write_off.warehouse_id " +
            "where w.customer_id = ?1 or c.customer_id = ?1 order by date_time", nativeQuery = true)
    List<WriteOff> findAllByWarehouseCustomerIdOrCarCustomerIdOrderByDateTime(Long customerId, Pageable pageable);

    List<WriteOff> findAllByWarehouseCustomerIdAndWarehouseId(Long customerId, Long warehouseId, Pageable pageable);

    List<WriteOff> findAllByCarCustomerIdAndCreatingUserIdOrderByDateTime(Long customerId, Long userId, Pageable pageable);

    @Modifying
    @Query(value = "insert into write_off(id, date_time, creating_user_id, warehouse_id, car_id) value (?1,?2,?3,?4,?5)", nativeQuery = true)
    void create(Long id, LocalDateTime dateTime, Long creatingUserId, Long warehouseId, Long carId);

    @Query(value = "select max(id) from write_off", nativeQuery = true)
    Long getMaxId();
}
