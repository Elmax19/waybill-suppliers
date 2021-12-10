package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CarRepo extends JpaRepository<Car, Long> {
    void deleteByIdIn(Collection<Long> ids);

    @Query("UPDATE Car c SET c.status = :status WHERE c.id IN :ids")
    void updateCarsStatus(@Param("ids") List<Long> ids, @Param("status") CarStatus status);
}
