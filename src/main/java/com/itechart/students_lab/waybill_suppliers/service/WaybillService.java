package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Application;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.Waybill;
import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ApplicationRecordDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillDetailsDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillEditDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillRecordDto;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.mapper.WaybillMapper;
import com.itechart.students_lab.waybill_suppliers.repository.ApplicationRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WaybillRepo;
import com.itechart.students_lab.waybill_suppliers.validation.WaybillValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaybillService {
    private static final String WAYBILL_ID_MUST_BE_SPECIFIED
            = "Waybill id must be specified in order to update it";
    private static final String WAYBILL_WITH_ID_NOT_FOUND
            = "Failed to update non-existent waybill with id ";
    private static final String CREATOR_ID_MUST_BE_SPECIFIED
            = "Creating specialist id must be specified";
    private static final String UPDATER_ID_MUST_BE_SPECIFIED
            = "Updating specialist id must be specified";

    private final WaybillRepo waybillRepo;
    private final WaybillMapper waybillMapper;
    private final WaybillValidator waybillValidator;

    private final ApplicationRepo applicationRepo;

    public List<WaybillRecordDto> getByPage(int page,
                                            int size,
                                            Long customerId,
                                            Long creatorId,
                                            Collection<WaybillState> states) {
        if (states == null) {
            states = List.of(WaybillState.values());
        }

        return waybillMapper.waybillListToWaybillRecordDtoList(
                (creatorId != null
                        ? waybillRepo.findByCreatorIdAndWarehouseCustomerIdAndStateIn(
                        creatorId, customerId, states,
                        PageRequest.of(page, size))
                        : waybillRepo.findByWarehouseCustomerIdAndStateIn(
                        customerId, states,
                        PageRequest.of(page, size))
                )
                        .getContent()
        );
    }

    public WaybillDetailsDto getById(Long customerId, Long waybillId) {
        Example<Waybill> waybillExample = Example.of(
                new Waybill(
                        waybillId,
                        new Warehouse(
                                new Customer(customerId)
                        )
                )
        );
        return waybillMapper.waybillToWaybillDetailsDto(
                waybillRepo.findOne(waybillExample)
                        .orElse(null));
    }

    public long getCount(Long creatorId,
                         Long customerId,
                         Collection<WaybillState> states) {
        if (states == null) {
            states = List.of(WaybillState.values());
        }

        return creatorId != null
                ? waybillRepo.countByCreatorIdAndWarehouseCustomerIdAndStateIn(creatorId, customerId, states)
                : waybillRepo.countByWarehouseCustomerIdAndStateIn(customerId, states);
    }

    @Transactional
    public WaybillDetailsDto create(WaybillEditDto waybillEditDto) {
        if (waybillEditDto.getCreatorId() == null) {
            throw new ServiceException(HttpStatus.CONFLICT, CREATOR_ID_MUST_BE_SPECIFIED);
        }

        Waybill waybill = waybillMapper.waybillEditDtoToWaybill(waybillEditDto);

        waybillValidator.waybillApplicationCompatibilityConstraint(
                waybill.getWarehouse(), waybill.getApplications());

        waybillValidator.employeeRoleCompatibilityConstraint(
                waybill.getCreator(), waybill.getLastUpdater(), waybill.getDriver());

        waybillValidator.noInWaybillApplicationsConstraint(waybill.getApplications());

        LocalDateTime now = LocalDateTime.now();
        waybill.setRegistrationTime(now);
        waybill.setLastUpdateTime(now);
        waybill.setLastUpdater(waybill.getCreator());

        waybill = waybillRepo.save(waybill);

        List<Long> applicationIds = waybill.getApplications().stream()
                .map(Application::getId)
                .collect(Collectors.toList());
        applicationRepo.setApplicationsWaybillId(waybill.getId(), applicationIds);

        return waybillMapper.waybillToWaybillDetailsDto(waybill);
    }

    @Transactional
    public WaybillDetailsDto update(WaybillEditDto waybillEditDto) {
        if (waybillEditDto.getLastUpdaterId() == null) {
            throw new ServiceException(HttpStatus.CONFLICT, UPDATER_ID_MUST_BE_SPECIFIED);
        }

        Long waybillId = waybillEditDto.getId();
        if (waybillId == null) {
            throw new BadRequestException(WAYBILL_ID_MUST_BE_SPECIFIED);
        }

        Waybill waybill = waybillMapper.waybillEditDtoToWaybill(waybillEditDto);

        waybillValidator.waybillApplicationCompatibilityConstraint(
                waybill.getWarehouse(), waybill.getApplications());

        waybillValidator.employeeRoleCompatibilityConstraint(
                waybill.getCreator(), waybill.getLastUpdater(), waybill.getDriver());

        Waybill dbWaybill = waybillRepo.findById(waybillId).orElseThrow(()
                -> new EntityNotFoundException(WAYBILL_WITH_ID_NOT_FOUND + waybillId));
        Employee creator = dbWaybill.getCreator();
        List<Long> dbWaybillApplicationIds = dbWaybill.getApplications().stream()
                .map(Application::getId)
                .collect(Collectors.toList());

        waybillMapper.update(waybillEditDto, dbWaybill);
        dbWaybill.setLastUpdateTime(LocalDateTime.now());
        dbWaybill.setCreator(creator);

        dbWaybill = waybillRepo.save(dbWaybill);

        List<Long> editWaybillApplicationIds = waybillEditDto.getApplicationRecords().stream()
                .map(ApplicationRecordDto::getId)
                .collect(Collectors.toList());

        if (!dbWaybillApplicationIds.equals(editWaybillApplicationIds)) {
            applicationRepo.clearApplicationsWaybillInfo(dbWaybillApplicationIds);
            applicationRepo.setApplicationsWaybillId(waybillId, editWaybillApplicationIds);
        }

        return waybillMapper.waybillToWaybillDetailsDto(dbWaybill);
    }

    @Transactional
    public void delete(Long customerId, Long waybillId) {
        applicationRepo.clearApplicationsWaybillInfo(customerId, waybillId);
        waybillRepo.deleteByIdAndWarehouseCustomerId(waybillId, customerId);
    }
}
