package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
    @Query(value = "SELECT DISTINCT w.id, w.w_name, w.address_id, w.type, w.total_capacity, "
            + "w.available_capacity, w.customer_id "
            + "FROM warehouse w "
            + "RIGHT JOIN application a on w.id = a.warehouse_id "
            + "WHERE w.customer_id = :id AND a.status = :status AND a.is_outgoing = 1 "
            + "LIMIT :offset, :size",
            nativeQuery = true)
    List<Warehouse> findByPageAndContainingOutApplicationStatus(@Param("offset") int offset,
                                                                @Param("size") int size,
                                                                @Param("id") Long customerId,
                                                                @Param("status") String applicationStatus);

    @Modifying
    @Query("DELETE FROM Warehouse w WHERE w.id IN :ids AND w.id NOT IN "
            + "(SELECT DISTINCT wi.warehouse.id FROM WarehouseItem wi WHERE wi.count <> 0)")
    int deleteEmptyByIdIn(@Param("ids") Collection<Long> ids);

    @Modifying
    @Query(value = "update warehouse set available_capacity=warehouse.available_capacity-?2 where id=?1", nativeQuery = true)
    void updateAvailableCapacity(Long id, int places);
}
