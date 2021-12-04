package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseRequestDto;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseMapper;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WarehouseController {
    private final WarehouseRepo warehouseRepo;
    private final WarehouseMapper warehouseMapper = Mappers.getMapper(WarehouseMapper.class);

    //@PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/warehouses")
    List<WarehouseRequestDto> findWarehouses(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return warehouseMapper.warehousesListToWarehousesDtoList(
                warehouseRepo.findAll(PageRequest.of(page, size)).getContent()
        );
    }

    //@PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/warehouse/{id}")
    WarehouseRequestDto findWarehouses(@PathVariable Long id) {
        return warehouseRepo.findById(id).map(warehouseMapper::warehouseToWarehouseDto)
                .orElseThrow(() -> new NotFoundException("Warehouse with id #" + id + " not found"));
    }

    //@PreAuthorize("hasAuthority('warehouses:write')")
    @PostMapping("/warehouse")
    WarehouseRequestDto addWarehouse(@RequestBody WarehouseRequestDto warehouseRequestDto) {
        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(warehouseRequestDto);
        warehouse = warehouseRepo.save(warehouse);
        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    //@PreAuthorize("hasAuthority('warehouses:write')")
    @DeleteMapping("/warehouse/{id}")
    void removeWarehouse(@PathVariable Long id) {
        warehouseRepo.deleteById(id);
    }
}
