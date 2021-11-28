package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {

}
