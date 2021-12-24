package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.dto.AddressDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address addressDtoToAddress(AddressDto addressDto);
}
