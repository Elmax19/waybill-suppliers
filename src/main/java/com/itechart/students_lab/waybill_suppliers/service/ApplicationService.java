package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationItem;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationDto;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.mapper.ApplicationMapper;
import com.itechart.students_lab.waybill_suppliers.repository.AddressRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ApplicationItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ApplicationRepo;
import com.itechart.students_lab.waybill_suppliers.repository.EmployeeRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final AddressRepo addressRepo;
    private final ApplicationRepo applicationRepo;
    private final ApplicationItemRepo applicationItemRepo;
    private final ItemRepo itemRepo;
    private final WarehouseRepo warehouseRepo;
    private final WarehouseItemRepo warehouseItemRepo;
    private final EmployeeRepo employeeRepo;
    private final ApplicationMapper applicationMapper = Mappers.getMapper(ApplicationMapper.class);

    public List<ApplicationDto> findAllApplications(Long customerId, int page, int count) {
        return applicationMapper.map(applicationRepo.findAllByWarehouseCustomerId(customerId, PageRequest.of(page, count)));
    }

    public ApplicationDto findApplication(int applicationNumber, Long customerId) {
        return applicationMapper.convertToDto(applicationRepo.findByNumberAndWarehouseCustomerId(applicationNumber, customerId));
    }

    public List<ApplicationDto> findApplicationsByStatus(Long customerId, ApplicationStatus status, int page, int count) {
        return applicationMapper.map(applicationRepo.findAllByStatusAndWarehouseCustomerId(status, customerId, PageRequest.of(page, count)));
    }

    public List<ApplicationDto> findAllOutgoingApplications(Long customerId, int page, int count) {
        return applicationMapper.map(applicationRepo.findAllByOutgoingIsTrueAndWarehouseCustomerId(customerId, PageRequest.of(page, count)));
    }

    public List<ApplicationDto> findOutgoingApplicationsByStatus(Long customerId, ApplicationStatus status, int page, int count) {
        return applicationMapper.map(applicationRepo.findAllByOutgoingIsTrueAndStatusAndWarehouseCustomerId(status, customerId, PageRequest.of(page, count)));
    }

    @Transactional
    public ApplicationDto createNewApplication(Long customerId, ApplicationDto applicationDto) {
        Application newApplication = applicationMapper.convertToEntity(applicationDto);
        Address address = newApplication.getDestinationAddress();
        addressRepo.saveOrUpdate(address.getId(), address.getState(), address.getCity(), address.getFirstAddressLine(), address.getSecondAddressLine());
        newApplication.setWarehouse(warehouseRepo.findById(applicationDto.getWarehouseId()).orElseThrow(
                () -> new NotFoundException("No such Warehouse with Id: " + applicationDto.getWarehouseId())));
        if (!newApplication.getWarehouse().getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("Incorrect customer: " + customerId);
        }
        newApplication.setCreatingUser(employeeRepo.findById(applicationDto.getCreatingUserId()).orElseThrow(
                () -> new NotFoundException("No such User with Id: " + applicationDto.getUpdatingUserId())));
        newApplication.setUpdatingUser(employeeRepo.findById(applicationDto.getCreatingUserId()).orElseThrow(
                () -> new NotFoundException("No such User with Id: " + applicationDto.getUpdatingUserId())));
        for (ApplicationItem applicationItem : newApplication.getItems()) {
            applicationItem.setApplication(newApplication);
        }
        return applicationMapper.convertToDto(applicationRepo.save(newApplication));
    }

    @Transactional
    public ApplicationDto changeApplicationStatus(String userLogin, Long customerId, int applicationNumber, ApplicationStatus status) {
        Long updatingUserId = employeeRepo.findIdByLogin(userLogin);
        applicationRepo.updateStatusByCustomerIdAndNumber(status.name(), updatingUserId, customerId, applicationNumber);
        return findApplication(applicationNumber, customerId);
    }

    @Transactional
    public ApplicationDto acceptApplicationItems(String userLogin, Long customerId, int applicationNumber, Long applicationItemId, int count) {
        Application application = applicationRepo.findByNumberAndWarehouseCustomerId(applicationNumber, customerId);
        if (application.isOutgoing()) {
            throw new BadRequestException("Application shouldn't be outgoing");
        }
        ApplicationItem applicationItem = application.getItems().stream()
                .filter(item -> item.getId().equals(applicationItemId))
                .findAny().orElseThrow(
                        () -> new BadRequestException("No ApplicationItem with id: " + applicationItemId));
        Item item = itemRepo.findById(applicationItem.getItem().getId()).orElseThrow(
                () -> new NotFoundException("No such Item with id: " + applicationItem.getItem().getId()));
        int places = item.getUnits() * count;
        if (application.getWarehouse().getAvailableCapacity() < places) {
            throw new BadRequestException("No such place(" + (application.getWarehouse().getTotalCapacity() - application.getWarehouse().getAvailableCapacity() + places) + ") in Warehouse");
        }
        changeApplicationItemPlacedCount(applicationItem, count);
        warehouseRepo.updateAvailableCapacity(application.getWarehouse().getId(), places);
        warehouseItemRepo.updateWarehouseItemCount(application.getWarehouse().getId(), item.getId(), count);
        if (applicationItem.getCount() == applicationItem.getPlacedCount() + count && checkOnClosedApplication(application)) {
            changeApplicationStatus(userLogin, customerId, applicationNumber, ApplicationStatus.FINISHED_PROCESSING);
        } else {
            changeApplicationStatus(userLogin, customerId, applicationNumber, ApplicationStatus.STARTED_PROCESSING);
        }
        return findApplication(applicationNumber, customerId);
    }

    private void changeApplicationItemPlacedCount(ApplicationItem applicationItem, int count) {
        if (applicationItem.getCount() < applicationItem.getPlacedCount() + count) {
            throw new BadRequestException("No such items(" + (applicationItem.getPlacedCount() + count) + ") in Application to accept");
        }
        applicationItemRepo.updateItemPlacedCount(applicationItem.getId(), count);
    }

    private boolean checkOnClosedApplication(Application application) {
        return application.getItems().stream().filter(item -> item.getCount() != item.getPlacedCount()).count() == 1;
    }
}
