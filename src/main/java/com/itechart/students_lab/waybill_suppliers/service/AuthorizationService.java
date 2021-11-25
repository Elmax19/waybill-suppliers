package com.itechart.students_lab.waybill_suppliers.service;

import com.itechart.students_lab.waybill_suppliers.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final UserRepo userRepo;

    public AuthorizationService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

}
