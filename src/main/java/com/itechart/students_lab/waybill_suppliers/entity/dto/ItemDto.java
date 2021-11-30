package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Long upc;
    private String label;
    private ItemCategoryDto itemCategory;
    private int units;
    private double price;
}
