package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ApplicationDto {
    private int number;
    private Long warehouseId;
    private AddressDto destinationAddress;
    private LocalDateTime registrationDateTime;
    private LocalDateTime lastUpdateDateTime;
    private Long creatingUserId;
    private Long updatingUserId;
    private ApplicationStatus status;
    private boolean outgoing;
    private Set<ApplicationItemDto> items;
}
