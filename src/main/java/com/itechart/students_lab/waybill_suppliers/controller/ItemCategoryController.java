package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Customer;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategory;
import com.itechart.students_lab.waybill_suppliers.entity.ItemCategoryWithItemsCount;
import com.itechart.students_lab.waybill_suppliers.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http:/localhost:3000"})
@RestController
@RequiredArgsConstructor
public class ItemCategoryController {

    private final ItemService itemService;
    // get all ItemCategories
    // get items amount of such category
    // get all ItemCategories have at least one item
    //edit ItemCategory

    @GetMapping("/customer/{id}/categories")
    @PreAuthorize("hasAuthority('categories:read')")
    public List<ItemCategoryWithItemsCount> getAll(@PathVariable Long id,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int count
                                                   ){
        return itemService.getAll(id, page, count);
    }
}
