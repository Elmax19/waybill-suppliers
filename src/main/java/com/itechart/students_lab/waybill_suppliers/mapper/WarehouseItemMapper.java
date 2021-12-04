package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.WarehouseItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseItemDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface WarehouseItemMapper {
    WarehouseItem convertToEntity(WarehouseItemDto warehouseItemDto);

    WarehouseItemDto convertToDto(WarehouseItem warehouseItem);

    List<WarehouseItemDto> map(List<WarehouseItem> warehouseItem);
}
