package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.WriteOff;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WriteOffDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface WriteOffMapper {
    @Mapping(source = "writeOff.creatingUser.id", target = "creatingUserId")
    @Mapping(source = "writeOff.warehouse.id", target = "warehouseId")
    WriteOffDto convertToDto(WriteOff writeOff);

    List<WriteOffDto> map(List<WriteOff> writeOffList);
}
