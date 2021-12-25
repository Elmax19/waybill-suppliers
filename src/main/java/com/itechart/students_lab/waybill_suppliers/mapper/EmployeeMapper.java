package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.dto.EmployeeDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    List<EmployeeDto> employeeListToEmployeeRecordDtoList(List<Employee> employees);
}
