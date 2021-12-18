package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOffReason;
import lombok.Data;

@Data
public class WriteOffItemDto {
    private Long id;
    private Long itemId;
    private int amount;
    private WriteOffReason reason;
}
