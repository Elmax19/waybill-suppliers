package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import com.itechart.students_lab.waybill_suppliers.entity.WriteOffItem;
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

    @Mapping(source = "writeOffItem.item.id", target = "itemId")
    WriteOffItemDto convertToDto(WriteOffItem writeOffItem);

    List<WriteOffDto> map(List<WriteOff> writeOffList);
}
