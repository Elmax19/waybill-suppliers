package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    List<Application> findAllByWarehouseCustomerId(Long customerId, Pageable pageable);

    Application findByIdAndWarehouseCustomerId(Long applicationId, Long customerId);

    List<Application> findAllByStatusAndWarehouseCustomerId(ApplicationStatus status, Long customerId, Pageable pageable);

    List<Application> findAllByOutgoingIsTrueAndWarehouseCustomerId(Long customerId, Pageable pageable);

    List<Application> findAllByOutgoingIsTrueAndStatusAndWarehouseCustomerId(ApplicationStatus status, Long customerId, Pageable pageable);
}
