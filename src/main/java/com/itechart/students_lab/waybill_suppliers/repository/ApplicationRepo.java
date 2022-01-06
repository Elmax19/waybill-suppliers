package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    List<Application> findAllByWarehouseCustomerIdAndOutgoingIsTrue(Long customerId, Pageable pageable);

    Integer countByWarehouseCustomerIdAndOutgoingIsTrue(Long customerId);

    List<Application> findAllByWarehouseId(Long warehouseId, Pageable pageable);

    List<Application> findAllByWarehouseIdAndOutgoing(Long warehouseId, boolean outgoing, Pageable pageable);

    Integer countByWarehouseId(Long warehouseId);

    Integer countByWarehouseIdAndOutgoing(Long warehouseId, boolean outgoing);

    Application findByNumberAndWarehouseCustomerId(int number, Long customerId);

    List<Application> findAllByStatusAndWarehouseCustomerId(ApplicationStatus status, Long customerId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update application join warehouse w on w.id = application.warehouse_id " +
            "join customer c on c.id = w.customer_id " +
            "set status=?1, updating_user_id=?2, last_update_date_time=now() where customer_id=?3 and number=?4", nativeQuery = true)
    void updateStatusByCustomerIdAndNumber(String status, Long updatingUserId, Long customerId, int number);

    @Modifying
    @Query("UPDATE Application a SET a.waybill.id = :wbId WHERE a.id IN :appIds")
    void setApplicationsWaybillId(@Param("wbId") Long waybillId,
                                  @Param("appIds") List<Long> applicationIds);

    @Modifying
    @Query("UPDATE Application a "
            + "SET a.waybill.id = NULL, a.sequenceNumber = NULL "
            + "WHERE a.id IN :appIds")
    void clearApplicationsWaybillInfo(@Param("appIds") List<Long> applicationIds);

    @Modifying
    @Query("UPDATE Application a " +
            "SET a.waybill.id = NULL, a.sequenceNumber = NULL "
            + "WHERE a.waybill.id = :wbId AND a.warehouse.id IN ("
            + "SELECT w.id "
            + "FROM Warehouse w "
            + "WHERE w.customer.id = :cId)")
    void clearApplicationsWaybillInfo(@Param("cId") Long customerId,
                                      @Param("wbId") Long waybillId);
}
