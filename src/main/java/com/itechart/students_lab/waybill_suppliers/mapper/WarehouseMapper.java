package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CustomerDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface WarehouseMapper extends AddressMapper {
    WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);

    Warehouse warehouseDtoToWarehouse(WarehouseDto warehouseDto);

    List<WarehouseDto> warehousesListToWarehousesDtoList(List<Warehouse> warehouses);

    @Mapping(target = "employees", ignore = true)
    CustomerDto customerToCustomerDto(Customer customer);

    @Mapping(target = "employees", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
