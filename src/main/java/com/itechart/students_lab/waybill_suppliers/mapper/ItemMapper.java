package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ItemMapper {
    Item convertToEntity(ItemDto itemDto);

    ItemDto convertToDto(Item item);

    List<ItemDto> map(List<Item> items);
}
