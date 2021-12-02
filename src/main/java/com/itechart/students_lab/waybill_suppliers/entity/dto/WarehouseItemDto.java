package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import lombok.Data;

@Data
public class WarehouseItemDto {
    ItemDto item;
    private int count;
    private ActiveStatus activeStatus;
}
