package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.ApplicationItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationItemDto;
import org.mapstruct.Mapper;

@Mapper
public interface ApplicationItemMapper {
    ApplicationItemDto convertToDto(ApplicationItem applicationItem);

    ApplicationItem convertToEntity(ApplicationItemDto applicationDto);
}
