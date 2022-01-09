package com.itechart.students_lab.waybill_suppliers.repository;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategoryWithItemsCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findAllByIdIn(List<Long> idList);

    List<Item> findAllByCustomerId(Long id, Pageable pageable);

    List<Item> findAllByCustomerId(Long id);

    Item findByCustomerIdAndId(Long customerId, Long id);

    @Transactional
    void deleteByCustomerIdAndId(Long customerId, Long id);

    @Query("select new com.itechart.students_lab.waybill_suppliers.entity.ItemCategoryWithItemsCount(it.itemCategory," +
            " count(it.itemCategory.id)) from Item as it where it.customer.id=:id group by it.itemCategory")
    Page<ItemCategoryWithItemsCount> findItemCategoriesAndItemsCount(@Param("id") Long id, Pageable pageable);

    @Query("select it.itemCategory.id from Item as it where it.customer.id=:id group by it.itemCategory.id")
    List<Integer> getTotalCustomerCategories(@Param("id") Long id);
}
