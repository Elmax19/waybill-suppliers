package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ItemCategoryWithItemsCount;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;

    public List<ItemCategoryWithItemsCount> getAll(Long id, int page, int count){
        return itemRepo.findItemCategoriesAndItemsCount(id, PageRequest.of(page, count)).getContent();
    }
}
