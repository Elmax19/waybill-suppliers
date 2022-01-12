package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ApplicationStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Employee;
import com.itechart.students_lab.waybill_suppliers.entity.Warehouse;
import com.itechart.students_lab.waybill_suppliers.entity.WarehouseDispatcher;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseDto;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseDispatcherRepo;
import com.itechart.students_lab.waybill_suppliers.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class WarehouseController {
    private static final String FAILED_DELETE_EMPTY_NOT_EXIST_WAREHOUSES
            = "Failed to delete %d out of %d warehouses, because they're not empty or don't exist";

    private final WarehouseService warehouseService;
    private final WarehouseDispatcherRepo warehouseDispatcherRepo;

    @PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping({"/customer/{id}/allWarehouses", "/customer/{id}/warehouses/all"})
    ResponseEntity<List<WarehouseDto>> getAll(@Min(value = 1L, message = "Customer id must be positive number")
                                              @PathVariable Long id) {
        List<WarehouseDto> warehouses = warehouseService.findAll(id);
        return warehouses.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(warehouses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/customer/{id}/warehouses")
    ResponseEntity<List<WarehouseDto>> getByPageOrAllByContainingOutApplicationsStatus(
            @Min(value = 1L, message = "Customer id must be positive number")
            @PathVariable Long id,
            @RequestParam(required = false) ApplicationStatus withApplicationStatus,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<WarehouseDto> warehouses = withApplicationStatus == null
                ? warehouseService.findByPage(page, size, id)
                : warehouseService.findByContainingOutApplicationStatus(id, withApplicationStatus);
        return warehouses.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(warehouses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/customer/{id}/warehouses/total")
    Integer getTotal(@PathVariable Long id) {
        return warehouseService.getTotal(id);
    }

    @PostMapping("/employee/{employee}/warehouse/{warehouse}")
    @PreAuthorize("hasAuthority('employees:write')")
    public ResponseEntity bindDispatcherWithWarehouse(
            @PathVariable Employee employee,
            @PathVariable Warehouse warehouse) {
        WarehouseDispatcher warehouseDispatcher = new WarehouseDispatcher(warehouse, employee);
        warehouseDispatcherRepo.save(warehouseDispatcher);
        return ResponseEntity.ok().body("Dispatcher successfully bound with warehouse");
    }

    @PreAuthorize("hasAuthority('warehouses:read')")
    @GetMapping("/warehouse/{id}")
    ResponseEntity<WarehouseDto> getById(@Min(value = 1L,
            message = "Warehouse id must be positive number")
                                         @PathVariable Long id) {
        WarehouseDto warehouse = warehouseService.findById(id);
        return warehouse == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(warehouse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('warehouses:write')")
    @PostMapping("/warehouse")
    @ResponseStatus(code = HttpStatus.CREATED)
    WarehouseDto create(@Valid @RequestBody WarehouseDto warehouseDto) {
        return warehouseService.create(warehouseDto);
    }

    @PreAuthorize("hasAuthority('warehouses:write')")
    @DeleteMapping("/warehouses")
    String removeEmptyWarehouses(@NotNull(message = "At least one warehouse's id must be specified")
                                 @RequestBody List<Long> id) {
        int deletedAmount = warehouseService.deleteEmptyByIdIn(id);
        int allAmount = id.size();
        if (deletedAmount != allAmount) {
            return String.format(FAILED_DELETE_EMPTY_NOT_EXIST_WAREHOUSES,
                    allAmount - deletedAmount, allAmount);
        }
        return "Selected warehouses was successfully removed!";
    }
}
