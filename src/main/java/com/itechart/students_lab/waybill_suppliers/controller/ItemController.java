package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.ActiveStatus;
import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.WarehouseItem;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import com.itechart.students_lab.waybill_suppliers.mapper.ItemMapper;
import com.itechart.students_lab.waybill_suppliers.repository.ItemCategoryRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseItemRepo;
import com.itechart.students_lab.waybill_suppliers.repository.WarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000"})
@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemRepo itemRepo;
    private final ItemCategoryRepo itemCategoryRepo;
    private final WarehouseItemRepo warehouseItemRepo;
    private final WarehouseRepo warehouseRepo;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/customer/{customerId}/items")
    List<ItemDto> findAll(@PathVariable Long customerId, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int count) {
        return itemMapper.map(itemRepo.findAllByCustomerId(customerId, PageRequest.of(page, count)));
    }

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/customer/{customerId}/items/all")
    List<ItemDto> findAllItems(@PathVariable Long customerId) {
        return itemMapper.map(itemRepo.findAllByCustomerId(customerId));
    }

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/customer/{customerId}/items/count")
    int getCountOfItems(@PathVariable Long customerId) {
        return itemRepo.findAllByCustomerId(customerId).size();
    }

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/customer/{customerId}/item/{id}")
    ItemDto findById(@PathVariable Long customerId, @PathVariable Long id) {
        return itemMapper.convertToDto(itemRepo.findByCustomerIdAndId(customerId, id));
    }

    @PreAuthorize("hasAuthority('items:write')")
    @PostMapping("/customer/{customerId}/item")
    ItemDto createItem(@PathVariable Long customerId, @RequestBody ItemDto newItemDto, @RequestParam Long warehouseId) {
        Item newItem = itemMapper.convertToEntity(newItemDto);
        Optional<ItemCategory> itemCategory = itemCategoryRepo.findByName(newItemDto.getItemCategory().getName());
        itemCategory.ifPresent(newItem::setItemCategory);
        newItem.setCustomer(new Customer());
        newItem.getCustomer().setId(customerId);
        itemRepo.save(newItem);
        WarehouseItem warehouseItem = new WarehouseItem(warehouseRepo.findById(warehouseId).get(), newItem, 0, ActiveStatus.ACTIVE);
        warehouseItemRepo.save(warehouseItem);
        return itemMapper.convertToDto(newItem);
    }

    @PreAuthorize("hasAuthority('items:write')")
    @DeleteMapping("/customer/{customerId}/item/{id}")
    void deleteItem(@PathVariable Long customerId, @PathVariable Long id) {
        itemRepo.deleteByCustomerIdAndId(customerId, id);
    }
}
