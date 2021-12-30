package com.itechart.students_lab.waybill_suppliers.controller;

import com.itechart.students_lab.waybill_suppliers.entity.User;
import com.itechart.students_lab.waybill_suppliers.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins={ "http://localhost:3000" })
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepo userRepo;

    @GetMapping("/user/{name}")
    public User getAuthUser(@PathVariable String name){
        return userRepo.findByLogin(name).get();
    }
}
