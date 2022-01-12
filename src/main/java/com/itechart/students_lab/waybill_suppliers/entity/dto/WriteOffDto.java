package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class WriteOffDto {
    private Long id;
    private LocalDateTime dateTime;
    private Set<WriteOffItemDto> writeOffItems;
    private Long creatingUserId;
    private Long warehouseId;
    private Long carId;
}
