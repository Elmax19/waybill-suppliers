package com.itechart.students_lab.waybill_suppliers.entity.dto;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private Date registrationDate;
    private ActiveStatus activeStatus;
    private Set<EmployeeDto> employees;
}
