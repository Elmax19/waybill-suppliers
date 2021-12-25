package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    @Mapping(target = "customerId", expression = "java(warehouse.getCustomer().getId())")
    WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);

    Warehouse warehouseDtoToWarehouse(WarehouseDto warehouseDto);

    List<WarehouseDto> warehousesListToWarehousesDtoList(List<Warehouse> warehouses);
}
