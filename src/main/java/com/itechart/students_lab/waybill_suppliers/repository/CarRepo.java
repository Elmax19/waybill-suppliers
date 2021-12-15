package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CarRepo extends JpaRepository<Car, Long> {
    void deleteByIdIn(Collection<Long> ids);
}
