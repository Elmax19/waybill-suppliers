package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.dto.ItemDto;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.repository.ItemCategoryRepo;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.expression.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemRepo itemRepo;
    private final ItemCategoryRepo itemCategoryRepo;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/items")
    List<ItemDto> findAll() {
        return convertToDtoList(itemRepo.findAll());
    }

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/items/page/{pageNumber}")
    List<ItemDto> findByPage(@PathVariable int pageNumber) {
        return convertToDtoList(itemRepo.findByPage((pageNumber - 1) * 10, 10));
    }

    @PreAuthorize("hasAuthority('items:read')")
    @GetMapping("/items/{id}")
    ItemDto findById(@PathVariable Long id) {
        return itemRepo.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("No such Item with id: " + id));
    }

    @PreAuthorize("hasAuthority('items:write')")
    @PostMapping("/items")
    ItemDto createItem(@RequestBody ItemDto newItemDto) {
        Item newItem = convertToEntity(newItemDto);
        Optional<ItemCategory> itemCategory = itemCategoryRepo.findByName(newItemDto.getItemCategory().getName());
        itemCategory.ifPresent(newItem::setItemCategory);
        return convertToDto(itemRepo.save(newItem));
    }

    @PreAuthorize("hasAuthority('items:write')")
    @DeleteMapping("/items/{id}")
    void deleteItem(@PathVariable Long id) {
        itemRepo.deleteById(id);
    }

    private List<ItemDto> convertToDtoList(List<Item> items){
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ItemDto convertToDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    private Item convertToEntity(ItemDto itemDto) throws ParseException {
        return modelMapper.map(itemDto, Item.class);
    }
}
