package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {
    Item convertToEntity(ItemDto itemDto);
    ItemDto convertToDto(Item item);
}
