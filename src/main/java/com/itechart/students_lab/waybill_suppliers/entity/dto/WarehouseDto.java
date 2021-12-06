package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WarehouseDto {
    private Long id;
    @NotBlank(message = "Warehouse name must not be empty")
    private String name;
    @Valid
    @NotNull(message = "Warehouse address must not be null")
    private AddressDto address;
    @NotBlank(message = "Warehouse type must not be empty")
    private String type;
    @NotNull(message = "Warehouse total capacity must be specified")
    @Min(value = 0, message = "Warehouse total capacity must be positive number")
    private Integer totalCapacity;
    @Min(value = 0, message = "Warehouse available capacity must be positive number")
    private Integer availableCapacity;
    @NotNull(message = "Warehouse customer must not be null")
    private CustomerDto customer;
}
