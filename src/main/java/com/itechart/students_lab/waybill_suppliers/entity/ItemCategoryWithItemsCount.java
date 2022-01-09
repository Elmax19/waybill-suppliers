package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemCategoryWithItemsCount {

    private ItemCategory itemCategory;
    private Long totalItems;

}
