package com.itechart.students_lab.waybill_suppliers.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ContactInformationRecordDto {
    private String name;
    private String surname;
    private Date birthday;
    private String email;
    private AddressDto address;
}
