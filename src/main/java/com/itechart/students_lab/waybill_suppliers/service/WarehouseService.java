package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.exception.BadRequestException;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseMapper;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private static final String WAREHOUSE_WITH_ID_NOT_FOUND
            = "Warehouse with id %d not found";
    private static final String WAREHOUSE_WITH_NAME_CUSTOMER_EXISTS
            = "This customer already has a warehouse with the same name";
    private static final String WAREHOUSE_WITH_ADDRESS_EXISTS
            = "Warehouse with such address exists";
    private static final String WAREHOUSE_CUSTOMER_DEACTIVATED
            = "Warehouse's customer is deactivated";

    private final WarehouseRepo warehouseRepo;
    private final WarehouseMapper warehouseMapper;

    private final CustomerService customerService;
    private final AddressService addressService;

    public Optional<String> processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndKey = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("warehouse".equals(tableAndKey[0])) {
                switch (tableAndKey[1]) {
                    case "fk-warehouse-address_id":
                        return Optional.of(WAREHOUSE_WITH_ADDRESS_EXISTS);
                    case "w_name_customer_id":
                        return Optional.of(WAREHOUSE_WITH_NAME_CUSTOMER_EXISTS);
                }
            }
        }
        return Optional.empty();
    }

    public List<WarehouseDto> findByPage(int page, int size, Long customerId) {
        Example<Warehouse> warehouseExample = Example.of(new Warehouse(new Customer(customerId)));
        return warehouseMapper.warehousesListToWarehousesDtoList(
                warehouseRepo.findAll(warehouseExample,
                        PageRequest.of(page, size)).getContent());
    }

    public List<WarehouseDto> findByPageAndContainingOutApplicationStatus(int page,
                                                                          int size,
                                                                          Long customerId,
                                                                          ApplicationStatus status) {
        if (page < 0) {
            throw new BadRequestException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new BadRequestException("Page size must not be less than one!");
        }
        int offset = page * size;
        return warehouseMapper.warehousesListToWarehousesDtoList(
                warehouseRepo.findByPageAndContainingOutApplicationStatus
                        (offset, size, customerId, status.name()));
    }

    public WarehouseDto findById(Long id) {
        Warehouse warehouse = warehouseRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format(WAREHOUSE_WITH_ID_NOT_FOUND, id)));
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    public WarehouseDto create(WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(warehouseDto);
        warehouse.setAvailableCapacity(warehouse.getTotalCapacity());

        Customer customer = customerService.getActiveCustomer(warehouseDto.getCustomerId());
        warehouse.setCustomer(customer);

        Address address = warehouse.getAddress();
        address = addressService.find(address).orElse(address);
        warehouse.setAddress(address);

        warehouse = warehouseRepo.save(warehouse);
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    @Transactional
    public int deleteEmptyByIdIn(List<Long> ids) {
        return warehouseRepo.deleteEmptyByIdIn(ids);
    }

    public Warehouse getActiveCustomerWarehouse(Long id) {
        if (id == null) {
            return null;
        }

        Warehouse warehouse = warehouseRepo.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format(WAREHOUSE_WITH_ID_NOT_FOUND, id)));
        if (warehouse.getCustomer().getActiveStatus() == ActiveStatus.INACTIVE) {
            throw new ServiceException(HttpStatus.CONFLICT, WAREHOUSE_CUSTOMER_DEACTIVATED);
        }
        return warehouse;
    }
}
