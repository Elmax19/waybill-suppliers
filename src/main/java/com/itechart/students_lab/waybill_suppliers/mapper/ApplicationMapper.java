package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.AddressDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationRecordDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.EmployeeDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    @Mapping(source = "application.items", target = "items")
    ApplicationDto convertToDto(Application application);

    @Mapping(source = "applicationDto.items", target = "items")
    Application convertToEntity(ApplicationDto applicationDto);

    List<ApplicationDto> map(List<Application> applications);

    List<ApplicationRecordDto> applicationListToApplicationRecordDtoList(
            List<Application> applications);

    @Mapping(target = "warehouseId", expression = "java(application.getWarehouse().getId())")
    ApplicationRecordDto applicationToApplicationRecordDto(Application application);

    Address addressDtoToAddress(AddressDto addressDto);

    @Mapping(target = "customerId", expression = "java(warehouse.getCustomer().getId())")
    WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);

    Warehouse warehouseDtoToWarehouse(WarehouseDto warehouseDto);

    EmployeeDto employeeToEmployeeDto(Employee employeeDto);

    Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
