package com.itechart.students_lab.waybill_suppliers.mapper.impl;

import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemCategoryDto;
import com.itechart.students_lab.waybill_suppliers.mapper.ItemCategoryMapper;

public class ItemCategoryMapperImpl implements ItemCategoryMapper {
    @Override
    public ItemCategory convertToEntity(ItemCategoryDto itemCategoryDto) {
        if(itemCategoryDto==null){
            return null;
        }
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setName(itemCategoryDto.getName());
        itemCategory.setTax_rate(itemCategory.getTax_rate());
        return itemCategory;
    }

    @Override
    public ItemCategoryDto convertToDto(ItemCategory item) {
        if(item==null){
            return null;
        }
        ItemCategoryDto itemCategoryDto = new ItemCategoryDto();
        itemCategoryDto.setName(item.getName());
        itemCategoryDto.setTax_rate(item.getTax_rate());
        return itemCategoryDto;
    }
}
