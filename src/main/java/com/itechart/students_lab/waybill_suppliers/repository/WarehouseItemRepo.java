package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.WarehouseItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WarehouseItemRepo extends JpaRepository<WarehouseItem, Long> {
    @Transactional
    @Modifying
    @Query(value = "update warehouse_item set count=?1, is_active=?2 where id=?3", nativeQuery = true)
    void updateWarehouseItem(int count, String status, Long warehouseItemId);

    @Query(value = "select * from warehouse_item where item_id=?1 and warehouse_id=?2", nativeQuery = true)
    WarehouseItem findByWarehouseAndItem(Long itemId, Long warehouseId);

    boolean existsByWarehouseIdAndItemId(Long warehouseId, Long itemId);

    @Transactional
    @Modifying
    @Query(value = "insert into warehouse_item(warehouse_id, item_id, count, is_active) value (?1, ?2, ?3,'ACTIVE')", nativeQuery = true)
    void saveWarehouseItem(Long warehouseId, Long itemId, int count);

    List<WarehouseItem> findAllByWarehouseId(Long id, Pageable pageable);
}
