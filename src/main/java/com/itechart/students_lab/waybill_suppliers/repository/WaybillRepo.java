package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Waybill;
import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface WaybillRepo extends JpaRepository<Waybill, Long> {
    void deleteByIdAndWarehouseCustomerId(Long id, Long customerId);

    Page<Waybill> findByCreatorIdAndWarehouseCustomerIdAndStateIn(Long creatorId,
                                                                  Long customerId,
                                                                  Collection<WaybillState> states,
                                                                  Pageable pageable);

    Page<Waybill> findByWarehouseCustomerIdAndStateIn(Long customerId,
                                                      Collection<WaybillState> states,
                                                      Pageable pageable);

    Long countByCreatorIdAndWarehouseCustomerIdAndStateIn(Long creatorId,
                                                          Long customerId,
                                                          Collection<WaybillState> states);

    Long countByWarehouseCustomerIdAndStateIn(Long customerId,
                                              Collection<WaybillState> states);
}
