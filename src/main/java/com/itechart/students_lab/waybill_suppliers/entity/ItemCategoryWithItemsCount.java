package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class ItemCategoryWithItemsCount {

    private ItemCategory itemCategory;
    private Long totalItems;

    public ItemCategoryWithItemsCount(ItemCategory itemCategory, Long totalItems) {
        this.itemCategory = itemCategory;
        this.totalItems = totalItems;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }
}
