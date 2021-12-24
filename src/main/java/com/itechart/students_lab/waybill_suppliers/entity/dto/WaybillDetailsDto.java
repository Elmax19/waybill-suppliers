package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WaybillDetailsDto {
    private Long id;
    private Integer number;
    private WarehouseDto warehouse;
    private List<ApplicationRecordDto> applicationRecords;
    private CarDto car;
    private EmployeeDto creator;
    private EmployeeDto lastUpdater;
    private EmployeeDto driver;
    private LocalDateTime registrationTime;
    private LocalDateTime lastUpdateTime;
    private WaybillState state;
}
