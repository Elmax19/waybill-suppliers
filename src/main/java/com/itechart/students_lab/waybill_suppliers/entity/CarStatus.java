package com.itechart.students_lab.waybill_suppliers.entity;

import java.util.Optional;

public enum CarStatus {
    FREE, ON_WAY;

    public static Optional<CarStatus> of(String status) {
        for (CarStatus v : values()) {
            if (v.name().equalsIgnoreCase(status)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }
}
