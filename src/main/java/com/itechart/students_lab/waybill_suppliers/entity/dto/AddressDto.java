package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String state;
    private String city;
    private String addressLine1;
    private String addressLine2;
}
