package com.itechart.students_lab.waybill_suppliers.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "item_category")
@Data
@NoArgsConstructor
public class ItemCategory extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tax_rate", nullable = false)
    private double taxRate;
}
