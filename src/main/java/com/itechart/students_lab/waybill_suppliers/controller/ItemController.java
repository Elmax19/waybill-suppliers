package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.Item;
import com.itechart.students_lab.waybill_suppliers.exception.NotFoundException;
import com.itechart.students_lab.waybill_suppliers.repository.ItemRepo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemController {
    private final ItemRepo itemRepo;

    public ItemController(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    @GetMapping
    List<Item> findAll(){
        return itemRepo.findAll();
    }

    @GetMapping("/{id}")
    Item findById(@PathVariable Long id){
        return itemRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("No such Item with id: " + id));
    }

    @PostMapping
    Item createItem(@RequestBody Item newItem) {
        return itemRepo.save(newItem);
    }

    @DeleteMapping("/{id}")
    void deleteEmployee(@PathVariable Long id) {
        itemRepo.deleteById(id);
    }
}
