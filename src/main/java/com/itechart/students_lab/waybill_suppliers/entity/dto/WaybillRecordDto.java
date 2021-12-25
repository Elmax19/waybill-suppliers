package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WaybillRecordDto {
    private Integer number;
    private WarehouseDto warehouse;
    private List<ApplicationRecordDto> applications;
    private LocalDateTime lastUpdateTime;
    private EmployeeDto lastUpdater;
    private WaybillState state;
}
