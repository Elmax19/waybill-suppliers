package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.UserRole;
import lombok.Data;

@Data
public class EmployeeDto {
    private Integer id;
    private UserRole role;
    private ContactInformationRecordDto contactInformation;
}
