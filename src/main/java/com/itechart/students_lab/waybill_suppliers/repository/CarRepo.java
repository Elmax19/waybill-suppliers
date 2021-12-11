package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface CarRepo extends JpaRepository<Car, Long> {
    void deleteByIdIn(Collection<Long> ids);

    @Modifying
    @Query("UPDATE Car c SET c.status = :status WHERE c.id = :id")
    void updateCarStatus(@Param("id") Long id, @Param("status") CarStatus status);

    @Modifying
    @Query("UPDATE Address a SET a.state = :state, a.city = :city, "
            + "a.firstAddressLine = :addressLine1, a.secondAddressLine = :addressLine2 "
            + "WHERE a.id = (SELECT c.lastAddress.id FROM Car c WHERE c.id = :id)")
    void updateCarLastAddress(@Param("id") Long carId,
                              @Param("state") String state,
                              @Param("city") String city,
                              @Param("addressLine1") String addressLine1,
                              @Param("addressLine2") String addressLine2);
}
