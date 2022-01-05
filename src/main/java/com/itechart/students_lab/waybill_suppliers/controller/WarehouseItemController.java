package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseItemDto;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseItemMapper;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseItemRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WarehouseItemController {
    private final WarehouseItemRepo warehouseItemRepo;
    private final WarehouseItemMapper warehouseItemMapper = Mappers.getMapper(WarehouseItemMapper.class);

    @PreAuthorize("hasAuthority('warehouseItems:read')")
    @GetMapping("/warehouse/{id}/items")
    List<WarehouseItemDto> findAll(@PathVariable Long id,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "10") int count,
                                   @RequestParam(required = false, defaultValue="ALL") String activeStatus) {
        if (activeStatus.equals("ALL")){
            return warehouseItemMapper.map(warehouseItemRepo.findAllByWarehouseId(id, PageRequest.of(page, count)));
        }
        return warehouseItemMapper.map(warehouseItemRepo.findAllByWarehouseIdAndActiveStatus(id, ActiveStatus.valueOf(activeStatus), PageRequest.of(page, count)));
    }

    @PreAuthorize("hasAuthority('warehouseItems:read')")
    @GetMapping("/warehouse/{id}/items/count")
    int getCount(@PathVariable Long id, @RequestParam(required = false, defaultValue="ALL") String activeStatus) {
        if (activeStatus.equals("ALL")) {
            return warehouseItemRepo.findAllByWarehouseId(id).size();
        }
        return warehouseItemRepo.findAllByWarehouseIdAndActiveStatus(id, ActiveStatus.valueOf(activeStatus)).size();
    }

    @PreAuthorize("hasAuthority('warehouseItems:write')")
    @PutMapping("/warehouse/{warehouseId}/item/{itemId}")
    void changeWarehouseItemStatus(@PathVariable Long warehouseId, @PathVariable Long itemId, @RequestParam String status) {
        ActiveStatus activeStatus = ActiveStatus.valueOf(status);
        warehouseItemRepo.updateWarehouseItemStatus(warehouseId, itemId, activeStatus.name());
    }

    @PreAuthorize("hasAuthority('warehouseItems:write')")
    @PostMapping("/warehouse/{warehouseId}/item/{itemId}")
    void createWarehouseItem(@PathVariable Long warehouseId, @PathVariable Long itemId, @RequestParam int count) {
        warehouseItemRepo.save(warehouseId, itemId, count, ActiveStatus.ACTIVE.name());
    }
}
