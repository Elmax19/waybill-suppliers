package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

@Data
public class WarehouseRequestDto {
    private String name;
    private AddressDto address;
    private String type;
    private int totalCapacity;
    private CustomerDto customer;
}
