package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.WarehouseItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.WarehouseItemDto;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.mapper.WarehouseItemMapper;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WarehouseItemController {
    private final WarehouseItemRepo warehouseItemRepo;
    private final WarehouseRepo warehouseRepo;
    private final ItemRepo itemRepo;
    private final WarehouseItemMapper warehouseItemMapper = Mappers.getMapper(WarehouseItemMapper.class);

    @PreAuthorize("hasAuthority('warehouseItems:read')")
    @GetMapping("/warehouseItems")
    List<WarehouseItemDto> findAll(@RequestParam Long warehouseId,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "10") int count) {
        return warehouseItemMapper.map(warehouseItemRepo.findAllByWarehouseId(warehouseId, PageRequest.of(page, count)));
    }

    @PreAuthorize("hasAuthority('warehouseItems:write')")
    @PutMapping("/warehouseItem")
    void changeWarehouseItemStatus(@RequestParam Long warehouseId, @RequestParam Long itemId) {
        WarehouseItem warehouseItem = warehouseItemRepo.findByWarehouseAndItem(itemId, warehouseId);
        ActiveStatus activeStatus = warehouseItem.getActiveStatus().equals(ActiveStatus.ACTIVE) ? ActiveStatus.INACTIVE : ActiveStatus.ACTIVE;
        warehouseItemRepo.updateWarehouseItem(warehouseItem.getCount(), activeStatus.name(), warehouseItem.getId());
    }

    @PreAuthorize("hasAuthority('warehouseItems:write')")
    @PostMapping("/warehouseItem")
    void createWarehouseItem(@RequestParam Long warehouseId, @RequestParam Long itemId, @RequestParam int count) {
        if(!warehouseRepo.existsById(warehouseId)){
            throw new NotFoundException("No such Warehouse with id: " + warehouseId);
        }
        if(!itemRepo.existsById(itemId)){
            throw new NotFoundException("No such Item with id: " + itemId);
        }
        if(warehouseItemRepo.existsByWarehouseIdAndItemId(warehouseId, itemId)) {
            WarehouseItem warehouseItem = warehouseItemRepo.findByWarehouseAndItem(warehouseId, itemId);
            warehouseItemRepo.updateWarehouseItem(warehouseItem.getCount()+count, warehouseItem.getActiveStatus().name(), warehouseItem.getId());
        } else {
            warehouseItemRepo.saveWarehouseItem(warehouseId, itemId, count);
        }
    }
}
