package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationDto;
import com.itechart.students_lab.waybill_suppliers.mapper.ApplicationMapper;
import com.itechart.students_lab.waybill_suppliers.repository.ApplicationRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationRepo applicationRepo;
    private final ApplicationMapper applicationMapper = Mappers.getMapper(ApplicationMapper.class);


    @GetMapping("/customer/{customerId}/applications")
    @PreAuthorize("hasAuthority('applications.all:read')")
    List<ApplicationDto> findAllApplications(@PathVariable Long customerId,
                                          @RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int count) {
        return applicationMapper.map(applicationRepo.findAllByWarehouseCustomerId(customerId, PageRequest.of(page, count)));
    }

    @GetMapping("/customer/{customerId}/application/{applicationId}")
    @PreAuthorize("hasAuthority('applications.all:read')")
    ApplicationDto findApplication(@PathVariable Long customerId, @PathVariable Long applicationId) {
        return applicationMapper.convertToDto(applicationRepo.findByIdAndWarehouseCustomerId(applicationId, customerId));
    }

    @GetMapping("/customer/{customerId}/applications/{status}")
    @PreAuthorize("hasAuthority('applications.all:read')")
    List<ApplicationDto> findApplicationsByStatus(@PathVariable Long customerId,
                                          @PathVariable ApplicationStatus status,
                                          @RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int count){
        return applicationMapper.map(applicationRepo.findAllByStatusAndWarehouseCustomerId(status, customerId, PageRequest.of(page, count)));
    }

    @GetMapping("/customer/{customerId}/applications/outgoing")
    @PreAuthorize("hasAuthority('applications.dispatching:read')")
    List<ApplicationDto> findAllOutgoingApplications(@PathVariable Long customerId,
                                             @RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int count) {
        return applicationMapper.map(applicationRepo.findAllByOutgoingIsTrueAndWarehouseCustomerId(customerId, PageRequest.of(page, count)));
    }

    @GetMapping("/customer/{customerId}/applications/outgoing/{status}")
    @PreAuthorize("hasAuthority('applications.dispatching:read')")
    List<ApplicationDto> findOutgoingApplicationsByStatus(@PathVariable Long customerId,
                                             @PathVariable ApplicationStatus status,
                                             @RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int count){
        return applicationMapper.map(applicationRepo.findAllByOutgoingIsTrueAndStatusAndWarehouseCustomerId(status, customerId, PageRequest.of(page, count)));
    }
}
