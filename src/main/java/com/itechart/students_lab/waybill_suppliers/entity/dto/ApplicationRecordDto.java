package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ApplicationRecordDto {
    @NotNull(message = "Application id must be specified")
    @Min(value = 1L, message = "Application id must be positive number")
    private Long id;

    @Min(value = 1L, message = "Application sequence number must be positive")
    private Integer sequenceNumber;

    private Integer number;
    private Set<ApplicationItemDto> items;
    private Long warehouseId;
    private AddressDto destinationAddress;
    private ApplicationStatus status;
}
