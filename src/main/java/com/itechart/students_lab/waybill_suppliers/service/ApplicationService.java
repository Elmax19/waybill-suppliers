package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationItem;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.StateTaxes;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationRecordDto;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    public List<ApplicationDto> findAllApplicationsByCustomer(Long customerId, int page, int count) {
        return applicationMapper.map(applicationRepo.findAllByWarehouseCustomerIdAndOutgoingIsTrue(customerId, PageRequest.of(page, count)));
    }

    public int findApplicationsCountByCustomer(Long customerId) {
        return applicationRepo.countByWarehouseCustomerIdAndOutgoingIsTrue(customerId);
    }

    public List<ApplicationDto> findAllApplicationsByWarehouse(Long warehouseId, int page, int count, String status) {
        switch (status) {
            case "OUTGOING":
                return applicationMapper.map(applicationRepo.findAllByWarehouseIdAndOutgoing(warehouseId, true, PageRequest.of(page, count)));
            case "INCOMING":
                return applicationMapper.map(applicationRepo.findAllByWarehouseIdAndOutgoing(warehouseId, false, PageRequest.of(page, count)));
            default:
                return applicationMapper.map(applicationRepo.findAllByWarehouseId(warehouseId, PageRequest.of(page, count)));
        }
    }

    public int findApplicationsCountByWarehouse(Long warehouseId, String status) {
        switch (status) {
            case "OUTGOING":
                return applicationRepo.countByWarehouseIdAndOutgoing(warehouseId, true);
            case "INCOMING":
                return applicationRepo.countByWarehouseIdAndOutgoing(warehouseId, false);
            default:
                return applicationRepo.countByWarehouseId(warehouseId);
        }
    }

    public ApplicationDto findApplication(int applicationNumber, Long customerId) {
        return applicationMapper.convertToDto(applicationRepo.findByNumberAndWarehouseCustomerId(applicationNumber, customerId));
    }

    public List<ApplicationDto> findApplicationsByStatus(Long customerId, ApplicationStatus status, int page, int count) {
        return applicationMapper.map(applicationRepo.findAllByStatusAndWarehouseCustomerId(status, customerId, PageRequest.of(page, count)));
    }

    public List<ApplicationRecordDto> findOutgoingApplications(Long customerId,
                                                               Long warehouseId,
                                                               ApplicationStatus status,
                                                               int page,
                                                               int size) {
        Example<Application> applicationExample = Example.of(
                new Application(
                        new Warehouse(
                                warehouseId,
                                new Customer(customerId)
                        ),
                        status,
                        true
                )
        );
        return applicationMapper.applicationListToApplicationRecordDtoList(
                applicationRepo.findAll(applicationExample, PageRequest.of(page, size))
                        .getContent());
    }

    @Transactional
    public ApplicationDto createNewApplication(Long customerId, ApplicationDto applicationDto) {
        Application newApplication = applicationMapper.convertToEntity(applicationDto);
        Address address = newApplication.getDestinationAddress();
        addressRepo.save(address);
        for (ApplicationItem applicationItem : newApplication.getItems()) {
            applicationItem.setApplication(newApplication);
        }
        newApplication = applicationRepo.save(newApplication);
        Customer customer = new Customer();
        customer.setId(customerId);
        newApplication.getWarehouse().setCustomer(customer);
        if(applicationDto.getId()!=null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();
            changeApplicationStatus(currentUserName, customerId, newApplication.getNumber(), ApplicationStatus.OPEN);
        } else {
            newApplication.getItems().forEach(applicationItemRepo::save);
        }
        return applicationMapper.convertToDto(newApplication);
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

    public List<Application> findByIdIn(Set<Long> ids) {
        return applicationRepo.findAllById(ids);
    }
}
