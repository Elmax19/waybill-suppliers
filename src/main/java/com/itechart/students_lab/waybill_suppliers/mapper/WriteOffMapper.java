package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import com.itechart.students_lab.waybill_suppliers.entity.WriteOffItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WriteOffDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WriteOffItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface WriteOffMapper {
    @Mapping(source = "writeOff.creatingUser.id", target = "creatingUserId")
    @Mapping(source = "writeOff.warehouse.id", target = "warehouseId")
    @Mapping(source = "writeOff.car.id", target = "carId")
    WriteOffDto convertToDto(WriteOff writeOff);

    WriteOffItemDto convertToDto(WriteOffItem writeOffItem);

    List<WriteOffDto> map(List<WriteOff> writeOffList);

    @Mapping(source = "writeOffDto.creatingUserId", target = "creatingUser.id")
    @Mapping(source = "writeOffDto.warehouseId", target = "warehouse.id")
    @Mapping(source = "writeOffDto.carId", target = "car.id")
    WriteOff convertToEntity(WriteOffDto writeOffDto);

    WriteOffItem convertToEntity(WriteOffItemDto writeOffItemDto);

    Item convertToEntity(ItemDto itemDto);

    ItemDto convertToDto(Item item);
}
