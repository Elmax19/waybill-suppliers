package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface WarehouseMapper extends AddressMapper {
    WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);

    Warehouse warehouseDtoToWarehouse(WarehouseDto warehouseDto);

    List<WarehouseDto> warehousesListToWarehousesDtoList(List<Warehouse> warehouses);
}
