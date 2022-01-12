package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import com.itechart.students_lab.waybill_suppliers.entity.WriteOffItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteOffItemRepo extends JpaRepository<WriteOffItem, Long> {
}
