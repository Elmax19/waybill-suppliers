package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class WaybillEditDto {
    @Min(value = 1L, message = "Waybill id must be positive number")
    private Long id;

    @NotNull(message = "Waybill number must be specified")
    private String number;

    @NotNull(message = "Warehouse id must be specified")
    @Min(value = 1L, message = "Warehouse id must be positive number")
    private Long warehouseId;

    @NotNull(message = "Route applications list must be specified")
    @Size(min = 1, message = "At least one route point must be specified")
    private List<ApplicationRecordDto> applicationRecords;

    @NotNull(message = "Car id must be specified")
    @Min(value = 1L, message = "Car id must be positive number")
    private Long carId;

    @Min(value = 1L, message = "Creator id must be positive number")
    private Long creatorId;

    @Min(value = 1L, message = "Updater id must be positive number")
    private Long lastUpdaterId;

    @Min(value = 1L, message = "Driver id must be positive number")
    private Long driverId;

    @NotNull(message = "Waybill state must be specified")
    private WaybillState state;
}
