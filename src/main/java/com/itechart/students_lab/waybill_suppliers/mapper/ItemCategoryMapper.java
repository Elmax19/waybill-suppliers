package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemCategoryDto;
import org.mapstruct.Mapper;

@Mapper
public interface ItemCategoryMapper {
    ItemCategory convertToEntity(ItemCategoryDto itemDto);

    ItemCategoryDto convertToDto(ItemCategory item);
}