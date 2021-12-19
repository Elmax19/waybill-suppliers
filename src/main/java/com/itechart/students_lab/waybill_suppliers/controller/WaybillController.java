package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.WaybillState;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WaybillRecordDto;
import com.itechart.students_lab.waybill_suppliers.service.WaybillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class WaybillController {
    private final WaybillService waybillService;

    @PreAuthorize("hasAuthority('waybills:read')")
    @GetMapping("/customer/{id}/waybills")
    ResponseEntity<List<WaybillRecordDto>> getByPage(
            @Min(value = 1L, message = "Customer id must be positive number")
            @PathVariable Long id,
            @RequestParam(required = false) WaybillState state,
            @RequestParam(required = false) Long creatorId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<WaybillRecordDto> waybills
                = waybillService.findByPage(page, size, id, creatorId, state);
        return waybills.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(waybills, HttpStatus.OK);
    }
}
