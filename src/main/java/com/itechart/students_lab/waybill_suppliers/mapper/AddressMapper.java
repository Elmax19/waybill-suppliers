package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.dto.AddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface AddressMapper {
    @Mappings({
            @Mapping(target = "state", source = "address.state"),
            @Mapping(target = "city", source = "address.city"),
            @Mapping(target = "addressLine1", source = "address.firstAddressLine"),
            @Mapping(target = "addressLine2", source = "address.secondAddressLine")
    })
    AddressDto addressToAddressDto(Address address);

    @Mappings({
            @Mapping(target = "state", source = "addressDto.state"),
            @Mapping(target = "city", source = "addressDto.city"),
            @Mapping(target = "firstAddressLine", source = "addressDto.addressLine1"),
            @Mapping(target = "secondAddressLine", source = "addressDto.addressLine2")
    })
    Address addressDtoToAddress(AddressDto addressDto);
}
