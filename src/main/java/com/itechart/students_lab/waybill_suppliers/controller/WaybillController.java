package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillDetailsDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillEditDto;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillRecordDto;
import com.itechart.students_lab.waybill_suppliers.service.WaybillService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class WaybillController {
    private final WaybillService waybillService;

    @PreAuthorize("hasAuthority('waybills:read')")
    @GetMapping("/customer/{id}/waybills")
    ResponseEntity<List<WaybillRecordDto>> getByPage(
            @Min(value = 1L, message = "Customer id must be positive number")
            @PathVariable Long id,
            @RequestParam(required = false) Collection<WaybillState> state,
            @Min(value = 1L, message = "Creator id must be positive number")
            @RequestParam(required = false) Long creatorId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<WaybillRecordDto> waybills
                = waybillService.getByPage(page, size, id, creatorId, state);
        return waybills.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(waybills, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('waybills:read')")
    @GetMapping("/customer/{customerId}/waybill/{waybillId}")
    ResponseEntity<WaybillDetailsDto> findById(@Min(value = 1L, message = "Customer id must be positive number")
                                               @PathVariable Long customerId,
                                               @Min(value = 1L, message = "Waybill id must be positive number")
                                               @PathVariable Long waybillId) {
        WaybillDetailsDto waybill = waybillService.getById(customerId, waybillId);
        return waybill == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(waybill, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('waybills:read')")
    @GetMapping("/customer/{id}/waybills/count")
    long getCount(@Min(value = 1L, message = "Customer id must be positive number")
                  @PathVariable Long id,
                  @RequestParam(required = false) Collection<WaybillState> state,
                  @Min(value = 1L, message = "Creator id must be positive number")
                  @RequestParam(required = false) Long creatorId) {
        return waybillService.getCount(creatorId, id, state);
    }

    @PreAuthorize("hasAuthority('waybills:write')")
    @PostMapping("customer/{id}/waybill")
    @ResponseStatus(code = HttpStatus.CREATED)
    WaybillDetailsDto create(@Valid @RequestBody WaybillEditDto waybillEditDto) {
        return waybillService.create(waybillEditDto);
    }

    @PreAuthorize("hasAuthority('waybills:write')")
    @PutMapping("customer/{id}/waybill")
    WaybillDetailsDto update(@Valid @RequestBody WaybillEditDto waybillEditDto) {
        return waybillService.update(waybillEditDto);
    }

    @PreAuthorize("hasAuthority('waybills:write')")
    @DeleteMapping("/customer/{customerId}/waybill/{waybillId}")
    void delete(@PathVariable Long customerId,
                @PathVariable Long waybillId) {
        waybillService.delete(customerId, waybillId);
    }
}
