package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    @Mapping(source = "application.warehouse.id", target = "warehouseId")
    @Mapping(source = "application.creatingUser.id", target = "creatingUserId")
    @Mapping(source = "application.updatingUser.id", target = "updatingUserId")
    @Mapping(source = "application.items", target = "items")
    ApplicationDto convertToDto(Application application);

    @Mapping(source = "applicationDto.warehouseId", target = "warehouse.id")
    @Mapping(source = "applicationDto.creatingUserId", target = "creatingUser.id")
    @Mapping(source = "applicationDto.updatingUserId", target = "updatingUser.id")
    @Mapping(source = "applicationDto.items", target = "items")
    Application convertToEntity(ApplicationDto applicationDto);

    List<ApplicationDto> map(List<Application> applications);
}
