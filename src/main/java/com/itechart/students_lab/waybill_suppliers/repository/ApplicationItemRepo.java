package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.ApplicationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationItemRepo extends JpaRepository<ApplicationItem, Long> {
    @Modifying
    @Query(value = "update application_item set placed_count=placed_count+?2 where id=?1", nativeQuery = true)
    void updateItemPlacedCount(Long id, int placedCount);
}
