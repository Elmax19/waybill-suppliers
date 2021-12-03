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
    @Query(value = "update warehouse_item set is_active=?3 where warehouse_id=?1 and item_id=?2", nativeQuery = true)
    void updateWarehouseItemStatus(Long warehouseId, Long itemId, String status);

    @Transactional
    @Modifying
    @Query(value = "update warehouse_item set count=count+?3 where warehouse_id=?1 and item_id=?2", nativeQuery = true)
    void updateWarehouseItemCount(Long warehouseId, Long itemId, int count);

    @Transactional
    @Modifying
    @Query(value = "insert into warehouse_item(warehouse_id, item_id, count, is_active) VALUE (?1, ?2, ?3, ?4)", nativeQuery = true)
    void save(Long warehouseId, Long itemId, int count, String status);


    boolean existsByWarehouseIdAndItemId(Long warehouseId, Long itemId);

    List<WarehouseItem> findAllByWarehouseId(Long id, Pageable pageable);
}
