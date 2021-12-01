package com.itechart.students_lab.waybill_suppliers.mapper.impl;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import com.itechart.students_lab.waybill_suppliers.mapper.ItemCategoryMapper;
import com.itechart.students_lab.waybill_suppliers.mapper.ItemMapper;
import org.mapstruct.factory.Mappers;

public class ItemMapperImpl implements ItemMapper {
    private final ItemCategoryMapper itemCategoryMapper = Mappers.getMapper(ItemCategoryMapper.class);

    @Override
    public Item convertToEntity(ItemDto itemDto) {
        if(itemDto == null){
            return null;
        }
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setUpc(itemDto.getUpc());
        item.setLabel(itemDto.getLabel());
        item.setItemCategory(itemCategoryMapper.convertToEntity(itemDto.getItemCategory()));
        item.setUnits(itemDto.getUnits());
        item.setPrice(itemDto.getPrice());
        return item;
    }

    @Override
    public ItemDto convertToDto(Item item) {
        if(item == null){
            return null;
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setUpc(item.getUpc());
        itemDto.setLabel(item.getLabel());
        itemDto.setItemCategory(itemCategoryMapper.convertToDto(item.getItemCategory()));
        itemDto.setUnits(item.getUnits());
        itemDto.setPrice(item.getPrice());
        return itemDto;
    }
}
