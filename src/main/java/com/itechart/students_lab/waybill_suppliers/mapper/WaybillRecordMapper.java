package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Waybill;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillRecordDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface WaybillRecordMapper extends WarehouseMapper {
    WaybillRecordDto waybillToWaybillRecordDto(Waybill waybill);

    List<WaybillRecordDto> waybillListToWaybillRecordDtoList(List<Waybill> waybills);
}
