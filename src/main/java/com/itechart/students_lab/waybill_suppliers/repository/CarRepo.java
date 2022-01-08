package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CarRepo extends JpaRepository<Car, Long> {
    Page<Car> findAllByTotalCapacityGreaterThanEqualAndCustomer_Id(
            Integer minCapacity, Long customer_id, Pageable pageable);

    void deleteByIdIn(Collection<Long> ids);

    @Modifying
    @Query(value = "update car set available_capacity=available_capacity-?2 where id=?1", nativeQuery = true)
    void updateAvailableCapacity(Long id, int places);

    List<Car> findAllByCustomerId(Long id);
}
