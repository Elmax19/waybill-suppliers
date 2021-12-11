package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseMapper;
import com.itechart.students_lab.waybill_suppliers.repository.CustomerRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import com.itechart.students_lab.waybill_suppliers.utils.ExceptionMessageParser;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private static final String FAILED_CREATE_WAREHOUSE_CUSTOMER_DEACTIVATED
            = "Failed to create warehouse, because the customer is deactivated";
    private static final String WAREHOUSE_WITH_NAME_CUSTOMER_EXISTS
            = "This customer already has a warehouse with the same name";
    private static final String CUSTOMER_WITH_ID_NOT_FOUND
            = "Customer with id %d not found";

    private final CustomerRepo customerRepo;
    private final WarehouseRepo warehouseRepo;
    private final WarehouseMapper warehouseMapper = Mappers.getMapper(WarehouseMapper.class);

    public String processSQLIntegrityConstraintViolationException
            (SQLIntegrityConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message.startsWith("Duplicate entry")) {
            String[] tableAndColumn = ExceptionMessageParser.parseSqlDuplicateEntryMessage(message);
            if ("warehouse".equals(tableAndColumn[0])) {
                return WAREHOUSE_WITH_NAME_CUSTOMER_EXISTS;
            }
        }
        return null;
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
        Optional<Customer> customer = customerRepo.findById(warehouseDto.getCustomerId());
        if (customer.isPresent()) {
            if (customer.get().getActiveStatus() == ActiveStatus.INACTIVE) {
                throw new ServiceException(HttpStatus.CONFLICT,
                        FAILED_CREATE_WAREHOUSE_CUSTOMER_DEACTIVATED);
            }
            warehouse.setCustomer(customer.get());
        } else {
            throw new EntityNotFoundException(
                    String.format(CUSTOMER_WITH_ID_NOT_FOUND, warehouseDto.getCustomerId()));
        }
        warehouse.setAvailableCapacity(warehouse.getTotalCapacity());
        warehouse = warehouseRepo.save(warehouse);
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        warehouseRepo.deleteByIdIn(ids);
    }
}
