package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

@Data
public class ApplicationItemDto {
    private Long id;
    private ItemDto item;
    private int count;
    private double price;
    private int placedCount;
}
