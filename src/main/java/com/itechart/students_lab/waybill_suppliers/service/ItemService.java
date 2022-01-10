package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategoryWithItemsCount;
import com.itechart.students_lab.waybill_suppliers.repository.ItemCategoryRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;
    private final ItemCategoryRepo itemCategoryRepo;

    public List<ItemCategoryWithItemsCount> getAll(Long id, int page, int count){
        return itemRepo.findItemCategoriesAndItemsCount(id, PageRequest.of(page, count)).getContent();
    }

    public ResponseEntity editTaxRate(ItemCategory itemCategory){
        return itemCategory.getTaxRate() < 0 ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tax rate less than 0.000")
        : ResponseEntity.ok(itemCategoryRepo.save(itemCategory));
    }

    public Integer getTotalCustomerCategories(Long id) {
        return itemRepo.getTotalCustomerCategories(id);
    }
}
