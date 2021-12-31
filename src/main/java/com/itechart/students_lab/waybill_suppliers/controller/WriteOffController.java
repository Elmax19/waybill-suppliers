package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WriteOffDto;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.mapper.WriteOffMapper;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseDispatcherRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WriteOffRepo;
import com.itechart.students_lab.waybill_suppliers.service.WriteOffService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class WriteOffController {
    private final WriteOffService writeOffService;
    private final WriteOffRepo writeOffRepo;
    private final EmployeeRepo employeeRepo;
    private final WarehouseDispatcherRepo warehouseDispatcherRepo;
    private final WriteOffMapper writeOffMapper = Mappers.getMapper(WriteOffMapper.class);

    @GetMapping("/customer/{customerId}/writeOffs")
    @PreAuthorize("hasAuthority('writeOff:read')")
    List<WriteOffDto> findAllWriteOffs(@PathVariable Long customerId){
        return writeOffMapper.map(writeOffRepo.findAllByWarehouseCustomerIdOrCarCustomerIdOrderByDateTime(customerId));
    }

    @GetMapping("/customer/{customerId}/writeOffs/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyAuthority('writeOff:read', 'warehouseWriteOff:read')")
    List<WriteOffDto> findAllWriteOffsByWarehouse(@PathVariable Long customerId, @PathVariable Long warehouseId){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!warehouseDispatcherRepo.existsByWarehouseIdAndDispatcherLogin(warehouseId, auth.getName())){
            throw new BadRequestException("You don't belong this Warehouse");
        }
        return writeOffMapper.map(writeOffRepo.findAllByWarehouseCustomerIdAndWarehouseId(customerId, warehouseId));
    }

    @GetMapping("/customer/{customerId}/writeOffs/driver/{driverId}")
    @PreAuthorize("hasAnyAuthority('writeOff:read', 'driverWriteOff:read')")
    List<WriteOffDto> findAllWriteOffsByDriver(@PathVariable Long customerId, @PathVariable Long driverId){
        Employee driver = employeeRepo.getById(driverId);
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!driver.getLogin().equals(auth.getName())){
            throw new BadRequestException("Input driverId isn't your");
        }
        return writeOffMapper.map(writeOffRepo.findAllByCarCustomerIdAndCreatingUserIdOrderByDateTime(customerId, driverId));
    }

    @PostMapping("/customer/{customerId}/writeOff")
    @PreAuthorize("hasAuthority('writeOff:write')")
    WriteOffDto createNewWriteOff(@PathVariable Long customerId, @RequestBody WriteOffDto writeOffDto){
        if((writeOffDto.getWarehouseId() == null) == (writeOffDto.getCarId() == null)){
            throw new BadRequestException("Should be not empty only one of Car and Warehouse");
        }
        return writeOffService.create(writeOffDto);
    }
}
