package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.mapper.ItemMapper;
import com.itechart.students_lab.waybill_suppliers.repository.ItemCategoryRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemRepo itemRepo;
    private final ItemCategoryRepo itemCategoryRepo;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/items")
    List<ItemDto> findAll(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int count) {
        return itemMapper.map(itemRepo.findAll(PageRequest.of(page, count)).getContent());
    }

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/item/{id}")
    ItemDto findById(@PathVariable Long id) {
        return itemRepo.findById(id).map(itemMapper::convertToDto)
                .orElseThrow(() -> new NotFoundException("No such Item with id: " + id));
    }

    @PreAuthorize("hasAuthority('items:write')")
    @PostMapping("/item")
    ItemDto createItem(@RequestBody ItemDto newItemDto) {
        Item newItem = itemMapper.convertToEntity(newItemDto);
        Optional<ItemCategory> itemCategory = itemCategoryRepo.findByName(newItemDto.getItemCategory().getName());
        itemCategory.ifPresent(newItem::setItemCategory);
        return itemMapper.convertToDto(itemRepo.save(newItem));
    }

    @PreAuthorize("hasAuthority('items:write')")
    @DeleteMapping("/item/{id}")
    void deleteItem(@PathVariable Long id) {
        itemRepo.deleteById(id);
    }
}
