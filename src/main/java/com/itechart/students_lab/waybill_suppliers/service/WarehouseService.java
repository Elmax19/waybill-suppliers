package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Address;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.CustomerDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.exception.ServiceException;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseMapper;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private static final String WAREHOUSE_WITH_ID_NOT_FOUND
            = "Warehouse with id #%d not found";
    private static final String WAREHOUSE_WITH_NAME_CUSTOMER_OR_ADDRESS_EXISTS
            = "Warehouse with name '%s' for customer '%s' or address %s exists";
    private static final String FAILED_CREATE_WAREHOUSE_CUSTOMER_DEACTIVATED
            = "Failed to create warehouse, because the customer is deactivated";

    private final WarehouseRepo warehouseRepo;
    private final WarehouseMapper warehouseMapper = Mappers.getMapper(WarehouseMapper.class);

    public List<WarehouseDto> findByPage(int page, int size, CustomerDto customerDto) {
        Customer customer = warehouseMapper.customerDtoToCustomer(customerDto);
        Example<Warehouse> warehouseExample = Example.of(
                new Warehouse(null, null, null, null, null, customer));
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

    @Transactional
    public WarehouseDto create(WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(warehouseDto);
        String whName = warehouse.getName();
        Customer whCustomer = warehouse.getCustomer();
        Address whAddress = warehouse.getAddress();

        if (whCustomer.getActiveStatus() == ActiveStatus.INACTIVE) {
            throw new ServiceException(HttpStatus.CONFLICT,
                    FAILED_CREATE_WAREHOUSE_CUSTOMER_DEACTIVATED);
        }

        List<Warehouse> dbWarehouses =
                warehouseRepo.findByAddressOrCustomerAndName(whAddress.getState(),
                        whAddress.getCity(), whAddress.getFirstAddressLine(),
                        whAddress.getSecondAddressLine(), warehouse.getName(), whCustomer.getName());
        if (!dbWarehouses.isEmpty()) {
            throw new EntityExistsException(
                    String.format(WAREHOUSE_WITH_NAME_CUSTOMER_OR_ADDRESS_EXISTS,
                            whName, whCustomer.getName(), warehouse.getAddress()));
        }

        warehouse.setAvailableCapacity(warehouse.getTotalCapacity());
        warehouse = warehouseRepo.save(warehouse);
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    public void deleteById(Long id) {
        warehouseRepo.deleteById(id);
    }

    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        warehouseRepo.deleteByIdIn(ids);
    }
}
