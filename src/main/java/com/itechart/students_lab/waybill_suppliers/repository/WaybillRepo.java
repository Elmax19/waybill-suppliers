package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaybillRepo extends JpaRepository<Waybill, Long> {

}
