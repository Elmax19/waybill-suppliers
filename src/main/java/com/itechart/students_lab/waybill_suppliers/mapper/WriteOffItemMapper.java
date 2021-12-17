package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOffItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WriteOffItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WriteOffItemMapper {
    @Mapping(source = "writeOffItem.item.id", target = "itemId")
    WriteOffItemDto convertToDto(WriteOffItem writeOffItem);
}
