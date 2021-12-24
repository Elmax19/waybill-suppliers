package com.itechart.students_lab.waybill_suppliers.mapper;

import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.Car;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.Waybill;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationRecordDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CarDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillDetailsDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillEditDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillRecordDto;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.service.ApplicationService;
import com.itechart.students_lab.waybill_suppliers.service.CarService;
import com.itechart.students_lab.waybill_suppliers.service.EmployeeService;
import com.itechart.students_lab.waybill_suppliers.service.WarehouseService;
import lombok.NoArgsConstructor;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring",
        uses = {WarehouseService.class, CarService.class, EmployeeService.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@NoArgsConstructor
public abstract class WaybillMapper {
    public static final String APPLICATION_MUST_CONTAIN_ID_AND_SEQ_NUMBER = "Every application record must at least contain id and sequence number";
    private static final String SOME_APPLICATIONS_DO_NOT_EXIST = "Some of the specified applications do not exist";
    private ApplicationService applicationService;

    @Autowired
    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public abstract List<WaybillRecordDto> waybillListToWaybillRecordDtoList(List<Waybill> waybills);

    @Mapping(target = "warehouse", source = "warehouseId")
    @Mapping(target = "applications", source = "applicationRecords", qualifiedByName = "ARecordToA")
    @Mapping(target = "car", source = "carId")
    @Mapping(target = "creator", source = "creatorId")
    @Mapping(target = "lastUpdater", source = "lastUpdaterId")
    @Mapping(target = "driver", source = "driverId")
    public abstract Waybill waybillEditDtoToWaybill(WaybillEditDto waybillEditDto);

    @Named("ARecordToA")
    public Set<Application> applicationRecordDtoListToApplicationSet(
            List<ApplicationRecordDto> applicationRecords) {
        if (applicationRecords == null || applicationRecords.isEmpty()) {
            return null;
        }

        if (applicationRecords.stream()
                .anyMatch(a -> a.getId() == null || a.getSequenceNumber() == null)) {
            throw new ServiceException(HttpStatus.CONFLICT,
                    APPLICATION_MUST_CONTAIN_ID_AND_SEQ_NUMBER);
        }

        Map<Long, Integer> applicationSeqNumbers = applicationRecords.stream()
                .collect(Collectors.toMap(
                        ApplicationRecordDto::getId, ApplicationRecordDto::getSequenceNumber));
        Set<Long> ids = applicationSeqNumbers.keySet();
        List<Application> applications = applicationService.findByIdIn(ids);
        if (ids.size() != applications.size()) {
            throw new ServiceException(HttpStatus.CONFLICT, SOME_APPLICATIONS_DO_NOT_EXIST);
        }

        for (Application a : applications) {
            a.setSequenceNumber(applicationSeqNumbers.get(a.getId()));
        }

        return Set.copyOf(applications);
    }

    @Mapping(target = "applicationRecords", source = "applications")
    public abstract WaybillDetailsDto waybillToWaybillDetailsDto(Waybill waybill);

    @Mapping(target = "warehouse", source = "warehouseId")
    @Mapping(target = "applications", source = "applicationRecords", qualifiedByName = "ARecordToA")
    @Mapping(target = "car", source = "carId")
    @Mapping(target = "creator", source = "creatorId")
    @Mapping(target = "lastUpdater", source = "lastUpdaterId")
    @Mapping(target = "driver", source = "driverId")
    public abstract void update(WaybillEditDto waybillEditDto, @MappingTarget Waybill waybill);

    public abstract List<ApplicationRecordDto> applicationSetToApplicationRecordDtoList(Set<Application> set);

    @Mapping(target = "warehouseId", expression = "java(application.getWarehouse().getId())")
    public abstract ApplicationRecordDto applicationToApplicationRecordDto(Application application);

    @Mapping(target = "customerId", expression = "java(car.getCustomer().getId())")
    public abstract CarDto carToCarDto(Car car);

    @Mapping(target = "customerId", expression = "java(warehouse.getCustomer().getId())")
    public abstract WarehouseDto warehouseToWarehouseDto(Warehouse warehouse);
}
