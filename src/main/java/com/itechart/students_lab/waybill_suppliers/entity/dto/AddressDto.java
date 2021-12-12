package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressDto {
    @NotBlank(message = "Address state must not be empty")
    private String state;

    @NotBlank(message = "Address city must not be empty")
    private String city;

    @NotBlank(message = "Address line 1 must not be empty")
    private String firstAddressLine;

    @NotBlank(message = "Address line 2 must not be empty")
    private String secondAddressLine;
}
