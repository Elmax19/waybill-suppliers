package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseMapper;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
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
            = "Warehouse with such address exists: ";

    private final WarehouseRepo warehouseRepo;
    private final WarehouseMapper warehouseMapper = Mappers.getMapper(WarehouseMapper.class);

    private final CustomerService customerService;

    public Optional<String> processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndColumn = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("warehouse".equals(tableAndColumn[0])) {
                return Optional.of(WAREHOUSE_WITH_NAME_CUSTOMER_EXISTS);
            }
        }
        return Optional.empty();
    }

    public List<WarehouseDto> findByPage(int page, int size, Long customerId) {
        Example<Warehouse> warehouseExample = Example.of(new Warehouse(new Customer(customerId)));
        return warehouseMapper.warehousesListToWarehousesDtoList(
                warehouseRepo.findAll(warehouseExample,
                        PageRequest.of(page, size, Sort.by("name"))).getContent());
    }

    public WarehouseDto findById(Long id) {
        Warehouse warehouse = warehouseRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format(WAREHOUSE_WITH_ID_NOT_FOUND, id)));
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    public WarehouseDto create(WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(warehouseDto);
        Customer customer = customerService.getActiveCustomer(warehouseDto.getCustomerId());

        Address address = warehouse.getAddress();
        synchronized (this) {
            if (!warehouseRepo.findByAddress(
                    address.getState(),
                    address.getCity(),
                    address.getFirstAddressLine(),
                    address.getSecondAddressLine()
            ).isEmpty()) {
                throw new EntityExistsException(WAREHOUSE_WITH_ADDRESS_EXISTS + address);
            }

            warehouse.setCustomer(customer);
            warehouse.setAvailableCapacity(warehouse.getTotalCapacity());
            warehouse = warehouseRepo.save(warehouse);
        }
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        warehouseRepo.deleteByIdIn(ids);
    }
}
