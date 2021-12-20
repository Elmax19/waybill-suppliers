package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findAllByIdIn(List<Long> idList);

    List<Item> findAllByCustomerId(Long id, Pageable pageable);

    Item findByCustomerIdAndId(Long customerId, Long id);

    @Transactional
    void deleteByCustomerIdAndId(Long customerId, Long id);
}
