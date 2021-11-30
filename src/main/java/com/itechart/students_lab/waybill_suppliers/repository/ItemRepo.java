package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    @Query(value = "select * from item join item_category ic on ic.id = item.category_id LIMIT ?1, ?2", nativeQuery = true)
    List<Item> findByPage(int startIndex, int count);
}
