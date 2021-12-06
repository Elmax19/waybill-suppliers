package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.dto.CustomerDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/warehouses")
    ResponseEntity<List<WarehouseDto>> getByPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @NotNull(message = "Customer must be specified")
            @RequestBody(required = false) CustomerDto customerDto) {
        List<WarehouseDto> warehouses = warehouseService.findByPage(page, size, customerDto);
        if (warehouses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(warehouses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/warehouse/{id}")
    ResponseEntity<WarehouseDto> getById(@Min(value = 1L,
            message = "Warehouse id must be positive number")
                                         @PathVariable Long id) {
        WarehouseDto warehouse = warehouseService.findById(id);
        if (warehouse == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(warehouse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('warehouses:write')")
    @PostMapping("/warehouse")
    ResponseEntity<WarehouseDto> create(@Valid @RequestBody WarehouseDto warehouseDto) {
        WarehouseDto createdWarehouse = warehouseService.create(warehouseDto);
        return new ResponseEntity<>(createdWarehouse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('warehouses:write')")
    @DeleteMapping("/warehouse/{id}")
    ResponseEntity<Void> removeWarehouse(@Min(value = 1L,
            message = "Warehouse id must be positive number")
                                         @PathVariable Long id) {
        warehouseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('warehouses:write')")
    @DeleteMapping("/warehouses")
    ResponseEntity<Void> removeWarehouses(@NotNull(
            message = "At least one warehouse's id must be specified")
                                          @RequestParam(required = false) List<Long> id) {
        warehouseService.deleteByIdIn(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
