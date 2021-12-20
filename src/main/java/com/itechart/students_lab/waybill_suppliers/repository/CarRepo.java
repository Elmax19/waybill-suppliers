package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CarRepo extends JpaRepository<Car, Long> {
    void deleteByIdIn(Collection<Long> ids);

    @Modifying
    @Query(value = "update car set available_capacity=available_capacity-?2 where id=?1", nativeQuery = true)
    void updateAvailableCapacity(Long id, int places);
}
